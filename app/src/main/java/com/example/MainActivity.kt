package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.zIndex
import com.example.model.AppConstants
import com.example.model.AppScreen
import com.example.ui.components.AppDrawer
import com.example.ui.components.rememberWebViewState
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.SocialMediaScreen
import com.example.ui.theme.AzazmadkiyaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AzazmadkiyaTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.HOME) }
    val webViewState = rememberWebViewState(initialUrl = AppConstants.BLOG_URL)

    // Pause/resume webview when navigating between screens
    LaunchedEffect(currentScreen) {
        if (currentScreen == AppScreen.HOME) {
            webViewState.webViewInstance?.onResume()
        } else {
            webViewState.webViewInstance?.onPause()
        }
    }

    // Back handling priority 1: Close navigation drawer if open
    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    // Back handling priority 2: Return to Home if on secondary screens
    BackHandler(enabled = currentScreen != AppScreen.HOME && !drawerState.isOpen) {
        currentScreen = AppScreen.HOME
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            AppDrawer(
                currentScreen = currentScreen,
                onScreenSelected = { screen ->
                    currentScreen = screen
                },
                onCloseDrawer = {
                    coroutineScope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Persistent HomeScreen so WebView is retained without graphics re-init
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (currentScreen == AppScreen.HOME) Modifier
                            else Modifier.alpha(0f).zIndex(-1f)
                        )
                ) {
                    HomeScreen(
                        webViewState = webViewState,
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                }

                AnimatedVisibility(
                    visible = currentScreen == AppScreen.SOCIAL,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SocialMediaScreen(
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                }

                AnimatedVisibility(
                    visible = currentScreen == AppScreen.PRIVACY,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    PrivacyPolicyScreen(
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        onBack = {
                            currentScreen = AppScreen.HOME
                        }
                    )
                }

                AnimatedVisibility(
                    visible = currentScreen == AppScreen.ABOUT,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    AboutScreen(
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        onBack = {
                            currentScreen = AppScreen.HOME
                        }
                    )
                }
            }
        }
    }
}
