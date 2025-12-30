package org.example.gitwispr.data

data class Repository(
    val owner: String,
    val name: String,
    val fullName: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val defaultBranch: String = "main"
)