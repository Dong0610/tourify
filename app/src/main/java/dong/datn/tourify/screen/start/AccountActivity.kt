package dong.datn.tourify.screen.start

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.firstStartApp
import dong.datn.tourify.app.isSetUpLanguage
import dong.datn.tourify.screen.client.MainActivity
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.USERS
import dong.datn.tourify.widget.animComposable
import dong.duan.ecommerce.library.showToast
import dong.duan.travelapp.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Arrays


sealed class AccountScreen(var route: String) {
    data object LanguageScreen : AccountScreen("language")
    data object SignUpScreen : AccountScreen("signup")
    data object SignInScreen : AccountScreen("signin")
    data object ForgetPassWordScreen : AccountScreen("forget_password")
    data object EnterOtpCodeScreen : AccountScreen("enter_tp_code")
    data object ResetPassWordScreen : AccountScreen("update_password")
}

@AndroidEntryPoint
open class AccountActivity : ComponentActivity() {
    private val auth = FirebaseAuth.getInstance()

    private lateinit var callbackManager: CallbackManager
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oneTapClient = Identity.getSignInClient(this@AccountActivity)
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    Log.d("TAG", "Cancelled onFacebookAccess")
                }

                override fun onError(error: FacebookException) {
                    Log.d("TAG", "onError: ${error.message}")
                }
            })

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id)) // Replace with your actual Web client ID
                    .setFilterByAuthorizedAccounts(false) // Set to true if you want to filter by authorized accounts
                    .build()
            )
            .build()
        currentTheme = 1
        setContent {

            TourifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigation(innerPadding)
                }
            }
        }
    }

    private fun updateUi(task: Task<AuthResult>, type: String) {
        val user = auth.currentUser
        if (user != null) {
            val userPush = Users(
                Name = user.displayName!!,
                Role = "User",
                Email = user.email!!,
                LoginType = type,
                Image = user.photoUrl.toString()
            )

            userPush.UId = task.result.user!!.uid
            Log.d("TAG", "US ID : ${userPush.UId}")
            viewModels.checkExitEmail(userPush.Email) {
                if (it == 0) {
                    viewModels.firestore.collection(USERS)
                        .document(userPush.UId).set(userPush)
                        .addOnSuccessListener {
                            showToast(getString(R.string.signup_success))
                        }
                        .addOnFailureListener { e ->
                            showToast("Failed to save user: ${e.message}")
                        }
                } else {
                    viewModels.firestore.collection(USERS)
                        .document(userPush.UId).update("loginType", type)
                        .addOnSuccessListener {
                            showToast(getString(R.string.signup_success))
                        }
                }
            }

            viewModels.getUserById(auth.currentUser!!.uid) {
                authSignIn = it
                showToast(getString(R.string.login_success))
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
                GlobalScope.launch(Dispatchers.Main) {
                    viewModels.updateNewToken(it!!.UId)
                }
                firstStartApp = true
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        updateUi(task, "Facebook")
                    }

                }.addOnFailureListener {
                    Log.d("TAG","Authentication failed: ${it.message}")
                }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun signInWithGoogle() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    googleSignInLauncher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("TAG", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                Log.e("TAG","error:"+ e.localizedMessage)
            }
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential: SignInCredential =
                    oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                updateUi(task, "Google")
                            } else {
                                Log.w("TAG", "signInWithCredential:failure", task.exception)
                            }
                        }
                } else {
                    Log.d("TAG", "No ID token!")
                }
            } catch (e: ApiException) {
                Log.d("TAG", "onActivityResult: ${e.message}")
            }
        }
    }

    private fun signInWithFacebook() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    @Composable
    fun MainNavigation(innerPadding: PaddingValues) {
        val navController = rememberNavController()
        val systemUiController = rememberSystemUiController()
        val statusBar = if (currentTheme == 1) white else black
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = statusBar,
            )
        }
        NavHost(
            modifier = Modifier.padding(paddingValues = innerPadding),
            navController = navController,
            startDestination = if(!isSetUpLanguage) AccountScreen.LanguageScreen.route else AccountScreen.SignInScreen.route
        ) {
            animComposable(AccountScreen.LanguageScreen.route) {
                LanguageScreen(navController) {
                    startActivity(Intent(this@AccountActivity, MainActivity::class.java))
                    finish()
                }
            }
            animComposable(AccountScreen.SignUpScreen.route) {
                SignUpScreen(navController, viewModels,{
                    signInWithFacebook()
                },{
                    signInWithGoogle()
                })
            }
            animComposable(AccountScreen.SignInScreen.route) {
                SignInScreen(navController, viewModels,{
                    signInWithFacebook()
                },{
                    signInWithGoogle()
                })
            }
            animComposable(AccountScreen.ForgetPassWordScreen.route) {
                ForgetPassScreen(navController, viewModels)
            }

            animComposable(AccountScreen.EnterOtpCodeScreen.route) {
                EnterOtpScreen(navController, viewModels)
            }
            animComposable(AccountScreen.ResetPassWordScreen.route) {
                ResetPassScreen(navController, viewModels)
            }



        }
    }
}