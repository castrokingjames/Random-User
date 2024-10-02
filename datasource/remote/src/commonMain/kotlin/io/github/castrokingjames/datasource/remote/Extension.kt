/*
 * Copyright 2024, King James Castro and project contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.castrokingjames.datasource.remote

import io.github.castrokingjames.datasource.remote.exception.HttpException
import io.github.castrokingjames.datasource.remote.response.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText

suspend inline fun <reified T> HttpClient.get(
  url: String,
  parameters: Map<String, Any> = emptyMap(),
): Response<T> {
  return try {
    val response = get(url) {
      parameters.forEach { entry ->
        val key = entry.key
        val value = entry.value
        parameter(key, value)
      }
    }
    val code = response.status.value
    if (code > 299) {
      val body = response.bodyAsText()
      val exception = HttpException(code, body)
      return Response.Error(exception)
    }
    val body: T = response.body()
    Response.Success(body)
  } catch (e: Exception) {
    Response.Error(e)
  }
}

inline val <T>Response<T>.isSuccess: Boolean
  get() = this is Response.Success

inline val <T>Response<T>.isError: Boolean
  get() = this is Response.Error

inline fun <T> Response<T>.onSuccess(onSuccess: (T) -> Unit): Response<T> {
  if (this is Response.Success) {
    onSuccess(data)
  }
  return this
}

inline fun <T> Response<T>.onError(onError: (Throwable) -> Unit): Response<T> {
  if (this is Response.Error) {
    onError(error)
  }
  return this
}
