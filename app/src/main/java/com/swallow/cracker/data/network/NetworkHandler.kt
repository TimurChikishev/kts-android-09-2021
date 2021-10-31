package com.swallow.cracker.data.network

import com.swallow.cracker.data.api.RedditApi
import com.swallow.cracker.data.model.Resources
import retrofit2.HttpException
import retrofit2.Response

object NetworkHandler {
    suspend fun <T, Model> call(
        api: suspend RedditApi.() -> Response<T>,
        mapper: T.() -> Model
    ): Resources<Model> {
        return try {
            val response = api(Networking.redditApiOAuth)
            val model = response.body()?.mapper()
            if (response.isSuccessful) {
                if (model != null) {
                    Resources.Success(model)
                } else {
                    Resources.Error()
                }
            } else {
                Resources.Error(throwable = HttpException(response))
            }
        } catch (e: Throwable) {
            Resources.Error(throwable = e)
        }
    }
}
