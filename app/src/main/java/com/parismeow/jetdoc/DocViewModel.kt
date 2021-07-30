package com.parismeow.jetdoc

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.parismeow.jetdoc.data.UploadAPI

class DocViewModel : ViewModel() {


    var docItem by mutableStateOf<Uri>(Uri.EMPTY)
        private set

    fun onDocItemChange(docUri: Uri) {
        docItem = docUri
    }

    val uploadAPI = UploadAPI()

    fun onDocUpload() {

    }
    /*val fileBody = RequestBody.create(MediaType.parse(file.type!!), file!!)
        val body = MultipartBody.Builder().addFormDataPart("name", file.name!!)
            .addFormDataPart("sampleFile", file.name!!, fileBody).build()*/


}
