package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

@Serializable
data class NotificationMessage(val title: String, val message: String)

object WebSocketManager {

    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    private var job: Job? = null
    private const val SERVER_IP = "10.0.2.2"

    fun start(context: Context) {
        if (job?.isActive == true) return // Already running

        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) { // reconnect loop
                try {
                    client.webSocket("ws://$SERVER_IP:8080/notify") {
                        println("Connected to WebSocket server")

                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                println("Received message: $text")
                                // Parse JSON to NotificationMessage
                                try {
                                    val notification = Json.decodeFromString<NotificationMessage>(text)
                                    showNotification(context, notification)
                                } catch (e: Exception) {
                                    println("Failed to parse notification: ${e.localizedMessage}")
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    println("WebSocket connection failed: ${e.localizedMessage}")
                    delay(3000) // Wait 3s before retrying
                }
            }
        }
    }

    fun triggerExpenseAddedNotification() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.post("http://$SERVER_IP:8080/trigger-expense-notification")
                println("Trigger request sent to server.")
            } catch (e: Exception) {
                println("Failed to send trigger request: ${e.message}")
            }
        }
    }

    private fun showNotification(context: Context, notification: NotificationMessage) {
        val builder = NotificationCompat.Builder(context, "expenses")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                println("Notification permission not granted.")
                return
            }
            notify((1000..9999).random(), builder.build())
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        println("WebSocket job cancelled.")
    }
}
