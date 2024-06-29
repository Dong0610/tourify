package dong.datn.tourify.app

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.messaging
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dong.datn.tourify.R
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.firebase.RealTime
import dong.datn.tourify.model.Places
import dong.datn.tourify.utils.SCHEDULE
import dong.datn.tourify.utils.SERVICE
import dong.datn.tourify.utils.USERS
import dong.duan.ecommerce.library.showToast
import dong.duan.travelapp.model.Schedule
import dong.duan.travelapp.model.Service
import dong.duan.travelapp.model.Tour
import dong.duan.travelapp.model.Users
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {

    val prevScreen= mutableStateOf("")
    val detailPlace = mutableStateOf<Places?>(null)
    val detailTour =
        mutableStateOf<Tour?>(null)

    var currentIndex = mutableStateOf(0)
    var isKeyboardVisible = mutableStateOf(false)

    val firestore = Firebase.firestore
    val auth = Firebase.auth
    val realtime = Firebase.firestore
    val storage = Firebase.storage

    val bookingTourNow = mutableStateOf<Tour?>(null)

    fun fogetPassword(text: String) {

    }

    val listTour = mutableStateOf<MutableList<Tour>>(mutableListOf())

    fun authSignUp(email: String, password: String, name: String, callback: (Int) -> Unit) {
        val user = Users(
            Email = email,
            Password = password,
            Name = name,
            LoginType = "Account",
            Role = "User"
        )
        firestore.collection(USERS)
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            user.UId = it.user!!.uid
                            firestore.collection(USERS)
                                .document(user.UId.toString()).set(user)
                                .addOnSuccessListener {
                                    authSignIn = user
                                    auth.signInWithEmailAndPassword(email, password)
                                    callback(1)
                                    showToast(appContext.getString(R.string.signup_success))
                                }
                                .addOnFailureListener { e ->
                                    callback(-1)
                                    showToast("Failed to save user: ${e.message}")
                                }
                        }
                        .addOnFailureListener { e ->
                            callback(-1)
                            showToast("Failed to create user: ${e.message}")
                        }
                } else {
                    showToast(appContext.getString(R.string.email_already_exists))
                    callback(-1)
                }
            }
            .addOnFailureListener { e ->
                showToast("Error checking email: ${e.message}")
                callback(-1)
            }
    }

    fun getAllPlaces(callback: (MutableList<Places>?) -> Unit) {
        Firestore.getListData<Places>("PLACES") {

            Log.d("getAllPlaces",it.toString())
            callback(it)
        }
    }

    fun signInWithEmailPassword(email: String, password: String, onSuccess: (Int) -> Unit) {

        firestore.collection(USERS)
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    showToast(appContext.getString(R.string.user_not_found))
                } else {
                    for (document in documents) {
                        val user = document.toObject(Users::class.java)
                        authSignIn = user
                        showToast(appContext.getString(R.string.login_success))
                        onSuccess.invoke(1)
                        GlobalScope.launch(Dispatchers.Main) {
                            updateNewToken(user.UId!!)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                onSuccess(-1)
                showToast("Error checking credentials: ${exception.message}")
            }
    }

    suspend fun updateNewToken(uId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    com.google.firebase.Firebase.messaging.token.addOnSuccessListener { token ->
                        com.google.firebase.Firebase.firestore.collection(USERS)
                            .document(uId)
                            .update("token", token)
                            .addOnSuccessListener {
                                Log.d("TAG", "updateNewToken: success")
                            }
                            .addOnFailureListener {
                                Log.d("TAG", "updateNewToken: ${it.message}")
                            }
                    }


                }
            } catch (e: Exception) {
                Log.d("TAG", "updateNewToken: ${e.message}")
            }
        }
    }

    fun onModifyLove(tour: Tour) {

    }

    fun loadServiceByTour(tourID: String?, callback: (Service?) -> Unit) {
        RealTime.fetchById<Service>("$SERVICE/$tourID") {
            callback(it)
        }
    }

    fun loadScheduleByTour(tourID: String?, function: (Schedule?) -> Unit) {
        RealTime.fetchById<Schedule>("$SCHEDULE/$tourID") {
            function(it)
        }
    }

    fun getUserComment(uId: String, callback: (Users?) -> Unit) {
        Firestore.fetchById<Users>(Firebase.firestore.collection("$USERS").document(uId)) {
            Log.i("getUserComment", "${it?.toJson()}")
            callback(it)
        }
    }

}

