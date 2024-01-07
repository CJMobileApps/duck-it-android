package com.cjmobileapps.duckitandroid.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailPasswordUi(
    modifier: Modifier,
    emailText: String,
    onEmailValueChange: (text: String) -> Unit,
    passwordText: String,
    onPasswordValueChange: (text: String) -> Unit,
    loginButtonText: String,
    isLogInButtonEnabled: Boolean,
    loginButtonClicked: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        //todo add is loading
        if (false) {
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
                label = { Text("Email") }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(""),
                value = passwordText,
                onValueChange = onPasswordValueChange,
                label = { Text("Password") }
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
//        Greeting("Android")
//    }
//}
