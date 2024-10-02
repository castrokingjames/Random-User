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
package io.github.castrokingjames.datasource.remote.service

import io.github.castrokingjames.datasource.remote.RESPONSE_1_USER
import io.github.castrokingjames.datasource.remote.RESPONSE_EMPTY_USER
import io.github.castrokingjames.datasource.remote.isError
import io.github.castrokingjames.datasource.remote.isSuccess
import io.github.castrokingjames.datasource.remote.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertTrue
import org.junit.Test

class UserServiceTest {

  private val contentType = "application/json"

  @Test
  fun testGetUserSuccess() {
    runBlocking {
      val expectedUserSize = 0
      val expectedContent = RESPONSE_EMPTY_USER
      val service = createService(expectedContent, HttpStatusCode.OK)
      val result = service.getUsers(expectedUserSize)
      assertTrue(result.isSuccess)
    }
  }

  @Test
  fun testGetUserError() {
    runBlocking {
      val expectedUserSize = 0
      val expectedContent = RESPONSE_EMPTY_USER
      val service = createService(expectedContent, HttpStatusCode.Forbidden)
      val result = service.getUsers(expectedUserSize)
      assertTrue(result.isError)
    }
  }

  @Test
  fun testGetOneUserReturnsOneUser() {
    runBlocking {
      val expectedUserSize = 1
      val expectedContent = RESPONSE_1_USER
      val service = createService(expectedContent, HttpStatusCode.OK)
      val result = service.getUsers(expectedUserSize)
      result.onSuccess { users ->
        val actualUserSize = users.results.size
        assertEquals(expectedUserSize, actualUserSize)
      }
    }
  }

  @Test
  fun testGetUsersReturnsEmpty() {
    runBlocking {
      val expectedUserSize = 1
      val expectedContent = RESPONSE_EMPTY_USER
      val service = createService(expectedContent, HttpStatusCode.OK)
      val result = service.getUsers(expectedUserSize)
      result.onSuccess { users ->
        assertTrue(users.results.isEmpty())
      }
    }
  }

  private fun createService(content: String, status: HttpStatusCode): UserService {
    val engine = MockEngine { _ ->
      respond(
        content = content,
        status = status,
        headers = headersOf(HttpHeaders.ContentType, contentType),
      )
    }
    val client = HttpClient(engine) {
      install(ContentNegotiation) {
        json(
          Json {
            ignoreUnknownKeys = true
          },
        )
      }
    }
    return UserService(client)
  }
}
