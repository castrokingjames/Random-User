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
@file:OptIn(
  ExperimentalInstanceKeeperApi::class,
  ExperimentalCoroutinesApi::class,
)

package io.github.castrokingjames.feature.user.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.instancekeeper.ExperimentalInstanceKeeperApi
import com.arkivanov.essenty.instancekeeper.retainedSimpleInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import io.github.castrokingjames.feature.user.list.user.UserComponent
import io.github.castrokingjames.usecase.LoadUsersBySizeUseCase
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class ListViewModel constructor(
  private val onUserClick: (String) -> Unit,
  private val loadUsersBySize: LoadUsersBySizeUseCase,
  private val userComponentFactory: UserComponent.Factory,
  private val componentContext: ComponentContext,
  private val coroutineContext: CoroutineContext,
) : ListComponent, ComponentContext by componentContext {

  private val size: MutableSharedFlow<Int> = MutableSharedFlow(1)

  private val scope = coroutineScope(coroutineContext + SupervisorJob())

  override val listUiState: StateFlow<ListComponent.ListUiState> = loadUsers()
    .stateIn(
      scope,
      started = SharingStarted.WhileSubscribed(5000L),
      initialValue = ListComponent.ListUiState.Success(emptyList()),
    )

  override fun load(newSize: Int) {
    size.tryEmit(newSize)
  }

  override fun userComponent(userId: String): UserComponent {
    return retainedSimpleInstance("user_component_factory_$userId") {
      userComponentFactory(userId, childContext("user_component_$userId")) { userId ->
        onUserClick.invoke(userId)
      }
    }
  }

  private fun loadUsers(): Flow<ListComponent.ListUiState> {
    return size.flatMapLatest { newSize ->
      channelFlow {
        send(ListComponent.ListUiState.Loading)
        loadUsersBySize(newSize)
          .catch { e ->
            send(ListComponent.ListUiState.Error(e))
          }
          .collectLatest { users ->
            send(ListComponent.ListUiState.Success(users))
          }
      }
    }
  }
}
