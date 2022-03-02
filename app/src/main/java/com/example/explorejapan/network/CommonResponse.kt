package com.example.explorejapan.network

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class CommonResponse {
    companion object {

        fun <T : Any> getErrorResponse(errorCode: Int = 500, message: String?): Response<T> =
            Response.error(
                errorCode, (message
                    ?: "something went wrong").toResponseBody()
            )
    }
}