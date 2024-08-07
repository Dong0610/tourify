package dong.datn.tourify.app

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.messaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dong.datn.tourify.R
import dong.datn.tourify.database.LoveItem
import dong.datn.tourify.database.LoveItemRemote
import dong.datn.tourify.database.OrderTime
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.firebase.RealTime
import dong.datn.tourify.firebase.putImgToStorage
import dong.datn.tourify.model.Chat
import dong.datn.tourify.model.ChatType
import dong.datn.tourify.model.Comment
import dong.datn.tourify.model.CommentType
import dong.datn.tourify.model.ConversionChat
import dong.datn.tourify.model.Emoji
import dong.datn.tourify.model.Notification
import dong.datn.tourify.model.Order
import dong.datn.tourify.model.OrderStatus
import dong.datn.tourify.model.OtpCode
import dong.datn.tourify.model.Places
import dong.datn.tourify.model.Reply
import dong.datn.tourify.model.Sale
import dong.datn.tourify.screen.client.ClientScreen
import dong.datn.tourify.screen.staff.ChartData
import dong.datn.tourify.screen.start.AccountScreen
import dong.datn.tourify.utils.CHAT
import dong.datn.tourify.utils.COMMENT
import dong.datn.tourify.utils.CONVERSION
import dong.datn.tourify.utils.CallbackType
import dong.datn.tourify.utils.LOVE
import dong.datn.tourify.utils.MailSender
import dong.datn.tourify.utils.NOTIFICATION
import dong.datn.tourify.utils.ORDER
import dong.datn.tourify.utils.SALES
import dong.datn.tourify.utils.SCHEDULE
import dong.datn.tourify.utils.SERVICE
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.USERS
import dong.datn.tourify.utils.timeNow
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.widget.navigationTo
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.utility.generateNumericOTP
import dong.duan.livechat.utility.toJson
import dong.duan.travelapp.model.PaymentMethod
import dong.duan.travelapp.model.Schedule
import dong.duan.travelapp.model.Service
import dong.duan.travelapp.model.Tour
import dong.duan.travelapp.model.TourTime
import dong.duan.travelapp.model.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {

    val currentImage = mutableStateOf("")
    val loadingState = mutableStateOf(false)
    val countUnReadNoti = mutableStateOf(0)
    val listNotifications = mutableStateOf(mutableListOf<Notification>())
    val listConversations = mutableStateOf<MutableList<ConversionChat>>(mutableListOf())
    val currentEmailVerify= mutableStateOf("")
    val prevScreen= mutableStateOf("")
    val detailPlace = mutableStateOf<Places?>(null)
    val detailTour =
        mutableStateOf<Tour?>(null)
    val  listTour =
        mutableStateOf<MutableList<Tour>>(mutableListOf())

    val listSale =
        mutableStateOf<MutableList<Sale>>(mutableListOf())

    var currentChat = mutableStateOf<ConversionChat?>(null)
    fun resetCurrentChat() {
        currentChat.value = null
    }
    var currentIndex = mutableStateOf(0)
    var otpCodeResponse =
        mutableStateOf<OtpCode?>(null)
    var currentTourTime = mutableStateOf<TourTime?>(null)

    val firestore = Firebase.firestore
    val auth = Firebase.auth
    val realtime = Firebase.database
    val storage = Firebase.storage

    val bookingTourNow = mutableStateOf<Tour?>(null)
    val tourTimeSelected = mutableStateOf<TourTime?>(null)
    val countChild =
        mutableStateOf(0)
    val countToddle= mutableStateOf(0)
    val countAdult =
        mutableStateOf(0)
    val totalPrice =
        mutableStateOf(0.0)

    var listChatCurrent: MutableState<MutableList<Chat>> = mutableStateOf(mutableListOf())
    private var childEventListenerCurrentChat: ChildEventListener? = null
    val lastTourIdByChat = mutableStateOf("")
    fun startListeningForNewChats() {

        val converId = authSignIn!!.ConversionChatId
        listChatCurrent.value = mutableListOf()
        val chatReference = realtime.getReference("CHAT/$converId")

        childEventListenerCurrentChat = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chat = snapshot.getValue(Chat::class.java)
                chat?.let {
                    listChatCurrent.value = (listChatCurrent.value + it).toMutableList()
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

    fun startListeningForNewChatsByConverId(converId: String) {
        listChatCurrent.value = mutableListOf()
        val chatReference = realtime.getReference("CHAT/$converId")
        childEventListenerCurrentChat = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chat = snapshot.getValue(Chat::class.java)
                chat?.let {
                    listChatCurrent.value = (listChatCurrent.value + it).toMutableList()
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

    fun listenerCurrentChat() {
        val listChat = mutableListOf<ConversionChat>()
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection(CONVERSION)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                if (e == null && snapshot != null) {
                    listChat.clear()
                    listChat.addAll(snapshot.toObjects(ConversionChat::class.java))
                    listChat.sortBy { it.lastMessageTime }
                    listConversations.value = listChat
                } else {
                    e?.printStackTrace()
                }
            }
    }

    fun listenerNotification() {
        val listNotification = mutableListOf<Notification>()
        FirebaseDatabase.getInstance().getReference(NOTIFICATION)
            .child(authSignIn!!.UId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listNotification.clear()
                    var unreadCount = 0

                    for (dataSnapshot in snapshot.children) {
                        val notification = dataSnapshot.getValue(Notification::class.java)
                        if (notification != null) {
                            if (!notification.isRead) {
                                unreadCount++
                            }
                            listNotification.add(notification)
                        }
                    }
                    listNotification.reverse()
                    countUnReadNoti.value = unreadCount
                    listNotifications.value = listNotification
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Notification", error.message)
                }
            })
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
        if (hasInternetConnection(appContext)) {
            Firebase.firestore.collection("USERS")
                .whereEqualTo("email", trimmedEmail)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        onSuccess(-1)
                        showToast(appContext.getString(R.string.invalid_email))
                    } else {
                        val user = it.documents.get(0).toObject(Users::class.java)
                        var userFound = false
                        if (user!!.Password == password) {
                            userFound = true
                            authSignIn = user
                            showToast(appContext.getString(R.string.login_success))
                            GlobalScope.launch(Dispatchers.Main) {
                                updateNewToken(user.UId)
                                pushNewNotification(
                                    title = appContext.getString(R.string.sign_in_success),
                                    image = successLogin,
                                    senderId = user.UId,
                                    content = appContext.getString(R.string.login_at) + getPhoneName() + appContext.getString(
                                        R.string.at_time
                                    ) + timeNow(),
                                    reciveId = user.UId,
                                    type = "LOGIN",
                                    link = "",
                                    router = "history_screen"

                                )
                                if (user.Role == "User") {
                                    loadTourLove(user.UId)
                                    loadLastChatTour(user)
                                }

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
        } else {
            showToast(appContext.getString(R.string.no_internet))
        }
    }

    private fun loadLastChatTour(uId: Users) {
        firestore.collection(CONVERSION)
            .document(uId.ConversionChatId)
            .get()
            .addOnSuccessListener {
                val conversionChat = it.toObject(ConversionChat::class.java)
                if (conversionChat!= null) {
                   Firestore.fetchById<Tour>("$TOUR/${conversionChat.lastChatTourId}"){
                       lastChatTour= it
                   }
                }
            }
    }

    private fun loadTourLove(uId: String) {
        realtime.getReference(LOVE)
            .child(uId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    snapshot.children.forEach { doc ->
                        val loveItem = doc.getValue(LoveItemRemote::class.java)
                        loveItem?.toJson()?.let { Log.d("LoveTag", it) }
                        loveItem?.let {
                            CoroutineScope(Dispatchers.IO).launch {
                                database.loveDao().insertItem(
                                    LoveItem(
                                        tourId = it.tourId,
                                        tourName = it.tourName
                                    )
                                )
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("LoveTag", "Error getting data", exception)
            }

    }

    val successLogin =
        "https://static-00.iconduck.com/assets.00/alert-success-icon-1024x1024-aobtkid4.png";

    fun pushNewNotification(
        title: String,
        image: String,
        senderId: String,
        reciveId: String,
        content: String,
        type: String = "COMMON",
        link: String,
        router: String, callback: ((Notification) -> Unit?)? = null
    ) {
        val notification = Notification(
            notiId = "",
            senderId = senderId,
            title = title,
            content = content,
            time = timeNow(),
            isRead = false,
            type = type,
            image = image,
            link = link,
            route = router,
        )

        RealTime.Push<Notification>("$NOTIFICATION/$reciveId")
            .newId()
            .withId {
                notification.notiId = it
            }
            .set(notification)
            .finish {
                Log.d("PushNoti", "")
                callback?.invoke(notification)
            }.error {
                showToast(it)
            }.executeAsync()

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

    fun onModifyLove(tour: Tour, callback: (Boolean) -> Unit) {
        GlobalScope.launch {
            val isLove = database.loveDao().doesItemExist(tour.tourID)
            if (isLove) {
                database.loveDao().deleteItem(tour.tourID)
                RealTime.delete("$LOVE/${authSignIn!!.UId}/${tour.tourID}", {}, {})
                callback(false)
            } else {
                val love = LoveItem(tourName = tour.tourName, tourId = tour.tourID)
                database.loveDao().insertItem(love)
                FirebaseDatabase.getInstance().getReference("LOVE")
                    .child(authSignIn!!.UId)
                    .child(tour.tourID)
                    .setValue(love)
                    .addOnSuccessListener {
                        callback(true)
                    }
                    .addOnFailureListener {
                        callback(false)
                    }
            }
        }

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

    fun gotoChatByTour(tour: Tour, nav: NavController) {
        lastChatTour = tour
        checkExitConversion { b, id ->
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
                        Firestore.updateAsync("$CONVERSION/$id", hashMapOf<String, Any>().apply {
                            put("lastChatTourId", tour.tourID)
                        },{
                        },{

                        })
                        nav.navigationTo(ClientScreen.ChatScreen.route)
                    }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }
            } else {
                createNewChatWithConversion(tour, nav)
            }
        }
    }


    private fun createNewChatWithConversion(tour: Tour, nav: NavController) {
        val chatWithConversion = ConversionChat(
            converId = "",
            clientName = authSignIn?.Name.toString(),
            clientId = authSignIn?.UId!!,
            clientImage = authSignIn?.Image.toString(),
            lastMessageSender = "",
            lastChatTourId = tour.tourID,
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
                        authSignIn = authSignIn!!.apply {
                            ConversionChatId = chatWithConversion.converId
                        }
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

    fun checkExitConversion(callback: (Boolean, String) -> Unit) {
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
        otpCodeResponse.value = OtpCode(timeNow(), generateNumericOTP())
        MailSender.to(email).subject("Verify your account")
            .body("Your otp code is: ${otpCodeResponse.value!!.otpCode}")
            .success {
                nav.navigationTo(AccountScreen.EnterOtpCodeScreen.route)
                currentEmailVerify.value = email;
                otpCodeSend = otpCodeResponse.value
            }
            .fail {
                showToast(appContext.getString(R.string.send_code_faild))
            }.send()
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

    fun sendMessage(message: String, idTour: String, callback: () -> Unit) {
        val reference = realtime.getReference(CHAT)
            .child(authSignIn!!.ConversionChatId)

        val firstChat = Chat(
            idChat = "",
            content = message,
            staffId = "",
            clientId = authSignIn!!.UId,
            staffImage = "",
            tourId = idTour,
            senderId = authSignIn!!.UId,
            clientImage = authSignIn!!.Image,
            time = timeNow(),
            chatType = ChatType.MESSAGE
        )

        firstChat.idChat = reference.push().key.toString()
        reference.child(firstChat.idChat).setValue(firstChat)
            .addOnFailureListener {
                showToast(it.message.toString())
            }
            .addOnSuccessListener {
                callback.invoke()
            }
    }

    fun getUserById(id: String, callback: (Users?) -> Unit) {
        firestore.collection(USERS)
            .document(id)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    callback(it.toObject(Users::class.java)!!)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "getUserById: ${it.message}")
                callback(null)
            }
    }

    fun checkExitEmail(email: String, callback: (Int) -> Unit) {
        firestore.collection(USERS)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    callback(0)
                } else {
                    val users = it.documents.get(0).toObject(Users::class.java)
                    if (users!!.LoginType == "Account") {
                        callback(0)
                    } else {
                        callback(1)
                    }
                }
            }
            .addOnFailureListener {
                showToast(it.message.toString())
            }
    }

    fun sendMessage(message: String, idTour: String, currentId: String, callback: () -> Unit) {
        val reference = realtime.getReference(CHAT)
            .child(currentId)

        val firstChat = Chat(
            idChat = "",
            content = message,
            staffId = "",
            clientId = authSignIn!!.UId,
            staffImage = "",
            tourId = idTour,
            senderId = authSignIn!!.UId,
            clientImage = authSignIn!!.Image,
            time = timeNow(),
            chatType = ChatType.MESSAGE
        )

        firstChat.idChat = reference.push().key.toString()
        reference.child(firstChat.idChat).setValue(firstChat)
            .addOnFailureListener {
                showToast(it.message.toString())
            }
            .addOnSuccessListener {
                callback.invoke()
            }
    }

    fun resetPassword(value: String, function: () -> Unit) {
        firestore.collection("$USERS")
            .whereEqualTo("email", currentEmailVerify.value)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    showToast(appContext.getString(R.string.eror_during_update))
                } else {
                    pushNewNotification(
                        title = appContext.getString(R.string.reset_password_success),
                        senderId = it.documents.get(0).id,
                        image = "https://cdn-icons-png.flaticon.com/512/13731/13731195.png",
                        content = appContext.getString(R.string.reset_password_at_time)+ " ${timeNow()}",
                        reciveId = it.documents.get(0).id,
                        type = "RESET_PASSWORD",
                        link = "",
                        router = ""
                    )
                    it.documents.get(0).reference.update("password", value)
                        .addOnSuccessListener {
                            function.invoke()
                            showToast(appContext.getString(R.string.reset_password_success))
                        }
                        .addOnFailureListener {
                            showToast(it.message.toString())
                        }
                }
            }


    }

    var currentOrder = mutableStateOf<Order?>(null)
    val notes =
        mutableStateOf("")
    val salePrice = mutableStateOf(0.0)

    private fun extractNumber(input: String): Int? {
        val regex = Regex("_(\\d+)$")
        val matchResult = regex.find(input)
        return matchResult?.groups?.get(1)?.value?.toInt()
    }
    fun creteOrderByTour(
        tour: Tour,
        note: String,
        sale: Double,
        tourTime: TourTime,
        function: (CallbackType, String, Notification) -> Unit
    ) {
        val totalPrice = (countAdult.value * sale) + (countChild.value * sale)

        currentOrder.value = Order(
            orderID = "",
            userOrderId = authSignIn!!.UId,
            tourName = tour.tourName,
            tourTime = tourTime,
            tourID = tour.tourID,
            orderDate = timeNow(),
            saleId = tour.saleId,
            tourPrice = sale,
            prePayment = totalPrice * percentDeposit,
            totalPrice = totalPrice,
            staffConfirmId = "",
            note = note,
            invoiceUrl = "",
            adultCount = countAdult.value,
            childCount = countChild.value,
            paymentMethod = PaymentMethod(),
            orderStatus = OrderStatus.PENDING,
            cancelReason = "",
            cancelDate = "",
            cancelBefore = "",
        )

        val count =
            tourTime.count - (currentOrder.value!!.adultCount + currentOrder.value!!.childCount)
        Firestore.pushAsync<Order>("$ORDER")
            .withId {
                currentOrder.value?.orderID = it
            }
            .set(currentOrder.value!!)
            .finish {
                updateCountInTourTime(currentOrder.value!!.tourID, tourTime.timeID, count)
                pushNewNotification(
                    "Successful tour booking",
                    "https://firebasestorage.googleapis.com/v0/b/travelapp-datn.appspot.com/o/OTHER%2Fimge_push.png?alt=media&token=50f72c11-6462-4fd0-b7c4-65bd80a75b32",
                    authSignIn!!.UId,
                    authSignIn!!.UId,
                    "You just book a successful tour at ${timeNow()}. We will handle you in the shortest time",
                    type = "BOOKING",
                    link = "",
                    router = "detail_image_screen"
                ) {
                    function.invoke(CallbackType.SUCCESS, currentOrder.value!!.orderID, it)
                }


            }
            .error {
                function(CallbackType.ERROR, "", Notification())
            }.execute()
    }

    fun updateCountInTourTime(documentId: String, timeID: String, newCount: Int) {
        val firestore = FirebaseFirestore.getInstance()
        val documentRef = firestore.collection("$TOUR").document(documentId)
        documentRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val tourTime = document.get("tourTime") as? List<Map<String, Any>>
                        ?: return@addOnSuccessListener
                    val updatedTourTime = tourTime.map {
                        if (it["timeID"] == timeID) {
                            it.toMutableMap().apply { put("count", newCount) }
                        } else {
                            it
                        }
                    }
                    documentRef.update("tourTime", updatedTourTime)
                        .addOnSuccessListener {
                            Log.d("UpdateDoc", "Document successfully updated!")
                        }
                        .addOnFailureListener { e ->
                            Log.d("UpdateDoc", "Error updating document: ${e.message}")
                        }
                } else {
                    Log.d("UpdateDoc", "Document not found")
                }
            }
            .addOnFailureListener { e ->
                Log.d("UpdateDoc", "Error getting document: ${e.message}")
            }
    }



    val dialogState = mutableStateOf(false)
    val dialogTitle = mutableStateOf("")
    val dialogMessage = mutableStateOf("")
    val dialogType = mutableStateOf(CallbackType.SUCCESS)
    var dialogCallback: (() -> Unit)? = null

    fun showDialog(
        message: String,
        type: CallbackType,
        title: String = "",
        callback: (() -> Unit)? = null
    ) {
        dialogType.value = type

        dialogTitle.value = title
        dialogMessage.value = message
        dialogCallback = callback
        dialogState.value = true
    }


    fun checkExitOrder(tour: Tour, callback: (Boolean) -> Unit) {
        firestore.collection("$ORDER")
            .get().addOnSuccessListener {
                for (document in it) {
                    val order = document.toObject(Order::class.java)
                    if (order.tourID == tour.tourID && order.orderStatus != OrderStatus.FINISH) {
                        callback(true)
                    }
                    return@addOnSuccessListener
                }
                callback(false)
            }
            .addOnFailureListener {
                callback.invoke(false)
            }
    }

    fun updateToData(url: String?, value: Order?, function: (Boolean) -> Unit) {

    }

    fun saveCommentByTour(
        tour: Tour,
        comment: String,
        star: Float,
        image: MutableList<Any?>,
        function: () -> Unit
    ) {
        image.remove(null)
        val comment = Comment(
            commentId = "",
            uId = authSignIn!!.UId,
            content = comment,
            tourId = tour.tourID,
            timeComment = timeNow(),
            ratting = star,
            commentType = CommentType.COMMENT,
            emoji = Emoji.NONE,
            listImage = mutableListOf(),
            response = mutableListOf()
        )

        if (image.size != 0) {
            pushImageComment(image, tour.tourID) {
                comment.listImage = it.toMutableList()
                RealTime.Push<Comment>("$COMMENT/${tour.tourID}")
                    .newId()
                    .withId {
                        comment.commentId = it
                    }
                    .set(comment)
                    .finish {
                        val avgStar = tour.star * tour.countRating;
                        tour.star = (avgStar + star) / (tour.countRating + 1)
                        Firestore.updateAsync(
                            "$TOUR/${tour.tourID}",
                            hashMapOf("star" to tour.star, "countRating" to tour.countRating + 1)
                        )
                        function.invoke()

                    }
                    .error { }
                    .executeAsync()
            }
        } else {
            RealTime.Push<Comment>("$COMMENT/${tour.tourID}")
                .newId()
                .withId {
                    comment.commentId = it
                }
                .set(comment)
                .finish {
                    val avgStar = tour.star * tour.countRating;
                    tour.star = (avgStar + star) / (tour.countRating + 1)
                    Firestore.updateAsync(
                        "$TOUR/${tour.tourID}",
                        hashMapOf("star" to tour.star, "countRating" to tour.countRating + 1)
                    )
                    function.invoke()
                }
                .error { }
                .executeAsync()
        }


    }

    private fun pushImageComment(
        imageData: MutableList<Any?>,
        tourID: String,
        callback: (MutableList<String>) -> Unit
    ) {
        val listUrl = mutableListOf<String>()
        for (i in 0 until imageData.size) {
            val reference = FirebaseStorage.getInstance().getReference("Comment/$tourID")
            reference.putImgToStorage(appContext, imageData.get(i) as Uri) {
                listUrl.add(it.toString())
                if (listUrl.size == imageData.size) {
                    callback.invoke(listUrl)
                }
            }
        }
    }

    fun replyComment(value: String, comment: Comment, function: () -> Unit) {
        val reponse = Reply(
            commentId = comment.commentId + "_" + comment.response.size + 1,
            uId = authSignIn!!.UId,
            content = value,
            timeComment = timeNow(),
            emoji = Emoji.NONE
        )
        comment.response.add(reponse)
        RealTime.udapteAsync(
            "$COMMENT/${comment.tourId}/${comment.commentId}",
            comment,
            { function.invoke() },
            {})
    }

    fun replyReplyComment(
        value: String,
        comment: Comment,
        reply: String,
        function: (Reply) -> Unit
    ) {
        val reponse = Reply(
            commentId = comment.commentId + "_" + comment.response.size + 1,
            uId = authSignIn!!.UId,
            content = value,
            timeComment = timeNow(),
            emoji = Emoji.NONE
        )
        comment.response.add(reponse)
        RealTime.udapteAsync(
            "$COMMENT/${comment.tourId}/${reply}/response",
            comment.response,
            { function.invoke(reponse) },
            {})
    }


    val listOrders = mutableStateOf<MutableList<Order>>(mutableListOf())


    val listTourStaff = mutableStateOf<MutableList<Tour>>(mutableListOf())
    val listAllOrder = mutableStateOf(mutableListOf<Order>())
    val ordersByMonth = mutableStateOf(mutableListOf<ChartData>())
    val listPlaces = mutableStateOf(mutableListOf<Places>())
    val listSalesManager = mutableStateOf(mutableListOf<Sale>())
    var currentSale = mutableStateOf<Sale?>(null)


    val titleScreen = mutableStateOf("")

    private fun pushImageTour(
        imageData: MutableList<Any?>, tourID: String, callback: (MutableList<String>) -> Unit
    ) {
        imageData.remove(null)
        val listUrl = mutableListOf<String>()
        for (i in 0 until imageData.size) {
            val reference = FirebaseStorage.getInstance().getReference("Comment/$tourID")

            reference.putImgToStorage(appContext, imageData.get(i) as Uri) {
                listUrl.add(it.toString())
                if (listUrl.size == imageData.size) {
                    callback.invoke(listUrl)
                }
            }
        }
    }

    fun createNewTour(tour: Tour, listImage: MutableList<Any?>, function: (Int, Tour?) -> Unit) {
        listImage.toMutableList().apply {
            remove(null)
        }
        val docRef = FirebaseFirestore.getInstance().collection("$TOUR").document()
        val newId = docRef.id
        pushImageTour(listImage, newId) { imae ->
            tour.tourID = newId
            tour.tourImage = imae
            docRef.set(tour).addOnSuccessListener {
                function(1, tour)
            }.addOnFailureListener {
                function(-1, null)
            }

        }

    }

    fun createService(service: Service, tourID: String, function: (Int) -> Unit) {
        FirebaseDatabase.getInstance().getReference("$SERVICE").child(tourID).setValue(service)
            .addOnSuccessListener {
                function(1)
            }.addOnFailureListener {
                function(-1)
            }
    }

    fun createSchedule(schedule: Schedule, tourID: String, function: (Int) -> Unit) {
        FirebaseDatabase.getInstance().getReference("$SCHEDULE").child(tourID).setValue(schedule)
            .addOnSuccessListener {
                function(2)
            }.addOnFailureListener {
                function(-1)
            }
    }

    fun saveNewSales(sales: Sale, image: Any?, callback: (Int, Sale?) -> Unit) {
        val docRef = FirebaseFirestore.getInstance().collection("$SALES").document()
        val newId = docRef.id
        val listImage = mutableListOf(image)
        pushImageTour(listImage, newId) { imae ->
            sales.saleId = newId
            sales.saleImage = imae.get(0)
            docRef.set(sales).addOnSuccessListener {
                callback(1, sales)
            }.addOnFailureListener {
                callback(-1, null)
            }

        }
    }


    fun isCurrentDateTimeAfter(targetDateTimeString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
        val targetDateTime = LocalDateTime.parse(targetDateTimeString, formatter)
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.isAfter(targetDateTime)
    }


    fun loadStatus(authSignIn: Users?) {
        viewModelScope.launch {
            try {
                val querySnapshot = FirebaseFirestore.getInstance().collection(ORDER).get().await()

                if (querySnapshot.isEmpty) return@launch

                querySnapshot.documents.forEach { document ->
                    val order =
                        document.toObject(Order::class.java) // Convert to your Order data class
                    if (order != null && order.orderStatus == OrderStatus.PENDING && order.userOrderId == dong.datn.tourify.app.authSignIn?.UId) {
                        withContext(Dispatchers.IO) {
                            val exists = database.orderTimeDao().doesItemExist(order.orderID)
                            if (!isCurrentDateTimeAfter(order.tourTime.endTime) && order.orderStatus == OrderStatus.PENDING) {
                                Firestore.updateAsync("$ORDER/${order.orderID}",
                                    hashMapOf<String, Any>().apply {
                                        put("orderStatus", OrderStatus.FINISH.toString())
                                    })
                            }
                            if (exists) {
                                database.orderTimeDao().updateStatus(
                                    orderId = order.orderID,
                                    status = order.orderStatus.toString(),
                                    startTime = order.tourTime.startTime,
                                    endTime = order.tourTime.endTime
                                )
                            } else {
                                database.orderTimeDao().insertItem(
                                    OrderTime(
                                        orderId = order.orderID,
                                        status = order.orderStatus.toString(),
                                        startTime = order.tourTime.startTime,
                                        endTime = order.tourTime.endTime,
                                        tourId = order.tourID
                                    )
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {

                e.printStackTrace()
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun sendMailOrder(id: String, url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Firestore.fetchById<Order>("$ORDER/$id") {
                it?.let { order ->
                    val data = hashMapOf(
                        "orderId" to order.orderID,
                        "childCount" to order.childCount,
                        "adultCount" to order.adultCount,
                        "tourName" to order.tourName,
                        "tourTime" to order.tourTime.toString(),
                        "price" to order.totalPrice,
                        "payment" to order.paymentMethod.toString(),
                        "note" to order.note,
                        "url" to url
                    )
                    val emailContent = """
                                    Order Details
                                
                                    Tour Name: ${data["tourName"]}
                                    Order ID: ${data["orderId"]}
                                    Tour Time: From ${order.tourTime.startTime} to ${order.tourTime.endTime}
                                    Price: ${data["price"]}
                                    Note: ${data["note"]}
                        
                                """.trimIndent()
                    MailSender.to("bdong0610@gmail.com").subject("Booking tour success")
                        .body(emailContent).success {
                            Log.d("Mail", "$emailContent")
                        }.fail {
                            showToast(appContext.getString(R.string.send_code_faild))
                        }.send()
                }
            }
        }
    }

    fun calculateNewTimes(oldStart: String, oldEnd: String): Pair<String, String> {
        val format = "HH:mm dd/MM/yyyy"
        Log.d("ValueUpdate", "Using format: $format")
        val formatter = DateTimeFormatter.ofPattern(format)

        try {
            val oldStartTime = LocalDateTime.parse(oldStart, formatter)
            val oldEndTime = LocalDateTime.parse(oldEnd, formatter)
            val durationBetweenOldTimes = ChronoUnit.MINUTES.between(oldStartTime, oldEndTime)
            val now = LocalDateTime.now()
            val newStartTime =
                now.plusDays(3).withHour(oldStartTime.hour).withMinute(oldStartTime.minute)
                    .withSecond(oldStartTime.second)
            val newEndTime =
                newStartTime.plusMinutes(durationBetweenOldTimes).withHour(oldStartTime.hour)
                    .withMinute(oldStartTime.minute)
                    .withSecond(oldStartTime.second)
            val newStartTimeStr = newStartTime.format(formatter)
            val newEndTimeStr = newEndTime.format(formatter)
            return Pair(newStartTimeStr, newEndTimeStr)
        } catch (e: Exception) {
            Log.e("ValueUpdate", "Error calculating new times: ${e.message}")
            throw e
        }
    }


    fun updateTourTimes(
        tourTimes: MutableList<TourTime>, callback: (MutableList<TourTime>) -> Unit
    ) {
        Log.d("ValueUpdate", "\nData: ${tourTimes.toJson()}")
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")

        tourTimes.forEachIndexed { _, tourTime ->

            val (newStartTime, newEndTime) = calculateNewTimes(
                tourTime.startTime, tourTime.endTime
            )
            tourTime.startTime = newStartTime
            tourTime.endTime = newEndTime
        }
        callback.invoke(tourTimes)
    }

    fun updateTourTimeData(it: MutableList<Tour>?) {

        Log.d("update", "Tour Time ${it.toJson()} updated")
        if (it?.size != 0 && it != null) {
            it.forEachIndexed { index, tour ->
                val listTimeNew = tour.tourTime
                updateTourTimes(listTimeNew) { data ->
                    Log.d("update", "Tour Time ${data.toJson()} updated")
                    FirebaseFirestore.getInstance().collection("$TOUR").document(tour.tourID)
                        .update(hashMapOf<String?, Any?>().apply {
                            put("tourTime", data)
                        }).addOnSuccessListener {
                            Log.d("update", "updateTourTime successfully")
                        }.addOnFailureListener {
                            showToast(it.message.toString())
                        }
                }
            }
        }

    }

    fun confirmCancelOrder(order: Order, nav: Tour, callback: (Boolean) -> Unit) {
        val countData = mutableStateOf(0)


        Firestore.fetchById<Tour>("$TOUR/${order.tourID}") {
            Log.d("TestData", "$TOUR/${order.tourID} , data : ${it?.tourTime.toJson()}")
            it?.tourTime?.forEach {
                if (extractNumber(it.timeID).toString() == order.tourTime.timeID) {
                    countData.value = it.count
                }
            }
        }



        FirebaseFirestore.getInstance().collection("$ORDER").document(order.orderID)
            .update(hashMapOf<String, Any?>().apply {
                put("orderStatus", OrderStatus.CANCELED.toString())
            }).addOnSuccessListener {
                MailSender.to("bdong0610@gmail.com").subject("Order canceled")
                    .body("Your order has been canceled by id ${order.orderID} with tour \"${nav.tourName}\". We have refunded your order with ${order.prePayment.toCurrency()}")
                    .success {

                    }.fail { }
                    .send()
                callback.invoke(true)
                pushNewNotification(
                    title = "Order canceled",
                    image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPELEX6ylJucNrA3W8SEAHLD8f5DFyky6UsA&s",
                    senderId = "",
                    reciveId = order.userOrderId,
                    content = "Your order has been canceled by id ${order.orderID} with tour \"${nav.tourName}\". We have refunded your order with ${order.prePayment.toCurrency()}",
                    type = "COMMON",
                    link = "",
                    router = ""
                )
                val count = (countData.value + order.childCount + order.adultCount)
                updateCountInTourTime(order.tourID, order.tourTime.timeID, count)
            }
            .addOnFailureListener {
                showToast(it.message.toString())
                callback.invoke(false)
            }
    }


    fun cancelOrder(value: String, orderID: Order, callback: (Boolean) -> Unit) {
        Firestore.updateAsync("$ORDER/${orderID.orderID}", hashMapOf<String, Any>().apply {
            put("orderStatus", OrderStatus.WAITING.toString())
            put("cancelDate", timeNow())
            put("cancelReason", value)
        }, {
            callback(true)
        }, { callback(false) })
    }


}


























