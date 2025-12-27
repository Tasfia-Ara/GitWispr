package org.example.gitwispr.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

import org.example.gitwispr.auth.GithubAuthenticator
import org.example.gitwispr.auth.GithubAuthService


class AndroidGitHubAuthenticator(
    private val context: Context,
    private val config: AuthConfig = AuthConfig()
) : GithubAuthenticator {

    private val authService = GithubAuthService()

    override fun startOAuthFlow(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val authUrl = buildAuthUrl()

        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        intent.launchUrl(context, Uri.parse(authUrl))

        // Store callbacks for when the redirect happens
        AuthCallbackHandler.onSuccess = onSuccess
        AuthCallbackHandler.onError = onError
    }

    override suspend fun exchangeCodeForToken(code: String): Result<String> {
        return authService.exchangeCodeForToken(code, config)
    }

    private fun buildAuthUrl(): String {
        return "https://github.com/login/oauth/authorize?" +
                "client_id=${config.clientId}&" +
                "redirect_uri=${config.redirectUri}&" +
                "scope=${config.scope}"
    }
}

object AuthCallbackHandler {
    var onSuccess: ((String) -> Unit)? = null
    var onError: ((String) -> Unit)? = null
}