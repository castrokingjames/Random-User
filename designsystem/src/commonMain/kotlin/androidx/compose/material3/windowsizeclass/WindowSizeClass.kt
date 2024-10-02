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
package androidx.compose.material3.windowsizeclass

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.jvm.JvmInline

@Immutable
class WindowSizeClass private constructor(
  val widthSizeClass: WindowWidthSizeClass,
  val heightSizeClass: WindowHeightSizeClass,
) {
  companion object {
    fun calculateFromSize(size: DpSize): WindowSizeClass {
      val windowWidthSizeClass = WindowWidthSizeClass.fromWidth(size.width)
      val windowHeightSizeClass = WindowHeightSizeClass.fromHeight(size.height)
      return WindowSizeClass(windowWidthSizeClass, windowHeightSizeClass)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as WindowSizeClass

    if (widthSizeClass != other.widthSizeClass) return false
    if (heightSizeClass != other.heightSizeClass) return false

    return true
  }

  override fun hashCode(): Int {
    var result = widthSizeClass.hashCode()
    result = 31 * result + heightSizeClass.hashCode()
    return result
  }

  override fun toString() = "WindowSizeClass($widthSizeClass, $heightSizeClass)"
}

@JvmInline
value class WindowWidthSizeClass private constructor(private val value: Int) :
  Comparable<WindowWidthSizeClass> {
  override operator fun compareTo(other: WindowWidthSizeClass) = value.compareTo(other.value)

  override fun toString(): String {
    return "WindowWidthSizeClass." + when (this) {
      Compact -> "Compact"
      Medium -> "Medium"
      Expanded -> "Expanded"
      else -> ""
    }
  }

  companion object {
    val Compact = WindowWidthSizeClass(0)
    val Medium = WindowWidthSizeClass(1)
    val Expanded = WindowWidthSizeClass(2)

    internal fun fromWidth(width: Dp): WindowWidthSizeClass {
      require(width >= 0.dp) { "Width must not be negative" }
      return when {
        width < 600.dp -> Compact
        width < 840.dp -> Medium
        else -> Expanded
      }
    }
  }
}

@JvmInline
value class WindowHeightSizeClass private constructor(private val value: Int) :
  Comparable<WindowHeightSizeClass> {
  override operator fun compareTo(other: WindowHeightSizeClass) = value.compareTo(other.value)

  override fun toString(): String {
    return "WindowHeightSizeClass." + when (this) {
      Compact -> "Compact"
      Medium -> "Medium"
      Expanded -> "Expanded"
      else -> ""
    }
  }

  companion object {
    val Compact = WindowHeightSizeClass(0)
    val Medium = WindowHeightSizeClass(1)
    val Expanded = WindowHeightSizeClass(2)

    internal fun fromHeight(height: Dp): WindowHeightSizeClass {
      require(height >= 0.dp) { "Height must not be negative" }
      return when {
        height < 480.dp -> Compact
        height < 900.dp -> Medium
        else -> Expanded
      }
    }
  }
}
