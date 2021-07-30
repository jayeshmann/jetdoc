package com.parismeow.jetdoc.data

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

const val BASE_URL = "https://192.168.1.46:3001"

interface UploadAPI {

    @Multipart
    @POST("upload")
    fun uploadDoc(@Part sampleFile: MultipartBody.Part): Call<UploadResponse>

    companion object{
        operator fun invoke(): UploadAPI{
            return Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create()).baseUrl(BASE_URL).build().create(UploadAPI::class.java)
        }
    }
}
