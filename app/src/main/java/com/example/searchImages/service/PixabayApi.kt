package com.example.searchImages.service

import com.example.searchImages.App
import com.example.searchImages.BuildConfig
import com.example.searchImages.model.ResponseHitsModel
import com.google.gson.Gson
import com.neil.network.NetworkClient
import com.neil.network.retryFactory.Retry
import com.orhanobut.logger.Logger
import retrofit2.Response
import retrofit2.http.*
import java.lang.Exception

object PixabayApi {

    object Api {
        val retrofit: ApiService by lazy {
            NetworkClient.getInstance().also {
                it.setCookie(App.instance)
                it.setLoggingInterceptor(BuildConfig.DEBUG)
            }.create(ApiService::class.java)
        }
    }

    interface ApiService {

        @GET("https://pixabay.com/api/")
        @Retry(2)
        suspend fun getSearchImages(
            @QueryMap paras: Map<String, String>
        ): Response<ResponseHitsModel>
    }

    suspend fun getSearchImages(
        query: String? = null,
        page: Int,
        count: Int,
    ): ResponseHitsModel {
        val map = mutableMapOf("key" to "27703140-f4964ea518947d2ddc2783638")
        query?.apply {
            map["q"] = query
        }
        map["safesearch"] = "true"
        map["orientation"] = "horizontal"
        map["page"] = page.toString()
        map["per_page"] = count.toString()

        val response = Api.retrofit.getSearchImages(map)
        if (response.isSuccessful) {
            val responseBody = response.body()
            Logger.json(Gson().toJson(responseBody))
            return responseBody!!
        } else {
            throw Exception(response.message())
        }
    }

}