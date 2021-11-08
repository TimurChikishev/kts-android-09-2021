package com.swallow.cracker.data.network

import com.swallow.cracker.data.model.Resources
import retrofit2.HttpException
import retrofit2.Response

class NetworkHandler  {
    companion object{
        suspend fun <T, API, Model> call(
            api: API,
            apiMethod: suspend API.() -> Response<T>,
            mapper: T.() -> Model
        ): Resources<Model> {
            return try {
                val response = apiMethod(api)
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
}
