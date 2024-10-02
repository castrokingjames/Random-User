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
@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.castrokingjames.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import io.github.castrokingjames.data.mapper.toModel
import io.github.castrokingjames.datasource.local.database.UserAddressQueries
import io.github.castrokingjames.model.Address
import io.github.castrokingjames.repository.AddressRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class AddressDataRepository constructor(
  private val userAddressQueries: UserAddressQueries,
  private val io: CoroutineDispatcher,
) : AddressRepository {

  override suspend fun loadUserById(userId: String): Flow<Address> {
    return channelFlow {
      userAddressQueries
        .selectByUserId(userId)
        .asFlow()
        .mapToOneOrNull(io)
        .collectLatest { query ->
          if (query == null) {
            throw Exception("Can't find address with user id $userId")
          } else {
            val model = query.toModel()
            send(model)
          }
        }
    }
  }
}
