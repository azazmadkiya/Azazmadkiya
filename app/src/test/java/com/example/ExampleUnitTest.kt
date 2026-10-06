package com.example

import com.example.model.AppConstants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun constants_areCorrect() {
        assertEquals("https://azazmadkiya.blogspot.com", AppConstants.BLOG_URL)
        assertEquals("https://facebook.com/azazmadkiya", AppConstants.FACEBOOK_URL)
        assertEquals("https://instagram.com/azazmadkiya", AppConstants.INSTAGRAM_URL)
        assertEquals("https://youtube.com/azazmadkiya", AppConstants.YOUTUBE_URL)
        assertTrue(AppConstants.PLAY_STORE_URL.contains("6037153291115945066"))
    }
}
