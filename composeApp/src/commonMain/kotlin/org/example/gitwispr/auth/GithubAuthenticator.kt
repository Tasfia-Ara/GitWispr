package org.example.gitwispr.auth

interface GithubAuthenticator {
    fun startOAuthFlow(onSuccess: (String) -> Unit, onError: (String) -> Unit)
    suspend fun exchangeCodeForToken(code: String): Result<String>
}

data class AuthConfig(
    val clientId: String = "Ov23lik9srgz94verHGt",
    val clientSecret: String = "65afc736ecdd2d142a5672ba1fb2b7862fdd5608",
    val redirectUri: String = "gitwispr://callback",
    val scope: String = "repo,read:user"
)
