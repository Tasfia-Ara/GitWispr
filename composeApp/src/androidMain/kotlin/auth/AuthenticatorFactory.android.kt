package org.example.gitwispr.auth

import android.content.Context
import org.example.gitwispr.auth.AndroidGitHubAuthenticator

// Store context globally for Android
object AndroidContext {
    lateinit var context: Context
}

actual fun createGithubAuthenticator(): GithubAuthenticator {
    return AndroidGitHubAuthenticator(AndroidContext.context)
}