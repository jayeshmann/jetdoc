package com.parismeow.jetdoc

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import com.parismeow.jetdoc.data.UploadAPI
import com.parismeow.jetdoc.data.UploadRequestBody
import com.parismeow.jetdoc.data.UploadResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DocViewModel : ViewModel() {


    var docItem by mutableStateOf<Uri>(Uri.EMPTY)
        private set

    fun onDocItemChange(docUri: Uri) {
        docItem = docUri
    }


    fun onDocUpload(context: Context) {
        val fileName = DocumentFile.fromSingleUri(context, docItem)?.name!!
        val parcelFileDescriptor =
            context.contentResolver.openFileDescriptor(docItem, "r", null) ?: return
        val file = File(docItem.path!!)
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "application") {}

        UploadAPI().uploadDoc(
            MultipartBody.Part.createFormData("sampleFile", fileName, body),
            RequestBody.create(MediaType.parse("multipart/form-data"), "PDF doc")
        ).enqueue(object :
            Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                println(t.message)
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                println(response.body()?.message.toString())
            }
        })
    }

}
