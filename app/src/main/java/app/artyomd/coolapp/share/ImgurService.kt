package app.artyomd.coolapp.share


import okhttp3.MultipartBody

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Created by maxim on 21/02/2017.
 */

internal interface ImgurService {
    @Multipart
    @Headers("Authorization: Client-ID 0b5e46b0ac7b39f")
    @POST("image")
    fun postImage(
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("album") albumId: String,
        @Query("account_url") username: String,
        @Part file: MultipartBody.Part
    ): Call<ImageResponse>

    companion object {


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
