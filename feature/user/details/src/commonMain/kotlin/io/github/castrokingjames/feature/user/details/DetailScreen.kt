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
@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.castrokingjames.feature.user.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.castrokingjames.model.Address
import io.github.castrokingjames.model.User
import io.github.castrokingjames.ui.shimmerBackground
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun DetailsScreen(
  modifier: Modifier = Modifier,
  component: DetailsComponent,
) {
  Scaffold(
    contentColor = MaterialTheme.colorScheme.background,
    topBar = {
      Toolbar {
        component.onBackClick()
      }
    },
  ) { padding ->
    Box(
      modifier = Modifier.padding(padding),
    ) {
      DetailsContent(component)
    }
  }
}

@Composable
private fun Toolbar(
  onBackButtonClick: () -> Unit,
) {
  TopAppBar(
    title = {
    },
    navigationIcon = {
      IconButton(
        onClick = {
          onBackButtonClick()
        },
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = MaterialTheme.colorScheme.onPrimary,
        )
      }
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.background,
    ),
  )
}

@Composable
private fun DetailsContent(component: DetailsComponent) {
  val userUiState = component
    .userUiState
    .collectAsState()
    .value

  when (userUiState) {
    DetailsComponent.UserUiState.Loading -> {
      LoadingView()
    }

    is DetailsComponent.UserUiState.Success -> {
      val user = userUiState.user
      val address = userUiState.address
      ContentView(user, address)
    }

    is DetailsComponent.UserUiState.Error -> {
      val error = userUiState.exception
      ErrorView(error)
    }
  }
}

@Composable
private fun LoadingView() {
  ContentView()
}

@Composable
private fun ContentView(
  user: User,
  address: Address,
) {
  val thumbnail = user.thumbnail
  val name = "${user.firstName} ${user.lastName}"
  val email = user.email
  val address = "${address.street}, ${address.city}, ${address.state} ${address.country}, ${address.postcode}"
  val date = Date(user.birthday * 1000L)
  val formatter = SimpleDateFormat("MMMM dd YYYY")
  val birthday = formatter.format(date)
  ContentView(thumbnail, name, email, address, birthday)
}

@Composable
private fun ContentView(
  thumbnail: String? = null,
  name: String? = null,
  email: String? = null,
  address: String? = null,
  birthday: String? = null,
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
  ) {
    Column(
      modifier = Modifier.align(Alignment.TopCenter),
    ) {
      AsyncImage(
        model = thumbnail,
        modifier = Modifier
          .size(240.dp)
          .clip(CircleShape)
          .align(Alignment.CenterHorizontally)
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
      Text(
        text = name ?: "",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .then(
            if (name == null) {
              Modifier
                .shimmerBackground()
                .fillMaxWidth()
            } else {
              Modifier
            },
          ),
      )
      Spacer(
        modifier = Modifier.size(16.dp),
      )
      Text(
        text = email ?: "",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
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
        modifier = Modifier.size(8.dp),
      )
      Text(
        text = address ?: "",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .then(
            if (address == null) {
              Modifier
                .shimmerBackground()
                .width(120.dp)
            } else {
              Modifier
            },
          ),
      )
      Spacer(
        modifier = Modifier.size(8.dp),
      )
      Text(
        text = "Birthday: $birthday" ?: "",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .then(
            if (birthday == null) {
              Modifier
                .shimmerBackground()
                .width(120.dp)
            } else {
              Modifier
            },
          ),
      )
    }
  }
}

@Composable
private fun ErrorView(exception: Throwable) {
  Box(
    modifier = Modifier.fillMaxSize(),
  ) {
    Text(
      text = exception.message ?: "",
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onPrimary,
      modifier = Modifier.align(Alignment.Center),
    )
  }
}
