package org.example.gitwispr

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssuesPage(
    owner: String,
    repo: String,
    navigator: Navigator
) {
    var issueUrl by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("$owner/$repo") },
                    navigationIcon = {
                        TextButton(onClick = {
                            navigator.navigateToRepoList(owner, "")
                        }) {
                            Text("Back", fontSize = 16.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                // Shared Tab Bar
                RepoTabBar(
                    currentTab = RepoTab.ISSUES,
                    owner = owner,
                    repo = repo,
                    navigator = navigator
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "First time contributing?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Get unstuck!",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Paste a GitHub issue link below:",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = issueUrl,
                onValueChange = { issueUrl = it },
                label = { Text("GitHub Issue URL") },
                placeholder = { Text("https://github.com/$owner/$repo/issues/123") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // TODO: Implement issue analysis
                    println("Analyzing issue: $issueUrl")
                },
                modifier = Modifier.height(48.dp),
                enabled = issueUrl.isNotBlank()
            ) {
                Text("Analyze Issue", fontSize = 16.sp)
            }
        }
    }
}