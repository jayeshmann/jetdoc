package com.parismeow.jetdoc

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage

class DocViewModel : ViewModel() {


    var docItem by mutableStateOf<Uri>(Uri.EMPTY)
        private set

    fun onDocItemChange(docUri: Uri) {
        docItem = docUri
    }

    fun onDocDone() {
        docItem = Uri.EMPTY
    }

    fun onDocUpload(context: Context, fileName: String) {
        val docRef = FirebaseStorage.getInstance().reference.child("uploads/$fileName")
        docRef.putFile(docItem)
            .addOnSuccessListener {
                println("File Uploaded")
                onDocDone()
            }
            .addOnFailureListener {
                println("File upload failed")

            }
            .addOnProgressListener {
                val progress = ((100 * it.bytesTransferred) / it.totalByteCount).toInt()
                println("Uploading...$progress%")
            }

    }

}
