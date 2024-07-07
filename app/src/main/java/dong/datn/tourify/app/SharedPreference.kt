package dong.datn.tourify.app

import android.content.Context
import android.content.SharedPreferences
import dong.duan.travelapp.model.Tour

class SharedPreference {
    private val sharedPreferences: SharedPreferences =
        appContext.getSharedPreferences("SharedPreference", Context.MODE_PRIVATE)!!

    fun edit(block: SharedPreferences.Editor.() -> Unit) {
        with(sharedPreferences.edit()) {
            block()
            apply()
        }
    }

    fun getBollean(key: String, default: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    fun putBollean(key: String, value: Boolean) {
        edit {
            putBoolean(key, value)
        }
    }

    fun getString(key: String, default: String? = null): String? {
        return sharedPreferences.getString(key, default)
    }

    fun putString(key: String, value: String) {
        edit {
            putString(key, value)
        }
    }

    fun getInt(key: String, default: Int = 0): Int {
        return sharedPreferences.getInt(key, default)
    }

    fun putInt(key: String, value: Int) {
        edit {
            putInt(key, value)
        }
    }

    fun getLong(key: String, default: Long = 0L): Long {
        return sharedPreferences.getLong(key, default)
    }

    fun putLong(key: String, value: Long) {
        edit {
            putLong(key, value)
        }
    }

    fun clear() {
        edit {
            clear()
        }
    }

}

val sharedPreferences: SharedPreference
    get() {
        return SharedPreference()
    }
var isSetUpLanguage: Boolean
    get() = sharedPreferences.getBollean("isSetupLang", false)
    set(value) = sharedPreferences.putBollean("isSetupLang", value)
var firstStartApp: Boolean
    get() = sharedPreferences.getBollean("firstStartApp", false)
    set(value) = sharedPreferences.putBollean("firstStartApp", value)
var appLanguageCode: String
    get() = sharedPreferences.getString("appLanguageCode", "en")!!
    set(value) = sharedPreferences.putString("appLanguageCode", value)
var isPostNotification: Boolean
    get() = sharedPreferences.getBollean("postNotification", false)
    set(value) = sharedPreferences.putBollean("postNotification", value)

var isChooseUiTheme: Boolean
    get() = sharedPreferences.getBollean("isChooseUiTheme", false)
    set(value) = sharedPreferences.putBollean("isChooseUiTheme", value)
var isRequestPermission: Boolean
    get() = sharedPreferences.getBollean("isRequestPermission", false)
    set(value) = sharedPreferences.putBollean("isRequestPermission", value)
var currentTheme: Int
    get() = sharedPreferences.getInt("currentTheme", 1)
    set(value) = sharedPreferences.putInt("currentTheme", value)
var isShowTrailer: Boolean
    get() = sharedPreferences.getBollean("showTrailer", true)
    set(value) = sharedPreferences.putBollean("showTrailer", value)

var lastChatTour: Tour?
    get() {
        val data = sharedPreferences.getString("lastChatTour", "")
        if (data != null) {
            return if (data.isEmpty()) null else Tour().fromJson(data)
        } else {
            return null
        }
    }
    set(value) {
        if (value != null) {
            sharedPreferences.putString("lastChatTour", value.toJson())
        } else {
            sharedPreferences.putString("lastChatTour", "")
        }

    }





























