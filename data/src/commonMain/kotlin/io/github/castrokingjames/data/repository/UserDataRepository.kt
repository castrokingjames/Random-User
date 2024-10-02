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
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import io.github.castrokingjames.data.mapper.address
import io.github.castrokingjames.data.mapper.toModel
import io.github.castrokingjames.data.mapper.users
import io.github.castrokingjames.datasource.local.database.UserAddressQueries
import io.github.castrokingjames.datasource.local.database.UserResult
import io.github.castrokingjames.datasource.local.database.UserResultQueries
import io.github.castrokingjames.datasource.local.database.UsersQueries
import io.github.castrokingjames.datasource.remote.onError
import io.github.castrokingjames.datasource.remote.onSuccess
import io.github.castrokingjames.datasource.remote.service.UserService
import io.github.castrokingjames.model.User
import io.github.castrokingjames.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

class UserDataRepository constructor(
  private val userService: UserService,
  private val usersQueries: UsersQueries,
  private val userResultQueries: UserResultQueries,
  private val userAddressQueries: UserAddressQueries,
  private val io: CoroutineDispatcher,
) : UserRepository {

  override suspend fun loadUserById(userId: String): Flow<User> {
    return channelFlow {
      usersQueries.selectByUserId(userId).asFlow().mapToOneOrNull(io).collectLatest { query ->
        if (query == null) {
          throw Exception("Can't find user with user id $userId")
        } else {
          val model = query.toModel()
          send(model)
        }
      }
    }
  }

  override suspend fun loadUsers(size: Int): Flow<List<User>> {
    return channelFlow {
      userService.getUsers(size).onSuccess { response ->
        val users = response.results
        val size = users.size
        if (size == 0) {
          throw Exception("Empty results from API")
        }

        users.map { user ->
          Pair(
            user.users(),
            user.address(),
          )
        }.forEachIndexed { index, pair ->
          val user = pair.first
          val address = pair.second
          usersQueries.upsert(user)

          val userResult = UserResult(index + 1L, user.id)
          userResultQueries.upsert(userResult)
          userAddressQueries.upsert(address)
        }

        usersQueries.selectByResult(size.toLong()).asFlow().mapToList(io).map { users ->
          users.map { user ->
            user.toModel()
          }
        }.collectLatest { users ->
          send(users)
        }
      }.onError { exception ->
        throw exception
      }
    }
  }
}
