package org.example.gitwispr

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.gitwispr.api.ClaudeService
import org.example.gitwispr.api.RepoOverview
import org.example.gitwispr.config.getClaudeApiKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewPage(
    owner: String,
    repo: String,
    navigator: Navigator
) {
    val claudeService = remember { ClaudeService() }
    val scope = rememberCoroutineScope()

    var overview by remember { mutableStateOf<RepoOverview?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val apiKey = getClaudeApiKey()

    // Fetch overview on load
    LaunchedEffect(owner, repo) {
        isLoading = true
        errorMessage = null

        scope.launch {
            val result = claudeService.analyzeRepository(owner, repo, apiKey)
            result.fold(
                onSuccess = { data ->
                    overview = data
                    isLoading = false
                },
                onFailure = { error ->
                    errorMessage = error.message ?: "Failed to analyze repository"
                    isLoading = false
                }
            )
        }
    }

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
                    currentTab = RepoTab.OVERVIEW,
                    owner = owner,
                    repo = repo,
                    navigator = navigator
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Analyzing repository with AI...")
                    }
                }

                errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = errorMessage ?: "Unknown error",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = {
                            scope.launch {
                                isLoading = true
                                errorMessage = null
                                val result = claudeService.analyzeRepository(owner, repo, apiKey)
                                result.fold(
                                    onSuccess = { overview = it; isLoading = false },
                                    onFailure = { errorMessage = it.message; isLoading = false }
                                )
                            }
                        }) {
                            Text("Retry")
                        }
                    }
                }

                overview != null -> {
                    OverviewContent(overview = overview!!)
                }
            }
        }
    }
}

@Composable
fun OverviewContent(overview: RepoOverview) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Overview",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        OverviewSection(
            title = "What This Does",
            content = overview.whatThisDoes
        )

        Spacer(modifier = Modifier.height(20.dp))

        OverviewSection(
            title = "Architecture at a Glance",
            content = overview.architecture
        )

        Spacer(modifier = Modifier.height(20.dp))

        OverviewSection(
            title = "Tech Stack",
            content = overview.techStack
        )

        Spacer(modifier = Modifier.height(20.dp))

        OverviewSection(
            title = "Key Design Decisions",
            content = overview.keyDecisions
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun OverviewSection(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}