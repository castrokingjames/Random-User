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

package io.github.castrokingjames.feature.user.list.user

import app.cash.turbine.test
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.castrokingjames.feature.user.list.generateAddress
import io.github.castrokingjames.feature.user.list.generateUsers
import io.github.castrokingjames.model.Address
import io.github.castrokingjames.model.User
import io.github.castrokingjames.usecase.LoadAddressByUserIdCase
import io.github.castrokingjames.usecase.LoadUserByUserIdUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.coroutines.CoroutineContext
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserComponentTest {

  private val lifecycle = LifecycleRegistry()
  private val defaultComponentContext = DefaultComponentContext(lifecycle)
  private val dispatcher = UnconfinedTestDispatcher()
  private val user = generateUsers(1)[0]
  private val address = generateAddress()

  @Before
  fun setup() {
    Dispatchers.setMain(dispatcher)
    lifecycle.resume()
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun testUiStateIsLoadingWhenInitialized() {
    runBlocking {
      val userId = "1"
      val component = createComponent(userId)
      assertEquals(UserComponent.UserUiState.Loading, component.userUiState.value)
    }
  }

  @Test
  fun testUiStateIsSuccessWhenLoaded() {
    runBlocking {
      val userId = "1"
      val component = createComponent(userId)
      component
        .userUiState
        .test {
          val userUiState = awaitItem()
          assertTrue(userUiState is UserComponent.UserUiState.Success)
          assertEquals(user, userUiState.user)
          assertEquals(address, userUiState.address)
        }
    }
  }

  @Test
  fun testUiStateIsErrorWhenUserDoesNotExist() {
    runBlocking {
      val userId = "1"
      val expectedException = Exception("Can't find user with id $userId")
      val component = createComponent(
        userId,
        loadUserByUserId = mockk {
          coEvery { this@mockk.invoke(userId) } returns flow { throw expectedException }
        },
      )
      component
        .userUiState
        .test {
          val userUiState = awaitItem()
          assertTrue(userUiState is UserComponent.UserUiState.Error)
          assertEquals(expectedException.message, userUiState.exception.message)
        }
    }
  }

  @Test
  fun testUiStateIsErrorWhenAddressDoesNotExist() {
    runBlocking {
      val userId = "1"
      val expectedException = Exception("Can't find address with user id $userId")
      val component = createComponent(
        userId,
        loadAddressByUserId = mockk {
          coEvery { this@mockk.invoke(userId) } returns flow { throw expectedException }
        },
      )
      component
        .userUiState
        .test {
          val userUiState = awaitItem()
          assertTrue(userUiState is UserComponent.UserUiState.Error)
          assertEquals(expectedException.message, userUiState.exception.message)
        }
    }
  }

  @Test
  fun testOnBackClickThenOnBackClickCalled() {
    runBlocking {
      val userId = "1"
      var isOnClickCalled = false
      val component = createComponent(
        userId,
        onUserClick = {
          isOnClickCalled = true
        },
      )
      component.onClick()
      assertTrue(isOnClickCalled)
    }
  }

  private fun createComponent(
    userId: String,
    onUserClick: (String) -> Unit = {},
    loadUserByUserId: LoadUserByUserIdUseCase = mockk {
      coEvery<Flow<User>> { this@mockk.invoke(userId) } returns flowOf(user)
    },
    loadAddressByUserId: LoadAddressByUserIdCase = mockk {
      coEvery<Flow<Address>> { this@mockk.invoke(userId) } returns flowOf(address)
    },
    componentContext: ComponentContext = defaultComponentContext,
    coroutineContext: CoroutineContext = dispatcher,
  ): UserComponent {
    return UserViewModel(
      userId,
      onUserClick,
      loadUserByUserId,
      loadAddressByUserId,
      componentContext,
      coroutineContext,
    )
  }
}
