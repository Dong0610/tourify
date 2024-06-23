package dong.datn.tourify.app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dong.duan.travelapp.model.Users

@HiltAndroidApp
class ContextProvider : Application() {

    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

    }
}


var appViewModels: AppViewModel? = null
val appContext: Context
    get() = ContextProvider.appContext

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
















