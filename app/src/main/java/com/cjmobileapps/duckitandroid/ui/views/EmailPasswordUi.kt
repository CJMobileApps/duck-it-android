package com.cjmobileapps.duckitandroid.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.cjmobileapps.duckitandroid.R

@Composable
fun EmailPasswordUi(
    modifier: Modifier,
    emailText: String,
    onEmailValueChange: (text: String) -> Unit,
    passwordText: String,
    onPasswordValueChange: (text: String) -> Unit,
    loginButtonText: String,
    isLogInButtonEnabled: Boolean,
    loginButtonClicked: () -> Unit,
    showLoading: Boolean
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        if (showLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(""),
                value = emailText,
                onValueChange = onEmailValueChange,
                label = { Text(stringResource(R.string.email)) }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(""),
                value = passwordText,
                onValueChange = onPasswordValueChange,
                label = { Text(stringResource(R.string.password)) },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (isPasswordVisible) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (isPasswordVisible) stringResource(R.string.hide_password)
                    else stringResource(R.string.show_password)

                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )

            Button(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                enabled = isLogInButtonEnabled,
                onClick = loginButtonClicked
            ) {
                Text(text = loginButtonText)
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    DuckItAndroidTheme {
//
//    }
//}
