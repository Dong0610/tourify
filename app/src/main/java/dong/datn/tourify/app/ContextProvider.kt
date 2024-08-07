package dong.datn.tourify.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import dagger.hilt.android.HiltAndroidApp
import dong.datn.tourify.database.AppDatabase
import dong.duan.travelapp.model.Users

@HiltAndroidApp
class ContextProvider : Application() {

    companion object {
        lateinit var appContext: Context
        lateinit var viewModel: AppViewModel
        lateinit var database: AppDatabase
    }

    override fun onCreate() {

        super.onCreate()
        appContext = applicationContext
        viewModel = ViewModelProvider(ViewModelStore(), ViewModelProvider.NewInstanceFactory()).get(
            AppViewModel::class.java
        )
        database = AppDatabase.getDatabase(this)
    }
}

fun getPhoneName(): String {
    return "${Build.MANUFACTURER} ${Build.MODEL}"
}

var viewModels: AppViewModel = ContextProvider.viewModel
val database: AppDatabase = ContextProvider.database
var appContext: Context
    get() = ContextProvider.appContext
    set(value) {
       ContextProvider.appContext = value
    }

var authSignIn: Users?
    get() {
        val data = sharedPreferences.getString("authSignIn", "")
        if (data != null) {
            return if (data.isEmpty()) null else Users().fromJson(data)
        } else {
            return null
        }
    }
    set(value) {
        if (value != null) {
            sharedPreferences.putString("authSignIn", value.toJson())
        } else {
            sharedPreferences.putString("authSignIn", "")
        }

    }

fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

var percentDeposit: Float  get() = sharedPreferences.getFloat("percentDeposit",0.4f)
    set(value) {
        sharedPreferences.putFloat("percentDeposit", value)
    }


















