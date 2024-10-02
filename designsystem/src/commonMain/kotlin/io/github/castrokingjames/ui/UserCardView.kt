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
@file:OptIn(ExperimentalFoundationApi::class)

package io.github.castrokingjames.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun UserCardView(
  modifier: Modifier = Modifier,
  thumbnail: String? = null,
  name: String? = null,
  email: String? = null,
  location: String? = null,
  onClick: (() -> Unit)? = null,
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        top = 8.dp,
        start = 16.dp,
        end = 16.dp,
      )
      .clickable(enabled = onClick != null) {
        onClick?.invoke()
      },
    elevation = CardDefaults.cardElevation(
      defaultElevation = 4.dp,
    ),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primary,
    ),
    shape = RoundedCornerShape(0.dp),
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
    ) {
      AsyncImage(
        model = thumbnail,
        modifier = Modifier
          .size(60.dp)
          .clip(CircleShape)
          .then(
            if (thumbnail == null) {
              Modifier.shimmerBackground()
            } else {
              Modifier
            },
          ),
        contentDescription = null,
      )
      Spacer(
        modifier = Modifier.size(16.dp),
      )
      Column {
        Text(
          text = name ?: "",
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier
            .fillMaxWidth()
            .then(
              if (name == null) {
                Modifier.shimmerBackground()
              } else {
                Modifier
              },
            ),
        )
        Spacer(
          modifier = Modifier.size(4.dp),
        )
        Text(
          text = email ?: "",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSecondary,
          modifier = Modifier
            .then(
              if (email == null) {
                Modifier
                  .shimmerBackground()
                  .width(120.dp)
              } else {
                Modifier
              },
            ),
        )
        Spacer(
          modifier = Modifier.size(4.dp),
        )
        Text(
          text = location ?: "",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSecondary,
          modifier = Modifier
            .then(
              if (location == null) {
                Modifier
                  .shimmerBackground()
                  .width(100.dp)
              } else {
                Modifier
              },
            ),
        )
      }
    }
  }
}
