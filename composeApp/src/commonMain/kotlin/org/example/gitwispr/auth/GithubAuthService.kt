package org.example.gitwispr.auth

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GithubAuthService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    suspend fun exchangeCodeForToken(code: String, config: AuthConfig): Result<String>{
        return try {
            val response: TokenResponse = client.post("https://github.com/login/oauth/access_token") {
                contentType(ContentType.Application.Json)
                setBody(TokenRequest(
                    clientId = config.clientId,
                    clientSecret = config.clientSecret,
                    code = code,
                    redirectUri = config.redirectUri,
                ))
                accept(ContentType.Application.Json)
            }.body()

            Result.success(response.accessToken)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}
@Serializable
private data class TokenRequest(
    val clientId: String,
    val clientSecret: String,
    val code: String,
    val redirectUri: String
)

@Serializable
private data class TokenResponse(
    val accessToken: String,
    val token_type: String,
    val scope: String
)