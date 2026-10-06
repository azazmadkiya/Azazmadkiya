package com.example.model

object AppConstants {
    const val BLOG_URL = "https://azazmadkiya.blogspot.com"
    const val FACEBOOK_URL = "https://facebook.com/azazmadkiya"
    const val INSTAGRAM_URL = "https://instagram.com/azazmadkiya"
    const val YOUTUBE_URL = "https://youtube.com/azazmadkiya"
    const val PLAY_STORE_URL = "https://play.google.com/store/apps/dev?id=6037153291115945066"
    const val CONTACT_EMAIL = "azazmadkiya@gmail.com"
    const val APP_VERSION = "1.0.0"
}

enum class AppScreen(val title: String) {
    HOME("Home"),
    SOCIAL("Social Media"),
    PRIVACY("Privacy Policy"),
    ABOUT("About & Contact")
}

data class SocialHandle(
    val name: String,
    val subtitle: String = "Open link",
    val url: String,
    val iconRes: Int
)
