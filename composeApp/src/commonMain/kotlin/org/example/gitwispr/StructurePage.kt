package org.example.gitwispr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Clean Architecture Layer Types
enum class ArchitectureLayer(val color: String, val displayName: String) {
    ENTITY("#4CAF50", "Entities"),           // Green
    USE_CASE("#2196F3", "Use Cases"),        // Blue
    GATEWAY("#FF9800", "Gateways"),          // Orange
    PRESENTER("#9C27B0", "Presenters"),      // Purple
    CONTROLLER("#F44336", "Controllers"),    // Red
    UTIL("#607D8B", "Utilities"),           // Gray
    MODEL("#00BCD4", "Models")              // Cyan
}

// External JavaScript function declarations
external fun initializeGraph()
external fun resetGraphToDefault()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StructurePage(
    owner: String,
    repo: String,
    navigator: Navigator
) {
    var isGraphInitialized by remember { mutableStateOf(false) }

    // Initialize graph when component loads
    LaunchedEffect(Unit) {
        if (!isGraphInitialized) {
            try {
                initializeGraph()
                isGraphInitialized = true
            } catch (e: Exception) {
                println("Error initializing graph: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("$owner/$repo") },
                    navigationIcon = {
                        TextButton(onClick = { navigator.navigateToLanding() }) {
                            Text("← Back", fontSize = 16.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                RepoTabBar(
                    currentTab = RepoTab.STRUCTURE,
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
        ) {
            // Reset Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Interactive Codebase Structure",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = {
                        try {
                            resetGraphToDefault()
                        } catch (e: Exception) {
                            println("Error resetting graph: ${e.message}")
                        }
                    }
                ) {
                    Text("Reset to Default")
                }
            }

            // Graph Container and Legend
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                // Graph Area - The JavaScript creates a fixed-position div
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFFAFAFA))
                ) {
                    if (!isGraphInitialized) {
                        Text(
                            text = "Loading graph...",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    }
                }

                // Legend
                Spacer(modifier = Modifier.width(16.dp))

                Card(
                    modifier = Modifier.width(200.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Legend",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        ArchitectureLayer.values().forEach { layer ->
                            LegendItem(
                                color = parseColor(layer.color),
                                label = layer.displayName
                            )
                        }
                    }
                }
            }

            // Instructions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Controls:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Single Click: Expand to show dependencies",
                        fontSize = 12.sp
                    )
                    Text(
                        text = "• Double Click: Show file description",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, fontSize = 12.sp)
    }
}

// Helper function to parse hex color
fun parseColor(hex: String): Color {
    val colorInt = hex.removePrefix("#").toLong(16)
    return Color(
        red = ((colorInt shr 16) and 0xFF) / 255f,
        green = ((colorInt shr 8) and 0xFF) / 255f,
        blue = (colorInt and 0xFF) / 255f
    )
}