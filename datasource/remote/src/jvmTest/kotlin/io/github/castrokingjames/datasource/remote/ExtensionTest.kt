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
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExtensionTest {

  private val url = "https://example.com"
  private val contentType = "application/json"

  @Test
  fun testResponseIsSameWithContent() {
    runBlocking {
      val expectedContent = "Success"
      val client = createClient(expectedContent, HttpStatusCode.OK)
      val result = client.get<String>(url)
      val actualContent = (result as Response.Success).data
      assertEquals(expectedContent, actualContent)
    }
  }

  @Test
  fun testResponseIsSuccess() {
    runBlocking {
      val expectedContent = "Success"
      val client = createClient(expectedContent, HttpStatusCode.OK)
      val result = client.get<String>(url)
      assertTrue(result is Response.Success)
    }
  }

  @Test
  fun testResponseIsError() {
    runBlocking {
      val expectedContent = "Error"
      val client = createClient(expectedContent, HttpStatusCode.Forbidden)
      val result = client.get<String>(url)
      assertTrue(result is Response.Error)
    }
  }

  @Test
  fun testResultIsSuccess() {
    runBlocking {
      val expectedContent = "Success"
      val client = createClient(expectedContent, HttpStatusCode.OK)
      val result = client.get<String>(url)
      assertTrue(result.isSuccess)
    }
  }

  @Test
  fun testResultOnSuccess() {
    runBlocking {
      val expectedContent = "Success"
      val client = createClient(expectedContent, HttpStatusCode.OK)
      val result = client.get<String>(url)
      result.onSuccess { response ->
        assertEquals(expectedContent, response)
      }
    }
  }

  @Test
  fun testResultIsError() {
    runBlocking {
      val expectedContent = "Error"
      val client = createClient(expectedContent, HttpStatusCode.Forbidden)
      val result = client.get<String>(url)
      assertTrue(result.isError)
    }
  }

  @Test
  fun testResultOnError() {
    runBlocking {
      val expectedContent = "Error"
      val client = createClient(expectedContent, HttpStatusCode.Forbidden)
      val result = client.get<String>(url)
      val expectedException = HttpException(HttpStatusCode.Forbidden.value, "Error")
      result.onError { error ->
        assertEquals(expectedException, error)
      }
    }
  }

  private fun createClient(content: String, status: HttpStatusCode): HttpClient {
    val engine = MockEngine { _ ->
      respond(
        content = content,
        status = status,
        headers = headersOf(HttpHeaders.ContentType, contentType),
      )
    }
    return HttpClient(engine)
  }
}
