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
package io.github.castrokingjames.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.github.castrokingjames.feature.user.details.DetailsScreen
import io.github.castrokingjames.feature.user.list.ListScreen

@Composable
fun App(
  modifier: Modifier = Modifier,
  component: AppComponent,
) {
  Children(
    stack = component.stack,
    modifier = modifier,
    animation = stackAnimation(animator = fade() + scale()),
  ) {
    when (val child = it.instance) {
      is AppComponent.Child.List -> ListScreen(
        modifier = modifier,
        component = child.component,
      )

      is AppComponent.Child.Details -> DetailsScreen(
        modifier = modifier,
        component = child.component,
      )
    }
  }
}
