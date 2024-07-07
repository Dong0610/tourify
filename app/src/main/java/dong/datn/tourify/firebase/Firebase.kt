package dong.datn.tourify.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dong.datn.tourify.firebase.FirebaseApp.listenValueAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FirebaseApp {
    suspend inline fun DatabaseReference.getValueAsync(): DataSnapshot =
        suspendCancellableCoroutine { cont ->
            get().addOnSuccessListener { dataSnapshot ->
                cont.resume(dataSnapshot)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
        }

    suspend inline fun <reified T> getModelById(ref: DatabaseReference): T? =
        withContext(Dispatchers.IO) {
            val dataSnapshot = ref.getValueAsync()
            if (dataSnapshot.exists()) {
                dataSnapshot.getValue(T::class.java)
            } else {
                null
            }
        }

    suspend fun DatabaseReference.listenValueAsync(): DataSnapshot = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { cont ->
            get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val dataSnapshot = task.result
                    cont.resume(dataSnapshot!!)
                } else {
                    val exception = task.exception ?: RuntimeException("Unknown exception")
                    cont.resumeWithException(exception)
                }
            }
        }
    }

    suspend fun DocumentReference.getValueAsync(): DocumentSnapshot =
        suspendCancellableCoroutine { cont ->
            get().addOnSuccessListener { documentSnapshot ->
                cont.resume(documentSnapshot)
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
        }

    suspend inline fun <reified T> getModelById(ref: DocumentReference): T? =
        withContext(Dispatchers.IO) {
            val documentSnapshot = ref.getValueAsync()
            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(T::class.java)
            } else {
                null
            }
        }

    suspend fun DocumentReference.listenValueAsync(): DocumentSnapshot =
        suspendCancellableCoroutine { cont ->
            val listenerRegistration = addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    cont.resumeWithException(exception)
                } else if (documentSnapshot != null && documentSnapshot.exists()) {
                    cont.resume(documentSnapshot)
                } else {
                    cont.resumeWithException(IllegalStateException("Document does not exist"))
                }
            }

            cont.invokeOnCancellation {
                listenerRegistration.remove()
            }
        }

    suspend inline fun <reified T> listenModelById(ref: DocumentReference): T? =
        withContext(Dispatchers.IO) {
            val documentSnapshot = ref.listenValueAsync()
            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(T::class.java)
            } else {
                null
            }
        }
}

