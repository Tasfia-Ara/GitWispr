package org.example.gitwispr

import androidx.compose.foundation.background
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

data class Repository(
    val owner: String,
    val name: String,
    val fullName: String,
    val description: String?,
    val language: String?,
    val stars: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListPage(
    username: String,
    token: String,
    navigator: Navigator
) {
    // Mock repositories for now
    val recentRepos = remember {
        listOf(
            Repository(
                owner = "facebook",
                name = "react",
                fullName = "facebook/react",
                description = "The library for web and native user interfaces",
                language = "JavaScript",
                stars = 230000
            ),
            Repository(
                owner = "torvalds",
                name = "linux",
                fullName = "torvalds/linux",
                description = "Linux kernel source tree",
                language = "C",
                stars = 180000
            )
        )
    }

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
                items(recentRepos) { repo ->
                    RepoCard(
                        repo = repo,
                        onClick = {
                            // TODO: Navigate to Overview screen
                            println("Clicked on ${repo.fullName}")
                        }
                    )
                }

                // Add New Repository Button
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            // TODO: Show dialog to add new repo
                            println("Add new repository clicked")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Add New Repository")
                    }
                }
            }
        }
    }
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
            modifier = Modifier
                .padding(16.dp)
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