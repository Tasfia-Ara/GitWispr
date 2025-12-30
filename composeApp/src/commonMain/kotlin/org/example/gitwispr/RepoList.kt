package org.example.gitwispr

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.gitwispr.data.RepoCache
import org.example.gitwispr.data.Repository
import org.example.gitwispr.utils.GitHubUrlParser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListPage(
    username: String,
    token: String,
    navigator: Navigator
) {
    // Initialize mock data on first load
    LaunchedEffect(Unit) {
        RepoCache.initMockData()
    }

    val repos = RepoCache.repos
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GitWispr") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Greeting
            Text(
                text = "Hello, $username! 👋",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Recently Added Repos Section
            Text(
                text = "Recently Added Repos",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Repo List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(repos) { repo ->
                    RepoCard(
                        repo = repo,
                        onClick = {
                            navigator.navigateToOverview(repo.owner, repo.name)
                        }
                    )
                }

                // Add New Repository Button
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Add New Repository")
                    }
                }
            }
        }
    }

    // Add Repository Dialog
    if (showAddDialog) {
        AddRepoDialog(
            onDismiss = { showAddDialog = false },
            onSubmit = { owner, repo ->
                showAddDialog = false
                navigator.navigateToOverview(owner, repo)
            }
        )
    }
}

@Composable
fun AddRepoDialog(
    onDismiss: () -> Unit,
    onSubmit: (owner: String, repo: String) -> Unit
) {
    var repoUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add New Repository",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Enter a GitHub repository URL or owner/repo format",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = repoUrl,
                    onValueChange = {
                        repoUrl = it
                        errorMessage = null
                    },
                    label = { Text("Repository URL") },
                    placeholder = { Text("e.g., facebook/react") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = errorMessage != null
                )

                errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val repoInfo = GitHubUrlParser.parse(repoUrl)
                    if (repoInfo != null) {
                        // Add to cache (with mock data for now)
                        RepoCache.addRepo(
                            Repository(
                                owner = repoInfo.owner,
                                name = repoInfo.name,
                                fullName = repoInfo.fullName,
                                description = "Added repository",
                                language = null,
                                stars = 0
                            )
                        )
                        onSubmit(repoInfo.owner, repoInfo.name)
                    } else {
                        errorMessage = "Invalid GitHub URL. Use format: owner/repo or https://github.com/owner/repo"
                    }
                }
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RepoCard(repo: Repository, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = repo.fullName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            repo.description?.let { desc ->
                Text(
                    text = desc,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repo.language?.let { lang ->
                    Text(
                        text = lang,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Text(
                    text = "⭐ ${formatStars(repo.stars)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

private fun formatStars(stars: Int): String {
    return when {
        stars >= 1000 -> "${stars / 1000}k"
        else -> stars.toString()
    }
}