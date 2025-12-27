package org.example.gitwispr

import androidx.compose.runtime.*

sealed class Screen {
    object Landing : Screen()
    data class RepoList(val username: String, val token: String) : Screen()
}

class Navigator {
    var currentScreen by mutableStateOf<Screen>(Screen.Landing)
        private set

    fun navigateToRepoList(username: String, token: String) {
        currentScreen = Screen.RepoList(username, token)
    }

    fun navigateToLanding() {
        currentScreen = Screen.Landing
    }
}