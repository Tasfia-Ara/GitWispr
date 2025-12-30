package org.example.gitwispr.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

object RepoCache {
    private val _repos: SnapshotStateList<Repository> = mutableStateListOf()

    // Public read-only access
    val repos: List<Repository>
        get() = _repos.toList()

    fun addRepo(repo: Repository) {
        // Don't add duplicates
        if (_repos.any { it.fullName == repo.fullName }) {
            println("⚠️ Repo ${repo.fullName} already exists")
            return
        }
        _repos.add(0, repo)  // Add to front (most recent first)
        println("✅ Added repo: ${repo.fullName}")
    }

    fun removeRepo(fullName: String) {
        _repos.removeAll { it.fullName == fullName }
        println("🗑️ Removed repo: $fullName")
    }

    fun getRepo(fullName: String): Repository? {
        return _repos.find { it.fullName == fullName }
    }

    fun clear() {
        _repos.clear()
        println("🧹 Cleared all repos")
    }

    // Initialize with some mock data for testing
    fun initMockData() {
        if (_repos.isEmpty()) {
            addRepo(
                Repository(
                    owner = "facebook",
                    name = "react",
                    fullName = "facebook/react",
                    description = "The library for web and native user interfaces",
                    language = "JavaScript",
                    stars = 230000
                )
            )
            addRepo(
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
    }
}