package org.example.gitwispr.auth

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.AuthenticationServices.*

import org.example.gitwispr.auth.GithubAuthenticator
import org.example.gitwispr.auth.GithubAuthService

class IOSGitHubAuthenticator(
    private val config: AuthConfig = AuthConfig()
) : GithubAuthenticator {

    private val authService = GithubAuthService()

    override fun startOAuthFlow(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val authUrl = buildAuthUrl()

        val url = NSURL.URLWithString(authUrl)
        if (url != null) {
            UIApplication.sharedApplication.openURL(url)
        }

        // Store callbacks
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