package org.example.gitwispr.auth

actual fun createGithubAuthenticator(): GithubAuthenticator {
    // Mock implementation for WASM (you can implement proper OAuth later)
    return object : GithubAuthenticator {
        override fun startOAuthFlow(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
            println("🌐 GitHub OAuth on Web - Coming soon!")
            // For now, simulate a successful login with a fake token
            onSuccess("fake-token-for-web-testing")
        }

        override suspend fun exchangeCodeForToken(code: String): Result<String> {
            return Result.success("fake-token")
        }
    }
}