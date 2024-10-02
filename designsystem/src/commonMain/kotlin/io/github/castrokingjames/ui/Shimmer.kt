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

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode

fun Modifier.shimmerBackground(shape: Shape = RectangleShape): Modifier = composed {
  val transition = rememberInfiniteTransition()

  val translateAnimation by transition.animateFloat(
    initialValue = 0f,
    targetValue = 400f,
    animationSpec = infiniteRepeatable(
      tween(durationMillis = 1500, easing = LinearOutSlowInEasing),
      RepeatMode.Restart,
    ),
  )
  val shimmerColors = listOf(
    Color.LightGray.copy(alpha = 0.9f),
    Color.LightGray.copy(alpha = 0.4f),
  )
  val brush = Brush.linearGradient(
    colors = shimmerColors,
    start = Offset(translateAnimation, translateAnimation),
    end = Offset(translateAnimation + 100f, translateAnimation + 100f),
    tileMode = TileMode.Mirror,
  )
  return@composed this.then(background(brush, shape))
}
