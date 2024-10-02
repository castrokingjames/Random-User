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
import io.github.castrokingjames.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoadUsersBySizeUseCaseTest {

  private lateinit var userRepository: UserRepository
  private lateinit var loadUsersBySize: LoadUsersBySizeUseCase

  @Before
  fun setup() {
    userRepository = mockk()
    loadUsersBySize = LoadUsersBySizeUseCase(userRepository)
  }

  @Test
  fun testLoadInvalidUserSize() {
    runBlocking {
      val size = 0
      val expectedException = Exception("Invalid size")
      coEvery { userRepository.loadUsers(size) } returns flow { throw expectedException }
      loadUsersBySize(size)
        .test {
          val actualException = awaitError()
          assertEquals(expectedException.message, actualException.message)
        }
    }
  }

  @Test
  fun testLoadUsersReturnsErrorFromApi() {
    runBlocking {
      val size = 100
      val expectedException = Exception("Empty results from API")
      coEvery { userRepository.loadUsers(size) } returns flow { throw expectedException }
      loadUsersBySize(size)
        .test {
          val actualException = awaitError()
          assertEquals(expectedException.message, actualException.message)
        }
    }
  }

  @Test
  fun testLoad100UsersReturnsEmpty() {
    runBlocking {
      val expectedSize = 100
      val users = generateUsers(0)
      coEvery { userRepository.loadUsers(expectedSize) } returns flowOf(users)
      loadUsersBySize(expectedSize)
        .test {
          val actualUsers = awaitItem()
          assertTrue(actualUsers.isEmpty())
          awaitComplete()
        }
    }
  }

  @Test
  fun testLoad10UsersReturns10Users() {
    runBlocking {
      val expectedSize = 10
      val users = generateUsers(expectedSize)
      coEvery { userRepository.loadUsers(expectedSize) } returns flowOf(users)
      loadUsersBySize(expectedSize)
        .test {
          val actualUsers = awaitItem()
          assertEquals(expectedSize, actualUsers.size)
          awaitComplete()
        }
    }
  }

  @Test
  fun testLoad100UsersReturns100Users() {
    runBlocking {
      val users = generateUsers(100)
      coEvery { userRepository.loadUsers(100) } returns flowOf(users)
      loadUsersBySize(100)
        .test {
          val results = awaitItem()
          assertEquals(results.size, 100)
          awaitComplete()
        }
    }
  }
}
