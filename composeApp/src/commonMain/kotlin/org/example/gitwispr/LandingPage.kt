package org.example.gitwispr

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.gitwispr.auth.createGithubAuthenticator

@Composable
fun LandingPage(navigator: Navigator) {
    val authenticator = remember { createGithubAuthenticator() }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // App Name
            Text(
                text = "GitWispr",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tagline
            Text(
                text = "Navigate unfamiliar code with confidence",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "AI-powered insights for exploring open source repositories",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(56.dp))

            // GitHub Login Button
            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null

                    authenticator.startOAuthFlow(
                        onSuccess = { token ->
                            isLoading = false
                            println("✅ Successfully logged in! Token: $token")

                            // Navigate to RepoList
                            // For now, use mock username since we don't have real OAuth
                            navigator.navigateToRepoList(
                                username = "MockUser",  // TODO: Get real username from GitHub API
                                token = token
                            )
                        },
                        onError = { error ->
                            isLoading = false
                            errorMessage = error
                            println("❌ Login failed: $error")
                        }
                    )
                },
                modifier = Modifier
                    .height(56.dp)
                    .widthIn(min = 280.dp),
                enabled = !isLoading
            ) {
                Text(
                    text = if (isLoading) "Logging in..." else "Continue with GitHub",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Optional: Add a small note
            if (!isLoading && errorMessage == null) {
                Text(
                    text = "Connect your GitHub account to get started",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}