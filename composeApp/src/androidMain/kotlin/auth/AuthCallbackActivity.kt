package org.example.gitwispr.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthCallbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the OAuth code from the redirect URI
        val uri = intent.data
        val code = uri?.getQueryParameter("code")
        val error = uri?.getQueryParameter("error")

        when {
            code != null -> {
                // Exchange code for token
                CoroutineScope(Dispatchers.Main).launch {
                    val authenticator = AndroidGitHubAuthenticator(
                        context = applicationContext,
                        config = AuthConfig()
                    )

                    val result = authenticator.exchangeCodeForToken(code)
                    result.fold(
                        onSuccess = { token ->
                            AuthCallbackHandler.onSuccess?.invoke(token)
                        },
                        onFailure = { exception ->
                            AuthCallbackHandler.onError?.invoke(exception.message ?: "Unknown error")
                        }
                    )
                }
            }
            error != null -> {
                AuthCallbackHandler.onError?.invoke(error)
            }
            else -> {
                AuthCallbackHandler.onError?.invoke("No code or error received")
            }
        }

        // Close this activity
        finish()
    }
}