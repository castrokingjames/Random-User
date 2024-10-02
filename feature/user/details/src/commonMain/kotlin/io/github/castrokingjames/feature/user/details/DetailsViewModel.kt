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
package io.github.castrokingjames.feature.user.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import io.github.castrokingjames.usecase.LoadAddressByUserIdCase
import io.github.castrokingjames.usecase.LoadUserByUserIdCase
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DetailsViewModel constructor(
  private val userId: String,
  private val onBackClick: () -> Unit,
  private val loadUserByUserId: LoadUserByUserIdCase,
  private val loadAddressByUserId: LoadAddressByUserIdCase,
  private val componentContext: ComponentContext,
  private val coroutineContext: CoroutineContext,
) : DetailsComponent, ComponentContext by componentContext {

  private val scope = coroutineScope(coroutineContext + SupervisorJob())

  override val userUiState: StateFlow<DetailsComponent.UserUiState> =
    loadUser()
      .catch { e ->
        emit(DetailsComponent.UserUiState.Error(e))
      }
      .stateIn(
        scope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DetailsComponent.UserUiState.Loading,
      )

  override fun onBackClick() {
    onBackClick.invoke()
  }

  private fun loadUser(): Flow<DetailsComponent.UserUiState> {
    return combine(
      loadUserByUserId(userId),
      loadAddressByUserId(userId),
    ) { user, address ->
      DetailsComponent.UserUiState.Success(user, address)
    }
  }
}
