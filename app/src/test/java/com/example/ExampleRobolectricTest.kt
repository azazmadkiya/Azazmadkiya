package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Azazmadkiya", appName)
  }

  @Test
  fun `verify no internet html asset exists`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val content = context.assets.open("no_internet.html").bufferedReader().use { it.readText() }
    assertTrue(content.contains("No Internet Connection"))
    assertTrue(content.contains("Try Again"))
  }
}
