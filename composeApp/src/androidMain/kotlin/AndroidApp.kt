package org.example.gitwispr

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.example.gitwispr.auth.AndroidGitHubAuthenticator

@Composable
fun AndroidApp() {
    val context = LocalContext.current
    val authenticator = AndroidGitHubAuthenticator(context)

    App(authenticator = authenticator)
}