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
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.turbine.test
import io.github.castrokingjames.data.mapper.toModel
import io.github.castrokingjames.datasource.local.database.UserAddress
import io.github.castrokingjames.datasource.local.database.UserAddressQueries
import io.github.castrokingjames.repository.AddressRepository
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

class AddressDataRepositoryTest {

  private val dispatcher = UnconfinedTestDispatcher()

  private lateinit var userAddressQueries: UserAddressQueries
  private lateinit var addressRepository: AddressRepository

  @Before
  fun setup() {
    Dispatchers.setMain(dispatcher)
    mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    userAddressQueries = mockk()
    addressRepository = AddressDataRepository(userAddressQueries, dispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun testLoadAddressWithValidUserIdReturnsValidAddress() {
    runBlocking {
      val expectedUserId = 123
      val expectedUserAddress = generateUserAddress()
      val query: Query<UserAddress> = mockk {
        coEvery { asFlow().mapToOneOrNull(dispatcher) } returns flowOf(expectedUserAddress)
      }
      coEvery { userAddressQueries.selectByUserId("$expectedUserId") } returns query
      addressRepository
        .loadUserById("$expectedUserId")
        .test {
          val actualAddress = awaitItem()
          val expectedAddress = expectedUserAddress.toModel()
          assertEquals(expectedAddress, actualAddress)
          awaitComplete()
        }
    }
  }

  @Test
  fun testLoadAddressWithInValidUserIdThrowsException() {
    runBlocking {
      val expectedUserId = 123
      val query: Query<UserAddress> = mockk {
        coEvery { asFlow().mapToOneOrNull(dispatcher) } returns flowOf(null)
      }
      coEvery { userAddressQueries.selectByUserId("$expectedUserId") } returns query
      addressRepository
        .loadUserById("$expectedUserId")
        .test {
          val actualException = awaitError()
          assertEquals("Can't find address with user id $expectedUserId", actualException.message)
        }
    }
  }
}
