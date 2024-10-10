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

package io.github.castrokingjames.feature.user.list

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.castrokingjames.feature.user.list.user.UserComponent
import io.github.castrokingjames.model.User
import io.github.castrokingjames.usecase.LoadUsersBySizeUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class ListComponentFactoryTest {

  private val lifecycle = LifecycleRegistry()
  private val defaultComponentContext = DefaultComponentContext(lifecycle)
  private val dispatcher = UnconfinedTestDispatcher()
  private lateinit var loadUsersBySizeUse: LoadUsersBySizeUseCase
  private lateinit var userComponentFactory: UserComponent.Factory
  private lateinit var factory: ListComponent.Factory

  @Before
  fun setup() {
    Dispatchers.setMain(dispatcher)
    lifecycle.resume()
    loadUsersBySizeUse = mockk {
      coEvery<Flow<List<User>>> { this@mockk.invoke(1) } returns flow {}
    }
    userComponentFactory = mockk()
    factory = ListComponentFactory(loadUsersBySizeUse, userComponentFactory, dispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun testCreateComponent() {
    val component = factory(defaultComponentContext, onUserClick = {})
    assertNotNull(component)
  }
}
