package com.parismeow.jetdoc.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

const val BASE_URL = "http://192.168.1.41:3001"

interface UploadAPI {

    @Multipart
    @POST("upload")
    fun uploadDoc(
        @Part sampleFile: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>

    companion object {
        operator fun invoke(): UploadAPI {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            return Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL).build().create(UploadAPI::class.java)
        }
    }
}
