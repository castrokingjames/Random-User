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
import io.github.castrokingjames.model.User
import io.github.castrokingjames.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoadUserByUserIdUseCaseTest {

  private lateinit var userRepository: UserRepository
  private lateinit var loadUserByUserId: LoadUserByUserIdCase
  private lateinit var users: List<User>

  @Before
  fun setup() {
    userRepository = mockk()
    loadUserByUserId = LoadUserByUserIdCase(userRepository)
    users = generateUsers(100)
  }

  @Test
  fun testLoadUserWithInvalidUserIdThrowsException() {
    runBlocking {
      val userId = "123"
      val expectedException = Exception("Can't find user with id 123")
      coEvery { userRepository.loadUserById(userId) } returns flow { throw expectedException }
      loadUserByUserId(userId)
        .test {
          val actualException = awaitError()
          assertEquals(expectedException.message, actualException.message)
        }
    }
  }

  @Test
  fun testLoadUserId12ReturnsUser12() {
    runBlocking {
      val expectedUserId = 12
      coEvery { userRepository.loadUserById("$expectedUserId") } returns flowOf(users[expectedUserId])
      loadUserByUserId("$expectedUserId")
        .test {
          val actualUser = awaitItem()
          assertEquals("$expectedUserId", actualUser.id)
          awaitComplete()
        }
    }
  }

  @Test
  fun testLoadUserId99ReturnsUser99() {
    runBlocking {
      val expectedUserId = 99
      coEvery { userRepository.loadUserById("$expectedUserId") } returns flowOf(users[expectedUserId])
      loadUserByUserId("$expectedUserId")
        .test {
          val actualUser = awaitItem()
          assertEquals("$expectedUserId", actualUser.id)
          awaitComplete()
        }
    }
  }
}
