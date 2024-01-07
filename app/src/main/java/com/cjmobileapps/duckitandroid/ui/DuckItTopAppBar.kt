package com.cjmobileapps.duckitandroid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.cjmobileapps.duckitandroid.R
import com.cjmobileapps.duckitandroid.data.model.compose.UserLoggedInState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuckItTopAppBar(
    navController: NavController,
    userLoggedInState: UserLoggedInState,
    onIsUserLoggedInButtonClick: () -> Unit = {}
) {

    var menuExpanded by remember {
        mutableStateOf(false)
    }
    val horizontalArrangement =
        if (navController.previousBackStackEntry != null) Arrangement.Start else Arrangement.Center

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag(""),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag(""),
                    horizontalArrangement = horizontalArrangement,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            }
        },
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            val isUserLoggedIn = (userLoggedInState == UserLoggedInState.UserLoggedIn)

            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "",
                    tint = Color.White
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
            ) {
                if (userLoggedInState != UserLoggedInState.DontShowUserLoggedIn) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (isUserLoggedIn) stringResource(R.string.sign_out)
                                else stringResource(R.string.log_in),
                                color = Color.White
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onIsUserLoggedInButtonClick.invoke()
                        },
                    )
                }
            }
        }
    )
}
