package org.example.gitwispr.utils

data class RepoInfo(
    val owner: String,
    val name: String
) {
    val fullName: String
        get() = "$owner/$name"
}

object GitHubUrlParser {
    fun parse(url: String): RepoInfo? {
        // Supports formats:
        // - https://github.com/owner/repo
        // - http://github.com/owner/repo
        // - github.com/owner/repo
        // - owner/repo

        val cleanUrl = url.trim()

        // Try regex for full URL
        val urlRegex = Regex("(?:https?://)?(?:www\\.)?github\\.com/([^/]+)/([^/\\s]+)")
        urlRegex.find(cleanUrl)?.let { match ->
            return RepoInfo(
                owner = match.groupValues[1],
                name = match.groupValues[2].removeSuffix(".git")
            )
        }

        // Try simple "owner/repo" format
        val simpleRegex = Regex("^([^/]+)/([^/\\s]+)$")
        simpleRegex.find(cleanUrl)?.let { match ->
            return RepoInfo(
                owner = match.groupValues[1],
                name = match.groupValues[2].removeSuffix(".git")
            )
        }

        return null
    }
}