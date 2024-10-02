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
package io.github.castrokingjames.data.mapper

import io.github.castrokingjames.datasource.local.database.UserAddress
import io.github.castrokingjames.datasource.local.database.Users
import io.github.castrokingjames.datasource.remote.response.UserResponse
import io.github.castrokingjames.model.Address
import io.github.castrokingjames.model.User
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun UserAddress.toModel(): Address {
  return Address(
    street,
    city,
    state,
    country,
    postcode,
  )
}

fun UserResponse.users(): Users {
  val id = "${name.first}-${name.last}".lowercase()
  val title = name.title
  val firstName = name.first
  val lastName = name.last
  val gender = gender
  val email = email
  val thumbnail = picture.large
  val nationality = nationality
  val birthday = OffsetDateTime
    .parse(dateOfBirth.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    .toEpochSecond()
  return Users(
    id,
    title,
    firstName,
    lastName,
    gender,
    email,
    thumbnail,
    nationality,
    birthday,
  )
}

fun UserResponse.address(): UserAddress {
  val userId = "${name.first}-${name.last}".lowercase()
  val street = "${location.street.number} ${location.street.name}"
  val city = location.city
  val state = location.state
  val country = location.country
  val postcode = location.postcode
  return UserAddress(
    userId,
    street,
    city,
    state,
    country,
    postcode,
  )
}

fun Users.toModel(): User {
  return User(
    id,
    title,
    firstName,
    lastName,
    gender,
    email,
    thumbnail,
    nationality,
    birthday,
  )
}
