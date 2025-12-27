package org.example.gitwispr.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthCallbackActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent?.data
        if (uri != null && uri.scheme == "gitwispr") {
            val code = uri.getQueryParameter("code")
            val error = uri.getQueryParameter("error")

            when {
                code != null -> {
                    // Exchange code for token
                    CoroutineScope(Dispatchers.Main).launch {
                        val authenticator = AndroidGitHubAuthenticator(applicationContext)
                        val result = authenticator.exchangeCodeForToken(code)

                        result.fold(
                            onSuccess = { token ->
                                AuthCallbackHandler.onSuccess?.invoke(token)
                            },
                            onFailure = { exception ->
                                AuthCallbackHandler.onError?.invoke(exception.message ?: "Unknown error")
                            }
                        )
                        finish()
                    }
                }
                error != null -> {
                    AuthCallbackHandler.onError?.invoke(error)
                    finish()
                }
            }
        } else {
            finish()
        }
    }
}