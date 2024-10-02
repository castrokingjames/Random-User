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
import io.github.castrokingjames.usecase.LoadAddressByUserIdCase
import io.github.castrokingjames.usecase.LoadUserByUserIdCase
import kotlin.coroutines.CoroutineContext

class DetailsComponentFactory(
  private val loadUserByUserId: LoadUserByUserIdCase,
  private val loadAddressByUserId: LoadAddressByUserIdCase,
  private val coroutineContext: CoroutineContext,
) : DetailsComponent.Factory {

  override operator fun invoke(userId: String, componentContext: ComponentContext, onBackClick: () -> Unit): DetailsComponent {
    return DetailsViewModel(
      userId,
      onBackClick,
      loadUserByUserId,
      loadAddressByUserId,
      componentContext,
      coroutineContext,
    )
  }
}
