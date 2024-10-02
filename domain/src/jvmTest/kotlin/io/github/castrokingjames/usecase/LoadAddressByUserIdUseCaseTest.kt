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
package io.github.castrokingjames.usecase

import app.cash.turbine.test
import io.github.castrokingjames.repository.AddressRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoadAddressByUserIdUseCaseTest {

  private lateinit var addressRepository: AddressRepository
  private lateinit var loadAddressByUserId: LoadAddressByUserIdCase

  @Before
  fun setup() {
    addressRepository = mockk()
    loadAddressByUserId = LoadAddressByUserIdCase(addressRepository)
  }

  @Test
  fun testLoadAddressWithInvalidUserIdThrowsException() {
    runBlocking {
      val userId = "123"
      val expectedException = Exception("Can't find address with user id 123")
      coEvery { addressRepository.loadUserById(userId) } returns flow { throw expectedException }
      loadAddressByUserId(userId)
        .test {
          val actualException = awaitError()
          assertEquals(expectedException.message, actualException.message)
        }
    }
  }

  @Test
  fun testLoadAddressWithUserId123ReturnsAddress() {
    runBlocking {
      val userId = "123"
      val expectedAddress = generateAddress()
      coEvery { addressRepository.loadUserById(userId) } returns flowOf(expectedAddress)
      loadAddressByUserId(userId)
        .test {
          val actualAddress = awaitItem()
          assertEquals(expectedAddress, actualAddress)
          awaitComplete()
        }
    }
  }
}
