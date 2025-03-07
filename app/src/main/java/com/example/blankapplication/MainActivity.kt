package com.example.blankapplication

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.blankapplication.ui.MainViewModel
import com.example.blankapplication.ui.model.Event
import com.example.blankapplication.ui.theme.BlankApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlankApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    private fun MainScreen(modifier: Modifier) {
        val viewModel: MainViewModel by viewModels()
        var url by remember { mutableStateOf("http://www.google.com") }
        var webViewReference by remember { mutableStateOf<WebView?>(null) }

        Column(modifier) {
            Row {
                TextField(value = url, onValueChange = {
                    url = it
                })
                Button(onClick = {
                    webViewReference?.loadUrl(url)
                }) {
                    Text(text = "Go")
                }
            }

            Webview(modifier, {
                webViewReference = it
                viewModel.processEvent(Event.OnWebpageLoadSuccess)
            }, { viewModel.processEvent(Event.OnWebviewReturnedError) })
        }
    }

    @Composable
    private fun Webview(
        modifier: Modifier,
        onPageLoaded: (WebView?) -> Unit,
        onError: () -> Unit,
    ) {
        AndroidView(modifier = modifier.fillMaxSize(), factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                val client = object : WebChromeClient() {
                }
                webChromeClient = client
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onPageLoaded.invoke(view)
                        view?.evaluateJavascript("alert('Hello!')") {
                            println("@@@, result: $it")
                        }
                    }

                    override fun onReceivedHttpError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        errorResponse: WebResourceResponse?
                    ) {
                        super.onReceivedHttpError(view, request, errorResponse)
                        onError.invoke()
                    }
                }
                loadUrl("https://www.google.com")

            }
        })
    }
}
