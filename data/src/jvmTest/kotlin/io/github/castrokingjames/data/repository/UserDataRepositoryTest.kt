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

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.turbine.test
import io.github.castrokingjames.data.mapper.address
import io.github.castrokingjames.data.mapper.toModel
import io.github.castrokingjames.data.mapper.users
import io.github.castrokingjames.datasource.local.database.UserAddressQueries
import io.github.castrokingjames.datasource.local.database.UserResult
import io.github.castrokingjames.datasource.local.database.UserResultQueries
import io.github.castrokingjames.datasource.local.database.Users
import io.github.castrokingjames.datasource.local.database.UsersQueries
import io.github.castrokingjames.datasource.remote.response.Response
import io.github.castrokingjames.datasource.remote.response.UsersResponse
import io.github.castrokingjames.datasource.remote.service.UserService
import io.github.castrokingjames.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserDataRepositoryTest {

  private val dispatcher = UnconfinedTestDispatcher()

  private lateinit var userService: UserService
  private lateinit var usersQueries: UsersQueries
  private lateinit var userResultQueries: UserResultQueries
  private lateinit var userAddressQueries: UserAddressQueries
  private lateinit var userRepository: UserRepository

  @Before
  fun setup() {
    Dispatchers.setMain(dispatcher)
    mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    userService = mockk()
    usersQueries = mockk()
    userResultQueries = mockk()
    userAddressQueries = mockk()
    userRepository = UserDataRepository(
      userService,
      usersQueries,
      userResultQueries,
      userAddressQueries,
      dispatcher,
    )
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun testLoadUsersReturnsEmptyListThrowsException() {
    runBlocking {
      val expectedSize = 100
      coEvery { userService.getUsers(expectedSize) } returns Response.Success(
        UsersResponse(
          emptyList(),
        ),
      )
      userRepository
        .loadUsers(expectedSize)
        .test {
          val actualError = awaitError()
          assertEquals("Empty results from API", actualError.message)
        }
    }
  }

  @Test
  fun testLoadUsersReturnsErrorFromApi() {
    runBlocking {
      val expectedSize = 100
      val expectedException = Exception("Empty results from API")
      coEvery { userService.getUsers(expectedSize) } returns Response.Error(expectedException)
      userRepository
        .loadUsers(expectedSize)
        .test {
          val actualError = awaitError()
          assertEquals(expectedException.message, actualError.message)
        }
    }
  }

  @Test
  fun testLoad0UsersReturnsException() {
    runBlocking {
      val expectedSize = 0
      val expectedUsers = generateUsers(expectedSize)
      val users = generateUserResponse(expectedSize)
      val response = UsersResponse(users)
      val expectedException = Exception("Empty results from API")
      coEvery { userService.getUsers(expectedSize) } returns Response.Success(response)
      val query: Query<Users> = mockk {
        coEvery { asFlow().mapToList(dispatcher) } returns flowOf(expectedUsers)
      }
      coEvery { usersQueries.selectByResult(expectedSize.toLong()) } returns query

      users.forEachIndexed { index, user ->
        val users = user.users()
        coEvery { usersQueries.upsert(users) } returns Unit
        coEvery { userResultQueries.upsert(UserResult(index + 1L, users.id)) } returns Unit
      }

      userRepository
        .loadUsers(expectedSize)
        .test {
          val actualException = awaitError()
          assertEquals(expectedException.message, actualException.message)
        }
    }
  }

  @Test
  fun testLoad10UsersReturn10Users() {
    runBlocking {
      val expectedSize = 10
      val expectedUsers = generateUsers(expectedSize)
      val users = generateUserResponse(expectedSize)
      val response = UsersResponse(users)
      coEvery { userService.getUsers(expectedSize) } returns Response.Success(response)
      val query: Query<Users> = mockk {
        coEvery { asFlow().mapToList(dispatcher) } returns flowOf(expectedUsers)
      }
      coEvery { usersQueries.selectByResult(expectedSize.toLong()) } returns query

      users.forEachIndexed { index, user ->
        val users = user.users()
        val address = user.address()
        coEvery { usersQueries.upsert(users) } returns Unit
        coEvery { userResultQueries.upsert(UserResult(index + 1L, users.id)) } returns Unit
        coEvery { userAddressQueries.upsert(address) } returns Unit
      }

      userRepository
        .loadUsers(expectedSize)
        .test {
          val actualUsers = awaitItem()
          assertEquals(expectedSize, actualUsers.size)
          awaitComplete()
        }
    }
  }

  @Test
  fun testLoad100UsersReturn100Users() {
    runBlocking {
      val expectedSize = 100
      val expectedUsers = generateUsers(expectedSize)
      val users = generateUserResponse(expectedSize)
      val response = UsersResponse(users)
      coEvery { userService.getUsers(expectedSize) } returns Response.Success(response)
      val query: Query<Users> = mockk {
        coEvery { asFlow().mapToList(dispatcher) } returns flowOf(expectedUsers)
      }
      coEvery { usersQueries.selectByResult(expectedSize.toLong()) } returns query

      users.forEachIndexed { index, user ->
        val users = user.users()
        val address = user.address()
        coEvery { usersQueries.upsert(users) } returns Unit
        coEvery { userResultQueries.upsert(UserResult(index + 1L, users.id)) } returns Unit
        coEvery { userAddressQueries.upsert(address) } returns Unit
      }

      userRepository
        .loadUsers(expectedSize)
        .test {
          val actualUsers = awaitItem()
          assertEquals(expectedSize, actualUsers.size)
          awaitComplete()
        }
    }
  }

  @Test
  fun testLoadUserWithValidUserIdReturnsUser() {
    runBlocking {
      val expectedUserId = 12
      val users = generateUsers(expectedUserId)
      val expectedUser = users[expectedUserId - 1]
      val query: Query<Users> = mockk {
        coEvery { asFlow().mapToOneOrNull(dispatcher) } returns flowOf(expectedUser)
      }
      coEvery { usersQueries.selectByUserId("$expectedUserId") } returns query
      userRepository
        .loadUserById("$expectedUserId")
        .test {
          val actualUser = awaitItem()
          val expectedUser = expectedUser.toModel()
          assertEquals(actualUser, expectedUser)
          awaitComplete()
        }
    }
  }

  @Test
  fun testLoadUserWithInvalidUserIdThrowsException() {
    runBlocking {
      val expectedUserId = 12
      val query: Query<Users> = mockk {
        coEvery { asFlow().mapToOneOrNull(dispatcher) } returns flowOf(null)
      }
      coEvery { usersQueries.selectByUserId("$expectedUserId") } returns query
      userRepository
        .loadUserById("$expectedUserId")
        .test {
          val actualException = awaitError()
          assertEquals("Can't find user with user id $expectedUserId", actualException.message)
        }
    }
  }
}
