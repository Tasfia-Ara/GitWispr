package org.example.gitwispr

import androidx.compose.runtime.*

sealed class Screen {
    object Landing : Screen()
    data class RepoList(val username: String, val token: String) : Screen()
    data class Overview(val owner: String, val repo: String) : Screen()
    data class Structure(val owner: String, val repo: String) : Screen()
    data class Issues(val owner: String, val repo: String) : Screen()
}

class Navigator {
    var currentScreen by mutableStateOf<Screen>(Screen.Landing)
        private set

    fun navigateToRepoList(username: String, token: String) {
        currentScreen = Screen.RepoList(username, token)
    }

    fun navigateToOverview(owner: String, repo: String) {
        currentScreen = Screen.Overview(owner, repo)
    }

    fun navigateToStructure(owner: String, repo: String) {
        currentScreen = Screen.Structure(owner, repo)
    }

    fun navigateToIssues(owner: String, repo: String) {
        currentScreen = Screen.Issues(owner, repo)
    }

    fun navigateToLanding() {
        currentScreen = Screen.Landing
    }
}