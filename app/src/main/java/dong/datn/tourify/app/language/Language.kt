package dong.datn.tourify.app.language

import android.content.Context
import dong.datn.tourify.R

data class Language(
    var id: Int? = 0,
    var flag: Int? = 0,
    var name: String? = "",
    var code: String? = "",
    var isSelector: Boolean = false
)


fun getLangArr(context: Context): MutableList<Language> {
    return mutableListOf<Language>().apply {
        add(
            Language(
                0,
                R.drawable.ic_lang_en,
                context.getString(R.string.language_english),
                "en"
            )
        )
        add(
            Language(
                1,
                R.drawable.ic_flag_vn,
                context.getString(R.string.language_vietnam),
                "vi",false
            )
        )

        add(
            Language(
                1,
                R.drawable.ic_lang_hi,
                context.getString(R.string.language_hindi),
                "hi",false
            )
        )
        add(
            Language(
                2,
                R.drawable.ic_lang_sp,
                context.getString(R.string.language_spain),
                "es"
            )
        )
        add(
            Language(
                3,
                R.drawable.ic_lang_fr,
                context.getString(R.string.language_france),
                "fr"
            )
        )

        add(
            Language(
                4,
                R.drawable.ic_lang_gm,
                context.getString(R.string.language_germany),
                "de"
            )
        )
        add(
            Language(
                5,
                R.drawable.ic_lang_id,
                context.getString(R.string.language_indonesia),
                "in"
            )
        )
        add(
            Language(
                6,
                R.drawable.ic_lang_pt,
                context.getString(R.string.language_portugal),
                "pt"
            )
        )
        add(
            Language(
                7,
                R.drawable.ic_lang_zh,
                context.getString(R.string.language_china),
                "zh"
            )
        )
    }
}