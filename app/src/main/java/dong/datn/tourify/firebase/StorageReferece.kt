package dong.datn.tourify.firebase

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

fun StorageReference.putImgToStorage(
    context: Context,
    uri: Uri,
    callback: (String?) -> Unit
) {
    val fileRef =
        this.child(System.currentTimeMillis().toString() + "." + fileExtension(uri, context))

    val uploadTask = fileRef.putFile(uri)
    uploadTask.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
        fileRef.downloadUrl.addOnSuccessListener { uri1: Uri ->
            callback(uri1.toString())
        }
            .addOnFailureListener { e: Exception ->
                Log.d("TAG", "putImgToStorage: ${e.message}")
            }
    }.addOnFailureListener { e: Exception ->
        Log.d("TAG", "putImgToStorage: ${e.message}")
    }
}

private fun fileExtension(uri: Uri, context: Context): String? {
    val cr: ContentResolver = context.getContentResolver()
    val mime = MimeTypeMap.getSingleton()
    return mime.getExtensionFromMimeType(cr.getType(uri))
}
