package dong.datn.tourify.app

import android.os.Build

fun isApi30orHigher(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
}
fun isApi30to33(): Boolean {
    return Build.VERSION.SDK_INT in Build.VERSION_CODES.R..Build.VERSION_CODES.TIRAMISU
}
fun isApiFrom23to29(): Boolean {
    return Build.VERSION.SDK_INT in Build.VERSION_CODES.M..Build.VERSION_CODES.Q
}
fun isApi23orLower():Boolean{
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
}
fun isApi33orHigher():Boolean{
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

}