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
package io.github.castrokingjames.feature.user.list.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import io.github.castrokingjames.ui.UserCardView

@Composable
fun UserView(component: UserComponent) {
  val userUiState = component
    .userUiState
    .collectAsState()
    .value
  when (userUiState) {
    UserComponent.UserUiState.Loading -> {
      UserCardView()
    }
    is UserComponent.UserUiState.Success -> {
      val user = userUiState.user
      val address = userUiState.address
      val name = "${user.firstName} ${user.lastName}"
      val thumbnail = user.thumbnail
      val email = user.email
      val location = "${address.city} ${address.country}"
      UserCardView(
        name = name,
        thumbnail = thumbnail,
        email = email,
        location = location,
      ) {
        component.onClick()
      }
    }

    is UserComponent.UserUiState.Error -> {
    }
  }
}
