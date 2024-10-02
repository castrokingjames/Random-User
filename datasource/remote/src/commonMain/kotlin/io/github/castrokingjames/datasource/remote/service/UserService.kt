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

import io.github.castrokingjames.API_USERS
import io.github.castrokingjames.USER_RESULTS
import io.github.castrokingjames.datasource.remote.get
import io.github.castrokingjames.datasource.remote.response.Response
import io.github.castrokingjames.datasource.remote.response.UsersResponse
import io.ktor.client.HttpClient

class UserService constructor(
  private val httpClient: HttpClient,
) {

  suspend fun getUsers(size: Int): Response<UsersResponse> {
    return httpClient
      .get<UsersResponse>(
        API_USERS,
        mapOf(
          USER_RESULTS to size,
        ),
      )
  }
}
