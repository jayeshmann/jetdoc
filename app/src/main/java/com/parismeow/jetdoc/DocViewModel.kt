package com.parismeow.jetdoc

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DocViewModel : ViewModel() {


    var docItem by mutableStateOf<Uri>(Uri.EMPTY)
        private set

    var snackbarMsg by mutableStateOf<String>("")

    fun onDocItemChange(docUri: Uri) {
        docItem = docUri
    }

    fun onDocDone() {
        docItem = Uri.EMPTY
    }

    fun hideSnackbar(){
        snackbarMsg = ""
    }

    fun onDocUpload(
        context: Context,
        fileName: String,
        onProgress: (Int) -> Unit
    ) {
        val docRef = Firebase.storage.reference.child("uploads/$fileName")
        docRef.putFile(docItem)
            .addOnSuccessListener {
                println("File Uploaded")
                snackbarMsg = "File Uploaded"
                onDocDone()
            }
            .addOnFailureListener {
                println("File upload failed")
                snackbarMsg = "File upload failed"
                onDocDone()

            }
            .addOnProgressListener {
                val progress = ((100 * it.bytesTransferred) / it.totalByteCount).toInt()
                println("Uploading...$progress%")
                onProgress(progress)
            }
    }

}
