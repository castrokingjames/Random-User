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
package io.github.castrokingjames.datasource.remote.response

import io.github.castrokingjames.LOCATION_CITY
import io.github.castrokingjames.LOCATION_COUNTRY
import io.github.castrokingjames.LOCATION_POSTCODE
import io.github.castrokingjames.LOCATION_STATE
import io.github.castrokingjames.LOCATION_STREET
import io.github.castrokingjames.LOCATION_STREET_NAME
import io.github.castrokingjames.LOCATION_STREET_NUMBER
import io.github.castrokingjames.USER_BIRTH_DATE
import io.github.castrokingjames.USER_DATE_OF_BIRTH
import io.github.castrokingjames.USER_EMAIL
import io.github.castrokingjames.USER_FIRST_NAME
import io.github.castrokingjames.USER_GENDER
import io.github.castrokingjames.USER_LAST_NAME
import io.github.castrokingjames.USER_LOCATION
import io.github.castrokingjames.USER_NAME
import io.github.castrokingjames.USER_NATIONALITY
import io.github.castrokingjames.USER_PICTURE
import io.github.castrokingjames.USER_PICTURE_LARGE
import io.github.castrokingjames.USER_TITLE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
  @SerialName(USER_GENDER)
  val gender: String,
  @SerialName(USER_NAME)
  val name: NameResponse,
  @SerialName(USER_LOCATION)
  val location: LocationResponse,
  @SerialName(USER_EMAIL)
  val email: String,
  @SerialName(USER_DATE_OF_BIRTH)
  val dateOfBirth: DateOfBirthResponse,
  @SerialName(USER_PICTURE)
  val picture: PictureResponse,
  @SerialName(USER_NATIONALITY)
  val nationality: String,
) {

  @Serializable
  data class NameResponse(
    @SerialName(USER_TITLE)
    val title: String,
    @SerialName(USER_FIRST_NAME)
    val first: String,
    @SerialName(USER_LAST_NAME)
    val last: String,
  )

  @Serializable
  data class LocationResponse(
    @SerialName(LOCATION_STREET)
    val street: StreetResponse,
    @SerialName(LOCATION_CITY)
    val city: String,
    @SerialName(LOCATION_STATE)
    val state: String,
    @SerialName(LOCATION_COUNTRY)
    val country: String,
    @SerialName(LOCATION_POSTCODE)
    val postcode: String,
  ) {

    @Serializable
    data class StreetResponse(
      @SerialName(LOCATION_STREET_NUMBER)
      val number: Int,
      @SerialName(LOCATION_STREET_NAME)
      val name: String,
    )
  }

  @Serializable
  data class DateOfBirthResponse(
    @SerialName(USER_BIRTH_DATE)
    val date: String,
  )

  @Serializable
  data class PictureResponse(
    @SerialName(USER_PICTURE_LARGE)
    val large: String,
  )
}
