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
package io.github.castrokingjames.data.repository

import io.github.castrokingjames.datasource.local.database.UserAddress
import io.github.castrokingjames.datasource.local.database.Users
import io.github.castrokingjames.datasource.remote.response.UserResponse

fun generateUserAddress(): UserAddress {
  return UserAddress(
    "1234",
    "1234",
    "City",
    "State",
    "Country",
    "1234",
  )
}

fun generateUsers(size: Int): List<Users> {
  return Array(size) { index ->
    Users(
      "${index + 1}",
      "Mr",
      "John",
      "Doe",
      "Male",
      "john.doe@gmail.com",
      "www.google.com/john.doe.jpg",
      "US",
      12345678L,
    )
  }
    .toList()
}

fun generateUserResponse(size: Int): List<UserResponse> {
  return Array(size) { _ ->
    UserResponse(
      "Male",
      UserResponse.NameResponse("Mr", "John", "Doe"),
      UserResponse.LocationResponse(
        UserResponse.LocationResponse.StreetResponse(123, "ABC"),
        "City",
        "State",
        "Country",
        "1234",
      ),
      "john.doe@email.com",
      UserResponse.DateOfBirthResponse("1992-07-10T08:32:40.120Z"),
      UserResponse.PictureResponse("https://randomuser.me/api/portraits/men/7.jpg"),
      "DE",
    )
  }
    .toList()
}
