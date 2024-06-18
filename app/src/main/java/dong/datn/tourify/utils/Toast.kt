package dong.duan.ecommerce.library

import android.view.Gravity
import android.widget.Toast
import dong.datn.tourify.app.appContext


fun showToast(mess: Any) {
    Toast.makeText(appContext, mess.toString(), Toast.LENGTH_LONG).show()
}

fun showToast(mess: Any, time: Int) {
    Toast.makeText(appContext, mess.toString(), time).show()
}

fun showToast(mess: Any, location: ToastLocation = ToastLocation.DEFAULT) {
    val toast = Toast.makeText(appContext, mess.toString(), Toast.LENGTH_LONG)
    when (location) {
        ToastLocation.DEFAULT -> toast.setGravity(Gravity.BOTTOM, 0, 0)
        ToastLocation.TOP -> {
            toast.setGravity(Gravity.TOP, 0, 0)

        }

        ToastLocation.TOP_LEFT -> toast.setGravity(Gravity.TOP and Gravity.LEFT, 0, 0)
        ToastLocation.TOP_RIGHT -> toast.setGravity(Gravity.TOP and Gravity.RIGHT, 0, 0)
        ToastLocation.BOTOM_LEFT -> toast.setGravity(Gravity.BOTTOM and Gravity.LEFT, 0, 0)
        ToastLocation.RIGHT -> toast.setGravity(Gravity.RIGHT, 0, 0)
        ToastLocation.LEFT -> toast.setGravity(Gravity.LEFT, 0, 0)
        ToastLocation.CENTER -> toast.setGravity(Gravity.CENTER, 0, 0)
        else -> {
            toast.setGravity(Gravity.BOTTOM, 0, 0)
        }
    }
    toast.show()
}

enum class ToastLocation {
    TOP,
    TOP_LEFT,
    TOP_RIGHT,
    DEFAULT,
    CENTER,
    LEFT,
    RIGHT,
    BOTOM_LEFT,
    BOTTOM_RIGHT
}