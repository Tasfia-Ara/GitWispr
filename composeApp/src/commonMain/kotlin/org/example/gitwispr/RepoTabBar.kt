package org.example.gitwispr

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

enum class RepoTab {
    OVERVIEW,
    STRUCTURE,
    ISSUES
}

@Composable
fun RepoTabBar(
    currentTab: RepoTab,
    owner: String,
    repo: String,
    navigator: Navigator
) {
    TabRow(
        selectedTabIndex = currentTab.ordinal,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Tab(
            selected = currentTab == RepoTab.OVERVIEW,
            onClick = { navigator.navigateToOverview(owner, repo) },
            text = { Text("Overview") }
        )
        Tab(
            selected = currentTab == RepoTab.STRUCTURE,
            onClick = { navigator.navigateToStructure(owner, repo) },
            text = { Text("Interactive Graph") }
        )
        Tab(
            selected = currentTab == RepoTab.ISSUES,
            onClick = { navigator.navigateToIssues(owner, repo) },
            text = { Text("Analyze Issue") }
        )
    }
}