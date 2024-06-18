package dong.datn.tourify.app.language

import android.content.Context
import android.content.res.Configuration
import android.text.TextUtils
import dong.datn.tourify.app.firstStartApp
import dong.datn.tourify.app.isSetUpLanguage
import dong.datn.tourify.app.sharedPreferences
import java.util.Locale

object LanguageUtil {
    private fun saveLocale(lang: String?) {
        sharedPreferences.putString(LANGUAGE, lang!!)
    }

    fun setupLanguage(context: Context) {
        var languageCode: String? = sharedPreferences.getString(LANGUAGE, "en")
        if (TextUtils.isEmpty(languageCode)) {
            languageCode = Locale.getDefault().language
        }
        val config = Configuration()
        val locale = if ((languageCode != null)) Locale(languageCode) else null
        if (locale != null) {
            Locale.setDefault(locale)
        }
        config.locale = locale
        context.resources.updateConfiguration(config, null)
    }

    fun changeLang(lang: String?, context: Context) {
        if (TextUtils.isEmpty(lang)) return
        val myLocale = Locale(lang)
        saveLocale(lang)
        Locale.setDefault(myLocale)
        val config = Configuration()
        config.locale = myLocale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        isSetUpLanguage = true
    }

    private const val FIRST_OPEN_APP = "FIRST_OPEN_APP"
    const val LANGUAGE = "LANGUAGE"


}

var languageCode: String?
    get() = sharedPreferences.getString(LanguageUtil.LANGUAGE, "")
    set(value) {
        if (value != null) {
            sharedPreferences.putString(LanguageUtil.LANGUAGE, value)
        }
        firstStartApp = false
    }