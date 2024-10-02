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
@file:OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalMaterial3Api::class,
)

package io.github.castrokingjames.feature.user.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.castrokingjames.Res
import io.github.castrokingjames.add_users
import io.github.castrokingjames.app_name
import io.github.castrokingjames.feature.user.list.user.UserComponent
import io.github.castrokingjames.feature.user.list.user.UserView
import io.github.castrokingjames.model.User
import io.github.castrokingjames.show
import io.github.castrokingjames.show_users
import io.github.castrokingjames.ui.UserCardView
import org.jetbrains.compose.resources.stringResource

@Composable
fun ListScreen(
  modifier: Modifier = Modifier,
  component: ListComponent,
) {
  Scaffold(
    contentColor = MaterialTheme.colorScheme.background,
    topBar = {
      Toolbar()
    },
  ) { padding ->
    Box(
      modifier = Modifier.padding(padding),
    ) {
      ListContent(component)
    }
  }
}

@Composable
fun Toolbar() {
  TopAppBar(
    title = {
      Text(
        text = stringResource(Res.string.app_name),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimary,
      )
    },
    modifier = Modifier
      .shadow(
        elevation = 6.dp,
        spotColor = MaterialTheme.colorScheme.onPrimary,
      ),
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.primary,
    ),
  )
}

@Composable
fun ShowUsersDialog(
  shouldShowDialog: MutableState<Boolean>,
  onAddUsers: (Int) -> Unit,
) {
  val text = remember { mutableStateOf("") }
  val windowInfo = LocalWindowInfo.current
  val focusRequester = remember { FocusRequester() }

  if (shouldShowDialog.value) {
    AlertDialog(
      containerColor = MaterialTheme.colorScheme.background,
      onDismissRequest = {
        shouldShowDialog.value = false
      },
      title = {
        Text(
          text = stringResource(Res.string.show_users),
          color = MaterialTheme.colorScheme.onPrimary,
        )
      },
      text = {
        TextField(
          value = text.value,
          onValueChange = { value ->
            if (value.toIntOrNull() != null) {
              text.value = value
            }
          },
          colors = TextFieldDefaults.colors(
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onPrimary,
          ),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.focusRequester(focusRequester),
        )
      },
      confirmButton = {
        Button(
          onClick = {
            shouldShowDialog.value = false
            val size = text.value.toIntOrNull() ?: 0
            onAddUsers(size)
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
          ),
        ) {
          Text(
            text = stringResource(Res.string.show),
            color = MaterialTheme.colorScheme.onPrimary,
          )
        }
      },
    )
  }

  LaunchedEffect(windowInfo) {
    snapshotFlow { windowInfo.isWindowFocused }.collect { isWindowFocused ->
      if (isWindowFocused) {
        focusRequester.requestFocus()
      }
    }
  }
}

@Composable
fun ListContent(component: ListComponent) {
  val listUiState = component
    .listUiState
    .collectAsState()
    .value

  val shouldShowDialog = remember { mutableStateOf(false) }
  if (shouldShowDialog.value) {
    ShowUsersDialog(shouldShowDialog) { size ->
      component.load(size)
    }
  }

  when (listUiState) {
    ListComponent.ListUiState.Loading -> {
      LoadingView()
    }

    is ListComponent.ListUiState.Success -> {
      val users = listUiState.users
      ContentView(
        users,
        onAddUserClick = {
          shouldShowDialog.value = true
        },
        userComponentFactory = { userId ->
          component.userComponent(userId)
        },
      )
    }

    is ListComponent.ListUiState.Error -> {
      val error = listUiState.exception
      ErrorView(error) {
        shouldShowDialog.value = true
      }
    }
  }
}

@Composable
private fun LoadingView() {
  LazyColumn {
    for (i in 0..10) {
      item {
        UserCardView()
      }
    }
  }
}

@Composable
private fun ContentView(
  users: List<User>,
  onAddUserClick: () -> Unit,
  userComponentFactory: (String) -> UserComponent,
) {
  val size = users.size
  Box(
    modifier = Modifier.fillMaxSize(),
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
    ) {
      items(
        size,
        key = { index ->
          users[index].id
        },
        contentType = { index ->
          users[index]::class.java
        },
      ) { index ->
        val user = users[index]
        val userId = user.id
        val component = userComponentFactory(userId)
        UserView(component)
      }
    }

    ExtendedFloatingActionButton(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp),
      onClick = onAddUserClick,
      containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
      contentColor = MaterialTheme.colorScheme.onPrimary,
      text = {
        Text(
          text = stringResource(Res.string.add_users),
        )
      },
      icon = {
        Icon(
          Icons.Filled.Add,
          "Small floating action button.",
        )
      },
    )
  }
}

@Composable
private fun ErrorView(
  exception: Throwable,
  onAddUserClick: () -> Unit,
) {
  Box(
    modifier = Modifier.fillMaxSize(),
  ) {
    Text(
      text = exception.message ?: "",
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onPrimary,
      modifier = Modifier.align(Alignment.Center),
    )

    ExtendedFloatingActionButton(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp),
      onClick = onAddUserClick,
      containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
      contentColor = MaterialTheme.colorScheme.onPrimary,
      text = {
        Text(
          text = stringResource(Res.string.add_users),
        )
      },
      icon = {
        Icon(
          Icons.Filled.Add,
          "Small floating action button.",
        )
      },
    )
  }
}
