package dong.duan.livechat.utility

import android.os.Handler
import android.os.Looper
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random

open class Event<out T>(val content:T) {
    var hasbeenHandeler= false
    fun getContentOrNull(): T?{
        return  if(hasbeenHandeler) null
        else{
            hasbeenHandeler=true
            content
        }
    }

}

fun delayTime(time: Long=1500L, function: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        function()
    }, time)
}
fun generateNumericOTP(length: Int = 4): String {
    val otp = StringBuilder()
    repeat(length) {
        val randomDigit = Random.nextInt(0, 10)
        otp.append(randomDigit)
    }
    return otp.toString()
}

fun Double.formats(round: Int=1): Double {
    val scale = 10.0.pow(round)
    return round(this * scale) / scale
}

fun Float.formats(round: Int=1): Double {
    val scale = 10.0.pow(round)
    return round(this * scale) / scale
}