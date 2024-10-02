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

import com.arkivanov.decompose.ComponentContext
import io.github.castrokingjames.feature.user.list.user.UserComponent
import io.github.castrokingjames.usecase.LoadUsersBySizeUseCase
import kotlin.coroutines.CoroutineContext

class ListComponentFactory(
  private val loadUsersBySize: LoadUsersBySizeUseCase,
  private val userComponentFactory: UserComponent.Factory,
  private val coroutineContext: CoroutineContext,
) : ListComponent.Factory {

  override operator fun invoke(componentContext: ComponentContext, onUserClick: (String) -> Unit): ListComponent {
    return ListViewModel(
      onUserClick,
      loadUsersBySize,
      userComponentFactory,
      componentContext,
      coroutineContext,
    )
  }
}
