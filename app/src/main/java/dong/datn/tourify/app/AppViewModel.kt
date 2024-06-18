package dong.datn.tourify.app

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.messaging
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dong.datn.tourify.R
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.utility.KEY_USER
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
    val firestore = Firebase.firestore
    val auth = Firebase.auth
    val realtime = Firebase.firestore
    val storage = Firebase.storage


    // clients
    fun fogetPassword(text: String) {

    }

    fun authSignUp(email: String, password: String, name: String, callback: (Boolean) -> Unit) {
        val user = Users(
            Email = email,
            Password = password,
            Name = name,
            LoginType = "Account",
            Role = "User"
        )
        firestore.collection(KEY_USER)
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            user.UId = it.user!!.uid
                            firestore.collection(KEY_USER)
                                .document(user.UId.toString()).set(user)
                                .addOnSuccessListener {
                                    authSignIn = user
                                    auth.signInWithEmailAndPassword(email, password)
                                    callback(true)
                                    showToast(appContext.getString(R.string.signup_success))
                                }
                                .addOnFailureListener { e ->
                                    callback(false)
                                    showToast("Failed to save user: ${e.message}")
                                }
                        }
                        .addOnFailureListener { e ->
                            callback(false)
                            showToast("Failed to create user: ${e.message}")
                        }
                } else {
                    showToast(appContext.getString(R.string.email_already_exists))
                    callback(false)
                }
            }
            .addOnFailureListener { e ->
                showToast("Error checking email: ${e.message}")
                callback(false)
            }
    }

    fun signInWithEmailPassword(email: String, password: String, onSuccess: (Int) -> Unit) {

        firestore.collection(KEY_USER)
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
                        com.google.firebase.Firebase.firestore.collection(KEY_USER)
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

}

