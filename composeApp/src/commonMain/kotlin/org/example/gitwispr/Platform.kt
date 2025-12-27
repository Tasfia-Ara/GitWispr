package org.example.gitwispr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform