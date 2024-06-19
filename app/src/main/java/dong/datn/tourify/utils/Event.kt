package dong.duan.livechat.utility

import android.os.Handler
import android.os.Looper

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