object RealTime {
    inline fun <reified T> fetchById(ref: DatabaseReference, crossinline callback: (T?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val service = FirebaseApp.getModelById<T>(ref)
                if (service != null) {
                    callback(service)
                } else {
                    Log.d("TAG", "Data does not exist")
                    callback(null)
                }
            } catch (e: Exception) {
                Log.d("TAG", "Error fetching data: ${e.message}")
                callback(null)
            }
        }
    }

    inline fun <reified T> fetchById(path: String, crossinline callback: (T?) -> Unit) {
        val reference = FirebaseDatabase.getInstance().getReference(path)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val service = FirebaseApp.getModelById<T>(reference)
                if (service != null) {
                    callback(service)
                } else {
                    Log.d("TAG", "Data does not exist")
                    callback(null)
                }
            } catch (e: Exception) {
                Log.d("TAG", "Error fetching data: ${e.message}")
                callback(null)
            }
        }
    }

    inline fun <reified T> getListData(
        path: String,
        crossinline callback: (MutableList<T>?) -> Unit
    ) {
        val reference = FirebaseDatabase.getInstance().getReference(path)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val snapshot = withContext(Dispatchers.IO) {
                    reference.get().await()
                }
                val dataList: MutableList<T> =
                    snapshot.children.mapNotNull { it.getValue(T::class.java) }.toMutableList()
                callback(dataList)
            } catch (e: Exception) {
                Log.d("TAG", "Error fetching data: ${e.message}")
                callback(null)
            }
        }
    }

    inline fun <reified T> listenById(ref: DatabaseReference, crossinline callback: (T?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val dataSnapshot = ref.listenValueAsync()
                val service = dataSnapshot.getValue(T::class.java)
                callback(service)
            } catch (e: Exception) {
                Log.d("TAG", "Error fetching data: ${e.message}")
                callback(null)
            }
        }
    }

    inline fun <reified T> listenById(path: String, crossinline callback: (T?) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference(path)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val dataSnapshot = ref.listenValueAsync()
                val service = dataSnapshot.getValue(T::class.java)
                callback(service)
            } catch (e: Exception) {
                Log.d("TAG", "Error fetching data: ${e.message}")
                callback(null)
            }
        }
    }

    class Push<T>(
        private var paths: String,
        private var data: T? = null,
        private var finishCallback: (() -> Unit)? = null,
        private var errorCallback: ((String) -> Unit)? = null
    ) {
        private var isSetPath = false
        private var newId: String? = null

        fun newId(): Push<T> {
            val newId = FirebaseDatabase.getInstance().reference.push().key
            paths += "/$newId"
            this.newId= newId
            return this
        }

        fun withId(idProvider: (String) -> Unit): Push<T> {
            isSetPath = true

            idProvider.invoke(this.newId!!)
            return this
        }

        fun set(data: T): Push<T> {
            this.data = data
            return this
        }

        fun finish(callback: () -> Unit): Push<T> {
            this.finishCallback = callback
            return this
        }

        fun error(callback: (String) -> Unit): Push<T> {
            this.errorCallback = callback
            return this
        }

        fun execute() {
            val database = FirebaseDatabase.getInstance()
            var ref: DatabaseReference = database.getReference(paths)

            if (data == null) {
                errorCallback?.invoke("Data is not set")
                return
            }
            ref.setValue(data)
                .addOnSuccessListener {
                    finishCallback?.invoke()
                }
                .addOnFailureListener { exception ->
                    errorCallback?.invoke(exception.message ?: "Unknown error")
                }
        }

        fun executeAsync() {
            val database = FirebaseDatabase.getInstance()
            var targetRef: DatabaseReference = database.getReference(paths)

            if (data == null) {
                errorCallback?.invoke("Data is not set")
                return
            }
            GlobalScope.launch(Dispatchers.IO) {
                targetRef.setValue(data)
                    .addOnSuccessListener {
                        finishCallback?.invoke()
                    }
                    .addOnFailureListener { exception ->
                        errorCallback?.invoke(exception.message ?: "Unknown error")
                    }
            }
        }
    }

    fun <T> udapte(
        ref: DatabaseReference,
        data: T,
        onFinish: () -> Unit,
        onError: (String) -> Unit
    ) {
        ref.setValue(data)
            .addOnSuccessListener {
                onFinish()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Unknown error")
            }
    }

    fun <T> udapteAsync(
        ref: DatabaseReference,
        data: T,
        onFinish: () -> Unit,
        onError: (String) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            ref.setValue(data)
                .addOnSuccessListener {
                    onFinish()
                }.addOnFailureListener { exception ->
                    onError(exception.message ?: "Unknown error")
                }.await()
        }
    }

    fun <T> udapte(path: String, data: T, onFinish: () -> Unit, onError: (String) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference(path)
        ref.setValue(data)
            .addOnSuccessListener {
                onFinish()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Unknown error")
            }
    }

    fun <T> udapteAsync(path: String, data: T, onFinish: () -> Unit, onError: (String) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference(path)
        GlobalScope.launch(Dispatchers.IO) {
            ref.setValue(data)
                .addOnSuccessListener {
                    onFinish()
                }.addOnFailureListener { exception ->
                    onError(exception.message ?: "Unknown error")
                }
        }
    }

    fun delete(ref: DatabaseReference, onFinish: () -> Unit, onError: (String) -> Unit) {
        ref.removeValue()
            .addOnSuccessListener {
                onFinish()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Unknown error")
            }
    }

    fun deleteAsync(ref: DatabaseReference, onFinish: () -> Unit, onError: (String) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            ref.removeValue()
                .addOnSuccessListener {
                    onFinish()
                }
                .addOnFailureListener { exception ->
                    onError(exception.message ?: "Unknown error")
                }
                .await()
        }
    }

    fun delete(path: String, onFinish: () -> Unit, onError: (String) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference(path)
        delete(ref, onFinish, onError)
    }

    fun deleteAsync(path: String, onFinish: () -> Unit, onError: (String) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference(path)
        deleteAsync(ref, onFinish, onError)
    }


}

