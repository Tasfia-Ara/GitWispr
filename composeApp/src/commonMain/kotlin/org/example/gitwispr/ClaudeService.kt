package org.example.gitwispr.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ClaudeRequest(
    val model: String = "claude-sonnet-4-20250514",
    val max_tokens: Int = 1024,
    val messages: List<Message>
)

@Serializable
data class Message(
    val role: String,
    val content: String
)

@Serializable
data class ClaudeResponse(
    val content: List<ContentBlock>,
    val id: String,
    val model: String,
    val role: String
)

@Serializable
data class ContentBlock(
    val type: String,
    val text: String
)

@Serializable
data class RepoOverview(
    val whatThisDoes: String,
    val architecture: String,
    val techStack: String,
    val keyDecisions: String
)

class ClaudeService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun analyzeRepository(owner: String, repo: String, apiKey: String): Result<RepoOverview> {
        return try {
            println("🔍 Analyzing repository: $owner/$repo")

            // ALWAYS use mock data for now (CORS issue in browser)
            // Once you test on Desktop, you can switch this
            println("🌐 Using mock data (browser CORS limitation)")
            delay(1500)  // Simulate API delay

            Result.success(
                RepoOverview(
                    whatThisDoes = "This repository ($owner/$repo) demonstrates a modern approach to building cross-platform applications. " +
                            "It allows developers to write code once and deploy it across multiple platforms including Android, iOS, and Web.",

                    architecture = "The project follows a layered architecture:\n\n" +
                            "• Presentation Layer: Compose UI components handling user interactions\n" +
                            "• Business Logic: Core application logic shared across all platforms\n" +
                            "• Data Layer: Repository pattern for data management\n" +
                            "• Platform-Specific: Native implementations using expect/actual pattern",

                    techStack = "Primary Technologies:\n\n" +
                            "• Language: Kotlin Multiplatform\n" +
                            "• UI Framework: Compose Multiplatform\n" +
                            "• Build System: Gradle with Kotlin DSL\n" +
                            "• Networking: Ktor Client\n" +
                            "• Serialization: kotlinx.serialization",

                    keyDecisions = "Key architectural decisions:\n\n" +
                            "• Kotlin Multiplatform: Maximize code sharing across platforms while maintaining native performance\n" +
                            "• Compose Multiplatform: Declarative UI framework for consistent experience\n" +
                            "• Expect/Actual Pattern: Platform-specific implementations when needed\n" +
                            "• Modern Coroutines: For asynchronous operations and state management"
                )
            )

            /*
            // Real API call - Enable this when testing on Desktop
            println("📡 Calling Claude API...")
            val prompt = buildAnalysisPrompt(owner, repo)

            val response: ClaudeResponse = client.post("https://api.anthropic.com/v1/messages") {
                contentType(ContentType.Application.Json)
                header("x-api-key", apiKey)
                header("anthropic-version", "2023-06-01")

                setBody(ClaudeRequest(
                    model = "claude-sonnet-4-20250514",
                    max_tokens = 1024,
                    messages = listOf(
                        Message(
                            role = "user",
                            content = prompt
                        )
                    )
                ))
            }.body()

            val overview = parseOverviewResponse(response.content.firstOrNull()?.text ?: "")
            println("✅ Successfully analyzed repository")
            Result.success(overview)
            */

        } catch (e: Exception) {
            println("❌ Claude API error: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun buildAnalysisPrompt(owner: String, repo: String): String {
        return """
            Analyze the GitHub repository: $owner/$repo
            
            Provide a structured overview with exactly 4 sections:
            
            1. WHAT THIS DOES (2-3 sentences in plain English, avoid marketing speak)
            2. ARCHITECTURE (3-5 main components/layers and how they connect)
            3. TECH STACK (Languages, frameworks, key libraries used)
            4. KEY DECISIONS (Why certain technologies/patterns were chosen)
            
            Format your response as:
            
            WHAT_THIS_DOES:
            [your answer]
            
            ARCHITECTURE:
            [your answer]
            
            TECH_STACK:
            [your answer]
            
            KEY_DECISIONS:
            [your answer]
            
            Keep each section concise (under 100 words).
        """.trimIndent()
    }

    private fun parseOverviewResponse(text: String): RepoOverview {
        val sections = text.split("\n\n")

        fun extractSection(marker: String): String {
            return sections.find { it.startsWith(marker) }
                ?.removePrefix(marker)
                ?.trim()
                ?: "Information not available"
        }

        return RepoOverview(
            whatThisDoes = extractSection("WHAT_THIS_DOES:"),
            architecture = extractSection("ARCHITECTURE:"),
            techStack = extractSection("TECH_STACK:"),
            keyDecisions = extractSection("KEY_DECISIONS:")
        )
    }
}