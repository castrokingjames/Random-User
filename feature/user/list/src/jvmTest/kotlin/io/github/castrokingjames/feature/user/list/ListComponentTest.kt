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
package io.github.castrokingjames.feature.user.list

import app.cash.turbine.test
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.castrokingjames.feature.user.list.user.UserComponent
import io.github.castrokingjames.feature.user.list.user.UserComponentFactory
import io.github.castrokingjames.model.Address
import io.github.castrokingjames.model.User
import io.github.castrokingjames.usecase.LoadAddressByUserIdCase
import io.github.castrokingjames.usecase.LoadUserByUserIdUseCase
import io.github.castrokingjames.usecase.LoadUsersBySizeUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.coroutines.CoroutineContext
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
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

class ListComponentTest {

  private val lifecycle = LifecycleRegistry()
  private val defaultComponentContext = DefaultComponentContext(lifecycle)
  private val dispatcher = UnconfinedTestDispatcher()

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
  fun testUiStateIsSuccessEmptyWhenInitialized() {
    runBlocking {
      val size = 100
      val component = createComponent(size)
      val listUiState = component.listUiState.value
      assertTrue(listUiState is ListComponent.ListUiState.Success)
      assertTrue(listUiState.users.isEmpty())
    }
  }

  @Test
  fun testUiStateIsSuccessWhenLoading10Users() {
    runBlocking {
      val size = 10
      val component = createComponent(size)
      component.load(size)
      component
        .listUiState
        .test {
          val listUiState = awaitItem()
          assertTrue(listUiState is ListComponent.ListUiState.Success)
          assertEquals(size, listUiState.users.size)
        }
    }
  }

  @Test
  fun testUiStateIsSuccessWhenLoading100Users() {
    runBlocking {
      val size = 100
      val component = createComponent(size)
      component.load(size)
      component
        .listUiState
        .test {
          val listUiState = awaitItem()
          assertTrue(listUiState is ListComponent.ListUiState.Success)
          assertEquals(size, listUiState.users.size)
        }
    }
  }

  @Test
  fun testUiStateIsErrorsWhenLoadingInvalidSize() {
    runBlocking {
      val size = 0
      val expectedException = Exception("Invalid size")
      val component = createComponent(
        size = size,
        loadUsersBySize = mockk {
          coEvery<Flow<List<User>>> { this@mockk.invoke(size) } returns flow { throw expectedException }
        },
      )
      component.load(size)
      component
        .listUiState
        .test {
          val listUiState = awaitItem()
          assertTrue(listUiState is ListComponent.ListUiState.Error)
          assertEquals(expectedException.message, listUiState.exception.message)
        }
    }
  }

  @Test
  fun testUserComponentFactory() {
    runBlocking {
      val userId = "1"
      val size = 100
      val factory = createFactory(userId)
      val component = createComponent(
        size = size,
        userComponentFactory = factory,
      )
      val userComponent = component.userComponent(userId)
      assertNotNull(userComponent)
    }
  }

  @Test
  fun testUserComponentOnClickThenOnUserClickCalled() {
    runBlocking {
      val userId = "1"
      val size = 100
      var isOnUserClickCalled = false
      val factory = createFactory(userId)
      val component = createComponent(
        size = size,
        userComponentFactory = factory,
        onUserClick = {
          isOnUserClickCalled = true
        },
      )
      val userComponent = component.userComponent(userId)
      userComponent.onClick()
      assertTrue(isOnUserClickCalled)
    }
  }

  private fun createComponent(
    size: Int,
    onUserClick: (String) -> Unit = {},
    loadUsersBySize: LoadUsersBySizeUseCase = mockk {
      coEvery<Flow<List<User>>> { this@mockk.invoke(size) } returns flowOf(generateUsers(size))
    },
    userComponentFactory: UserComponent.Factory = mockk(),
    componentContext: ComponentContext = defaultComponentContext,
    coroutineContext: CoroutineContext = dispatcher,
  ): ListComponent {
    return ListViewModel(
      onUserClick,
      loadUsersBySize,
      userComponentFactory,
      componentContext,
      coroutineContext,
    )
  }

  private fun createFactory(userId: String): UserComponent.Factory {
    val loadUserByUserId: LoadUserByUserIdUseCase = mockk {
      coEvery<Flow<User>> { this@mockk.invoke(userId) } returns flow {}
    }
    val loadAddressByUserId: LoadAddressByUserIdCase = mockk {
      coEvery<Flow<Address>> { this@mockk.invoke(userId) } returns flow {}
    }
    return UserComponentFactory(loadUserByUserId, loadAddressByUserId, dispatcher)
  }
}