object Firestore {
    inline fun <reified T> fetchById(ref: DocumentReference, crossinline callback: (T?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val model = FirebaseApp.getModelById<T>(ref)
                if (model != null) {
                    callback(model)
                } else {
                    Log.d("TAG", "Data does not exist")
                    callback(null)
                }
            } catch (e: Exception) {
                Log.d("TAG", "Error fetching data: ${e.message}")
                callback(null)
            }
        }
    }

    inline fun <reified T> fetchById(path: String, crossinline callback: (T?) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val documentRef = firestore.document(path)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val model = FirebaseApp.getModelById<T>(documentRef)
                if (model != null) {
                    callback(model)
                } else {
                    Log.d("TAG", "Data does not exist")
                    callback(null)
                }
            } catch (e: Exception) {
                Log.d("TAG", "Error fetching data: ${e.message}")
                callback(null)
            }
        }
    }

    inline fun <reified T> getListData(
        collectionRef: CollectionReference,
        crossinline callback: (MutableList<T>?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val snapshot = collectionRef.get().await()
                val documents = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(T::class.java)
                }
                withContext(Dispatchers.Main) {
                    callback(documents.toMutableList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }

    inline fun <reified T> getListData(
        path: String,
        crossinline callback: (MutableList<T>?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val collectionRef: CollectionReference =
                    FirebaseFirestore.getInstance().collection(path)
                val snapshot = collectionRef.get().await()
                val documents = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(T::class.java)
                }
                withContext(Dispatchers.Main) {
                    callback(documents.toMutableList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }

    inline fun <reified T> listenById(
        path: String,
        crossinline callback: (T?) -> Unit
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val documentRef = firestore.document(path)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val model = FirebaseApp.listenModelById<T>(documentRef)
                if (model != null) {
                    callback(model)
                } else {
                    Log.d("TAG", "Data does not exist")
                    callback(null)
                }
            } catch (e: Exception) {
                Log.d("TAG", "Error listening to data: ${e.message}")
                callback(null)
            }
        }
    }

    inline fun <reified T> listenById(
        ref: DocumentReference,
        crossinline callback: (T?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val model = FirebaseApp.listenModelById<T>(ref)
                if (model != null) {
                    callback(model)
                } else {
                    Log.d("TAG", "Data does not exist")
                    callback(null)
                }
            } catch (e: Exception) {
                Log.d("TAG", "Error listening to data: ${e.message}")
                callback(null)
            }
        }
    }

    class push<T>(
        private val path: String,
        private val idProvider: ((String) -> Unit)? = null,
        private var data: T? = null,
        private var finishCallback: (() -> Unit)? = null,
        private var errorCallback: ((String) -> Unit)? = null,
        private var doSomeThing: (() -> Unit)? = null
    ) {
        fun withId(idProvider: (String) -> Unit): push<T> {
            return push(path, idProvider, data, finishCallback, errorCallback)
        }

        fun set(data: T): push<T> {
            this.data = data
            return this
        }

        fun doSomething(callback: () -> Unit): push<T> {
            this.doSomeThing = callback
            return this
        }

        fun finish(callback: () -> Unit): push<T> {
            this.finishCallback = callback
            return this
        }

        fun error(callback: (String) -> Unit): push<T> {
            this.errorCallback = callback
            return this
        }

        fun execute() {
            if (data == null) {
                errorCallback?.invoke("Data is not set")
                return
            }
            if (path.contains("/")) {
                val firestore = FirebaseFirestore.getInstance()
                val documentRef = firestore.document(path)
                idProvider?.invoke(documentRef.id)
                documentRef.set(data!!)
                    .addOnSuccessListener {
                        doSomeThing?.invoke()
                        finishCallback?.invoke()
                    }
                    .addOnFailureListener { exception ->
                        errorCallback?.invoke(exception.message ?: "Unknown error")
                    }
            } else {
                val firestore = FirebaseFirestore.getInstance().collection(path)
                val documentRef = firestore.document(firestore.document().id)
                idProvider?.invoke(documentRef.id)
                documentRef.set(data!!)
                    .addOnSuccessListener {
                        doSomeThing?.invoke()
                        finishCallback?.invoke()
                    }
                    .addOnFailureListener { exception ->
                        errorCallback?.invoke(exception.message ?: "Unknown error")
                    }
            }
        }
    }

    class pushAsync<T>(
        private val path: String,
        private val idProvider: ((String) -> Unit)? = null,
        private var data: T? = null,
        private var finishCallback: (() -> Unit)? = null,
        private var errorCallback: ((String) -> Unit)? = null,
        private var doSomeThing: (() -> Unit)? = null
    ) {
        fun withId(idProvider: (String) -> Unit): pushAsync<T> {
            return pushAsync(path, idProvider, data, finishCallback, errorCallback)
        }

        fun set(data: T): pushAsync<T> {
            this.data = data
            return this
        }

        fun finish(callback: () -> Unit): pushAsync<T> {
            this.finishCallback = callback
            return this
        }

        fun doSomething(callback: () -> Unit): pushAsync<T> {
            this.doSomeThing = callback
            return this
        }

        fun error(callback: (String) -> Unit): pushAsync<T> {
            this.errorCallback = callback
            return this
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun execute() {
            GlobalScope.launch(Dispatchers.IO) {
                if (data == null) {
                    errorCallback?.invoke("Data is not set")
                }
                val firestore = FirebaseFirestore.getInstance().collection(path)
                val documentRef = firestore.document()
                idProvider?.invoke(documentRef.id)
                try {
                    documentRef.set(data!!)
                        .addOnSuccessListener {
                            doSomeThing?.invoke()
                            finishCallback?.invoke()
                        }
                        .addOnFailureListener { exception ->
                            errorCallback?.invoke(exception.message ?: "Unknown error")
                        }.await()
                } catch (e: Exception) {
                    errorCallback?.invoke(e.message ?: "Unknown error")
                }
            }

        }
    }

    fun update(
        ref: DocumentReference,
        data: HashMap<String, Any>,
        onFinish: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        ref.update(data!!)
            .addOnSuccessListener {
                if (onFinish != null) {
                    onFinish()
                }
            }
            .addOnFailureListener { exception ->
                if (onError != null) {
                    onError(exception.message ?: "Unknown error")
                }
            }
    }
    fun updateAsync(
        path: String, data: HashMap<String, Any>, onFinish: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        val ref = FirebaseFirestore.getInstance().document(path)
        updateAsync(ref, data, onFinish, onError)
    }

    fun updateAsync(
        ref: DocumentReference,
        data: HashMap<String, Any>,
        onFinish: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        Log.d("updateAsync", "Starting update")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                ref.update(data)
                    .addOnSuccessListener {
                        Log.d("updateAsync", "Update successful")
                        onFinish?.invoke()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("updateAsync", "Update failed: ${exception.message}")
                        onError?.invoke(exception.message ?: "Unknown error")
                    }
            } catch (e: Exception) {
                Log.e("updateAsync", "Exception: ${e.message}")
                onError?.invoke(e.message ?: "Unknown error")
            }
        }
    }

    fun update(
        path: String,
        data: HashMap<String, Any>,
        onFinish: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        val ref = FirebaseFirestore.getInstance().document(path)
        update(ref, data, onFinish, onError)
    }



    fun delete(ref: DocumentReference, onFinish: () -> Unit, onError: (String) -> Unit) {
        ref.delete()
            .addOnSuccessListener {
                onFinish()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Unknown error")
            }
    }

    fun deleteAsync(ref: DocumentReference, onFinish: () -> Unit, onError: (String) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            ref.delete()
                .addOnSuccessListener {
                    onFinish()
                }
                .addOnFailureListener { exception ->
                    onError(exception.message ?: "Unknown error")
                }
        }
    }

    fun delete(path: String, onFinish: () -> Unit, onError: (String) -> Unit) {
        val ref = FirebaseFirestore.getInstance().document(path)
        delete(ref, onFinish, onError)
    }

    fun deleteAsync(path: String, onFinish: () -> Unit, onError: (String) -> Unit) {
        val ref = FirebaseFirestore.getInstance().document(path)
        deleteAsync(ref, onFinish, onError)
    }


}




