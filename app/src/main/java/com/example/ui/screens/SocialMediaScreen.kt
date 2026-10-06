package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppConstants
import com.example.model.SocialHandle
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

val socialHandlesList = listOf(
    SocialHandle(
        name = "Facebook",
        subtitle = "Open link",
        url = AppConstants.FACEBOOK_URL,
        iconRes = R.drawable.ic_facebook
    ),
    SocialHandle(
        name = "Instagram",
        subtitle = "Open link",
        url = AppConstants.INSTAGRAM_URL,
        iconRes = R.drawable.ic_instagram
    ),
    SocialHandle(
        name = "YouTube",
        subtitle = "Open link",
        url = AppConstants.YOUTUBE_URL,
        iconRes = R.drawable.ic_youtube
    ),
    SocialHandle(
        name = "Play Store",
        subtitle = "Open link",
        url = AppConstants.PLAY_STORE_URL,
        iconRes = R.drawable.ic_playstore
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialMediaScreen(
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Dark Header Banner matching reference image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Slate900)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Social Media",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Azazmadkiya",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFCBD5E1)
                    )
                }

                Row {
                    IconButton(
                        onClick = { shareSocialHandles(context) },
                        modifier = Modifier.testTag("share_social_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Handles",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier.testTag("menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Drawer",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // "Connect with us" heading matching reference image
            Text(
                text = "Connect with us",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4 Social Media Cards matching reference image layout
            socialHandlesList.forEach { handle ->
                SocialMediaCard(
                    handle = handle,
                    onClick = { openSocialLink(context, handle) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer Section matching reference image
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Azazmadkiya",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.drawer_footer_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SocialMediaCard(
    handle: SocialHandle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(onClick = onClick)
            .testTag("social_card_${handle.name.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle placeholder/icon container like reference image
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = handle.iconRes),
                    contentDescription = handle.name,
                    modifier = Modifier.size(28.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = handle.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = handle.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                contentDescription = "Open",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

fun openSocialLink(context: Context, handle: SocialHandle) {
    when (handle.name.lowercase().trim()) {
        "facebook" -> openFacebookApp(context, handle.url)
        "instagram" -> openInstagramApp(context, handle.url)
        "youtube" -> openYouTubeApp(context, handle.url)
        "play store" -> openPlayStoreApp(context, handle.url)
        else -> openDefaultBrowser(context, handle.url)
    }
}

private fun openFacebookApp(context: Context, webUrl: String) {
    try {
        val uri = Uri.parse("fb://facewebmodal/f?href=$webUrl")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.facebook.katana")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        try {
            val uri = Uri.parse("fb://facewebmodal/f?href=$webUrl")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            openDefaultBrowser(context, webUrl)
        }
    }
}

private fun openInstagramApp(context: Context, webUrl: String) {
    try {
        val uri = Uri.parse("http://instagram.com/_u/azazmadkiya")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.instagram.android")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        try {
            val uri = Uri.parse(webUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.instagram.android")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            openDefaultBrowser(context, webUrl)
        }
    }
}

private fun openYouTubeApp(context: Context, webUrl: String) {
    try {
        val uri = Uri.parse(webUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.youtube")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        openDefaultBrowser(context, webUrl)
    }
}

private fun openPlayStoreApp(context: Context, webUrl: String) {
    try {
        val marketUri = Uri.parse("market://dev?id=6037153291115945066")
        val intent = Intent(Intent.ACTION_VIEW, marketUri).apply {
            setPackage("com.android.vending")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        try {
            val uri = Uri.parse(webUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.android.vending")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            openDefaultBrowser(context, webUrl)
        }
    }
}

private fun openDefaultBrowser(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
    }
}

fun shareSocialHandles(context: Context) {
    val shareContent = buildString {
        append("Connect with Azazmadkiya:\n\n")
        append("🌐 Website: ${AppConstants.BLOG_URL}\n")
        append("📘 Facebook: ${AppConstants.FACEBOOK_URL}\n")
        append("📸 Instagram: ${AppConstants.INSTAGRAM_URL}\n")
        append("▶️ YouTube: ${AppConstants.YOUTUBE_URL}\n")
        append("📱 Play Store: ${AppConstants.PLAY_STORE_URL}\n")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Azazmadkiya Social Media Handles")
        putExtra(Intent.EXTRA_TEXT, shareContent)
    }
    context.startActivity(Intent.createChooser(intent, "Share Azazmadkiya Links"))
}
