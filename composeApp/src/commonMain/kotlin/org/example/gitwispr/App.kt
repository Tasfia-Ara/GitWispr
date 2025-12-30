package org.example.gitwispr

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navigator = remember { Navigator() }

    MaterialTheme {
        when (val screen = navigator.currentScreen) {
            is Screen.Landing -> LandingPage(navigator)
            is Screen.RepoList -> RepoListPage(
                username = screen.username,
                token = screen.token,
                navigator = navigator
            )
            is Screen.Overview -> OverviewPage(
                owner = screen.owner,
                repo = screen.repo,
                navigator = navigator
            )
            is Screen.Structure -> StructurePage(
                owner = screen.owner,
                repo = screen.repo,
                navigator = navigator
            )
            is Screen.Issues -> IssuesPage(
                owner = screen.owner,
                repo = screen.repo,
                navigator = navigator
            )
        }
    }
}