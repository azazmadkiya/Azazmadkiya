package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.AppConstants

@Stable
class WebViewState(initialUrl: String) {
    var currentUrl by mutableStateOf(initialUrl)
    var pageTitle by mutableStateOf("Azazmadkiya")
    var isLoading by mutableStateOf(true)
    var progress by mutableFloatStateOf(0f)
    var canGoBack by mutableStateOf(false)
    var canGoForward by mutableStateOf(false)
    var hasError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var reloadKey by mutableIntStateOf(0)

    internal var webViewInstance: WebView? = null

    fun goBack() {
        webViewInstance?.let {
            if (it.canGoBack()) it.goBack()
        }
    }

    fun goForward() {
        webViewInstance?.let {
            if (it.canGoForward()) it.goForward()
        }
    }

    fun reload() {
        hasError = false
        isLoading = true
        val currentInstance = webViewInstance
        if (currentInstance != null) {
            currentInstance.reload()
        } else {
            reloadKey++
        }
    }

    fun loadUrl(url: String) {
        hasError = false
        isLoading = true
        currentUrl = url
        val currentInstance = webViewInstance
        if (currentInstance != null) {
            currentInstance.loadUrl(url)
        } else {
            reloadKey++
        }
    }

    fun recreate() {
        hasError = false
        isLoading = true
        reloadKey++
    }
}

@Composable
fun rememberWebViewState(initialUrl: String): WebViewState {
    return remember(initialUrl) { WebViewState(initialUrl) }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewContainer(
    state: WebViewState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Handle back button press when web view can navigate back
    BackHandler(enabled = state.canGoBack) {
        state.goBack()
    }

    Box(modifier = modifier.fillMaxSize()) {
        key(state.reloadKey) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("app_webview"),
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        // Enable nested scrolling and smooth scrollbars
                        isNestedScrollingEnabled = true
                        isVerticalScrollBarEnabled = true
                        isHorizontalScrollBarEnabled = false
                        overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
                        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            setSupportZoom(true)
                            builtInZoomControls = false
                            displayZoomControls = false
                            cacheMode = WebSettings.LOAD_DEFAULT
                            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                            allowFileAccess = false
                            allowContentAccess = false
                        }

                        // Bridge for custom HTML No Internet retry button
                        addJavascriptInterface(object {
                            @JavascriptInterface
                            fun retry() {
                                post {
                                    state.hasError = false
                                    state.loadUrl(AppConstants.BLOG_URL)
                                }
                            }
                        }, "AndroidBridge")

                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val targetUri = request?.url ?: return false
                                val scheme = targetUri.scheme ?: ""
                                val host = targetUri.host ?: ""

                                // Handle special external schemes
                                if (scheme == "tel" || scheme == "mailto" || scheme == "sms" ||
                                    scheme == "whatsapp" || scheme == "intent" || scheme == "market"
                                ) {
                                    openExternalIntent(ctx, targetUri)
                                    return true
                                }

                                // Keep Blogspot and Google authentication internal
                                if (host.contains("blogspot.com") || host.contains("blogger.com") ||
                                    host.contains("google.com")
                                ) {
                                    state.hasError = false
                                    return false
                                }

                                return false
                            }

                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                state.isLoading = true
                                url?.let { state.currentUrl = it }
                                state.canGoBack = view?.canGoBack() == true
                                state.canGoForward = view?.canGoForward() == true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                state.isLoading = false
                                url?.let { state.currentUrl = it }
                                view?.title?.let { state.pageTitle = it }
                                state.canGoBack = view?.canGoBack() == true
                                state.canGoForward = view?.canGoForward() == true
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                if (request?.isForMainFrame == true) {
                                    state.isLoading = false
                                    try {
                                        // Load custom local HTML No Internet error page
                                        val htmlContent = ctx.assets.open("no_internet.html")
                                            .bufferedReader()
                                            .use { it.readText() }
                                        view?.loadDataWithBaseURL(
                                            "https://azazmadkiya.blogspot.com",
                                            htmlContent,
                                            "text/html",
                                            "UTF-8",
                                            null
                                        )
                                    } catch (_: Exception) {
                                        state.hasError = true
                                        state.errorMessage = error?.description?.toString()
                                            ?: "Failed to load website. Please check your internet connection."
                                    }
                                }
                            }

                            // CRITICAL: Prevent host app process crash when Chromium renderer terminates
                            override fun onRenderProcessGone(
                                view: WebView?,
                                detail: RenderProcessGoneDetail?
                            ): Boolean {
                                val didCrash = detail?.didCrash() == true
                                (view?.parent as? ViewGroup)?.removeView(view)
                                try {
                                    view?.destroy()
                                } catch (_: Exception) {
                                }
                                state.webViewInstance = null
                                state.isLoading = false
                                state.hasError = true
                                state.errorMessage = if (didCrash) {
                                    "Web rendering was reset. Tap Retry to reload."
                                } else {
                                    "Web renderer process was closed. Tap Retry to reload."
                                }
                                return true
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                state.progress = newProgress / 100f
                                if (newProgress >= 100) {
                                    state.isLoading = false
                                }
                            }

                            override fun onReceivedTitle(view: WebView?, title: String?) {
                                super.onReceivedTitle(view, title)
                                title?.let {
                                    if (it.isNotBlank()) state.pageTitle = it
                                }
                            }
                        }

                        loadUrl(state.currentUrl)
                        state.webViewInstance = this
                    }
                },
                update = { webView ->
                    state.webViewInstance = webView
                }
            )
        }

        // Loading Progress Bar
        AnimatedVisibility(
            visible = state.isLoading && state.progress < 1f,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            LinearProgressIndicator(
                progress = { state.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )
        }

        // Fallback Error Overlay if WebView instance is completely missing
        if (state.hasError && state.webViewInstance == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudOff,
                        contentDescription = "Offline",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Unable to load page",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.errorMessage.ifBlank {
                            "Please check your network connection and try again."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { state.recreate() },
                        modifier = Modifier.testTag("retry_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Retry")
                    }
                }
            }
        }
    }

    DisposableEffect(state.reloadKey) {
        onDispose {
            state.webViewInstance?.let { webView ->
                (webView.parent as? ViewGroup)?.removeView(webView)
                try {
                    webView.destroy()
                } catch (_: Exception) {
                }
            }
            state.webViewInstance = null
        }
    }
}

private fun openExternalIntent(context: Context, uri: Uri) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        // Fallback or ignore if no app handler
    }
}
