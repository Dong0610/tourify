package dong.datn.tourify.app

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.messaging
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dong.datn.tourify.R
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.firebase.RealTime
import dong.datn.tourify.model.Chat
import dong.datn.tourify.model.ChatType
import dong.datn.tourify.model.ConversionChat
import dong.datn.tourify.model.OtpCode
import dong.datn.tourify.model.Places
import dong.datn.tourify.screen.client.ClientScreen
import dong.datn.tourify.screen.client.MainActivity
import dong.datn.tourify.ui.theme.findActivity
import dong.datn.tourify.utils.CHAT
import dong.datn.tourify.utils.CONVERSION
import dong.datn.tourify.utils.SCHEDULE
import dong.datn.tourify.utils.SERVICE
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.USERS
import dong.datn.tourify.utils.timeNow
import dong.datn.tourify.widget.navigationTo
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


    val currentEmailVerify= mutableStateOf("")
    val prevScreen= mutableStateOf("")
    val detailPlace = mutableStateOf<Places?>(null)
    val detailTour =
        mutableStateOf<Tour?>(null)
    val  listTour =
        mutableStateOf<MutableList<Tour>>(mutableListOf())

    var currentChat = mutableStateOf<ConversionChat?>(null)
    fun resetCurrentChat() {
        currentChat.value = null
    }
    var currentIndex = mutableStateOf(0)
    var otpCodeResponse =
        mutableStateOf<OtpCode?>(null)


    val firestore = Firebase.firestore
    val auth = Firebase.auth
    val realtime = Firebase.database
    val storage = Firebase.storage

    val bookingTourNow = mutableStateOf<Tour?>(null)

    fun fogetPassword(text: String) {

    }

    var listConverChat: MutableState<List<Chat>> = mutableStateOf(emptyList())


    private var childEventListenerCurrentChat: ChildEventListener? = null
    val lastTourIdByChat = mutableStateOf("")
    fun startListeningForNewChats() {

        val converId = authSignIn!!.ConversionChatId
        listConverChat.value = emptyList()
        val chatReference = realtime.getReference("CHAT/$converId")

        childEventListenerCurrentChat = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chat = snapshot.getValue(Chat::class.java)
                chat?.let {
                    listConverChat.value = listConverChat.value + it
                }
                if (chat!!.chatType == ChatType.FIRST_MESSAGE) {
                    lastTourIdByChat.value = chat.tourId
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
            }
        }

        chatReference.addChildEventListener(childEventListenerCurrentChat as ChildEventListener)
    }


    fun authSignUp(email: String, password: String, name: String, callback: (Int) -> Unit) {
        val user = Users(
            Email = email,
            Password = password,
            Name = name,
            LoginType = "Account",
            Role = "User"
        )
        firestore.collection(USERS)
            .whereEqualTo("email", email)
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


    fun getAllTour(callback: (MutableList<Tour>?) -> Unit) {
        Firestore.getListData<Tour>("$TOUR") {
            callback(it)
        }
    }

    fun signInWithEmailPassword(email: String, password: String, onSuccess: (Int) -> Unit) {
        val trimmedEmail = email.trim()
        Firebase.firestore.collection("USERS")
            .whereEqualTo("email", trimmedEmail)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    onSuccess(-1)
                } else {
                    val user = it.documents.get(0).toObject(Users::class.java)
                    var userFound = false
                    if (user!!.Password == password) {
                        userFound = true
                        authSignIn = user
                        showToast(appContext.getString(R.string.login_success))
                        GlobalScope.launch(Dispatchers.Main) {
                            updateNewToken(user.UId!!)
                        }
                        onSuccess.invoke(1)


                    }
                    if (!userFound) {
                        showToast(appContext.getString(R.string.invalid_password))
                        onSuccess.invoke(-1)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("SignIn", "Error checking credentials: ${exception.message}", exception)
                showToast("Error checking credentials: ${exception.message}")
                onSuccess.invoke(-1)
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

    val listChatCurrent = mutableStateOf<MutableList<Chat>>(mutableListOf())

    fun gotoChatByTour(tour: Tour, nav: NavController) {

        checkExitConversion(tour) { b, id ->
            if (b) {
                val reference = realtime.getReference(CHAT)
                    .child(id)

                val firstChat = Chat(
                    idChat = "",
                    content = appContext.getString(R.string.get_more_info),
                    staffId = "",
                    clientId = authSignIn!!.UId,
                    staffImage = "",
                    tourId = tour.tourID,
                    senderId = authSignIn!!.UId,
                    clientImage = authSignIn!!.Image,
                    time = timeNow(),
                    chatType = ChatType.FIRST_MESSAGE
                )
                firstChat.idChat = reference.push().key.toString()
                reference.child(firstChat.idChat).setValue(firstChat)
                    .addOnSuccessListener {
                        nav.navigationTo(ClientScreen.ChatScreen.route)
                    }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }
            } else {
                createNewChatWitdConversion(tour, nav)
            }
        }
    }

    private fun getListChatById(tour: Tour, id: String, callback: (MutableList<Chat>) -> Unit) {
        val listChat = mutableListOf<Chat>()
        realtime.getReference(CHAT)
            .child(tour.tourID!!)
            .child(id)
            .get()
            .addOnSuccessListener { snapshot ->
                for (chatSnapshot in snapshot.children) {
                    val chat = chatSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        listChat.add(chat)
                    }
                }
                callback(listChat)
            }
            .addOnFailureListener {
                callback(mutableListOf())
            }
    }


    private fun createNewChatWitdConversion(tour: Tour, nav: NavController) {
        val chatWithConversion = ConversionChat(
            converId = "",
            clientName = authSignIn?.Name.toString(),
            clientId = authSignIn?.UId!!,
            clientImage = authSignIn?.Image.toString(),
            lastMessageSender = "",
            lastSenderId = authSignIn?.UId!!,
            lastMessageTime = timeNow(),
            lastMessageClientRead = false,
            lastMessageStaffRead = false,
        )
        val docRef = firestore.collection(CONVERSION).document()
        chatWithConversion.converId = docRef.id
        docRef.set(chatWithConversion)
            .addOnSuccessListener {
                nav.navigationTo(ClientScreen.ChatScreen.route)
                val reference = realtime.getReference(CHAT)
                    .child(chatWithConversion.converId)

                val firstChat = Chat(
                    idChat = "",
                    content = appContext.getString(R.string.get_more_info),
                    staffId = "",
                    clientId = authSignIn!!.UId,
                    staffImage = "",
                    tourId = tour.tourID,
                    senderId = authSignIn!!.UId,
                    clientImage = authSignIn!!.Image,
                    time = timeNow(),
                    chatType = ChatType.FIRST_MESSAGE
                )

                firstChat.idChat = reference.push().key.toString()
                reference.child(firstChat.idChat).setValue(firstChat)
                    .addOnSuccessListener {
                        nav.navigationTo(ClientScreen.ChatScreen.route)
                        authSignIn!!.ConversionChatId= chatWithConversion.converId
                        Firestore.updateAsync("$USERS/${firstChat.senderId}", hashMapOf<String, Any>().apply {
                            put("conversionChatId",chatWithConversion.converId)
                        })
                    }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }
            }
            .addOnFailureListener {
                showToast(appContext.getString(R.string.error))
            }
    }

    fun checkExitConversion(tour: Tour, callback: (Boolean, String) -> Unit) {
        val db = Firebase.firestore
        db.collection(CONVERSION)
            .whereEqualTo("clientId", authSignIn!!.UId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    callback(false, "")
                } else {
                    callback(true, documents.documents[0].id)
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors here
                callback(false, "")
            }
    }


    fun sendVerificationEmail(email: String, nav: NavController) {
//        otpCodeResponse.value = OtpCode(timeNow(), generateNumericOTP())
//        MailSender.to(email).subject("Verify your account")
//            .body("Your otp code is: ${otpCodeResponse.value!!.otpCode}")
//            .success {
//             nav.navigationTo(ClientScreen.EnterOtpCodeScreen.route)
//                currentEmailVerify.value=email;
//            }
//            .fail {
//                showToast(appContext.getString(R.string.send_code_faild))
//            }.send()
    }

    fun updatePassword(value: String, function: (Int) -> Unit) {
        Firestore.updateAsync("$USERS/${authSignIn!!.UId}", hashMapOf<String, Any>().apply {
            put("password", value)
        }, {
            function(1)
            authSignIn!!.Password = value
        }, {
            function(-1)
        })
    }


}

