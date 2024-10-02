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
import io.github.castrokingjames.model.Address
import io.github.castrokingjames.model.User
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

  val userUiState: StateFlow<UserUiState>

  fun onBackClick()

  sealed interface UserUiState {
    data class Success(val user: User, val address: Address) : UserUiState
    data class Error(val exception: Throwable) : UserUiState
    data object Loading : UserUiState
  }

  interface Factory {
    operator fun invoke(userId: String, componentContext: ComponentContext, onBackClick: () -> Unit): DetailsComponent
  }
}
