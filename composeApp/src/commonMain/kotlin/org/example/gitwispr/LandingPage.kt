package org.example.gitwispr

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.gitwispr.auth.GithubAuthenticator // ✅ This is the ONLY auth import
@Composable
fun LandingPage(authenticator: GithubAuthenticator) {  // ✅ Receives authenticator as parameter
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to GitWispr!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("To get started, sign in with your Github account")

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLoading = true
                    authenticator.startOAuthFlow(
                        onSuccess = { token ->
                            isLoading = false
                            // TODO: Save token and navigate
                            println("Token received: $token")
                        },
                        onError = { error ->
                            isLoading = false
                            errorMessage = error
                        }
                    )
                }
            ) {
                Text("Sign in with GitHub")
            }
        }

        errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Error: $error",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}