package com.example.avisoyape

import android.app.Notification
import android.content.ComponentName
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class YapeListener : NotificationListenerService() {

    companion object {
        const val TAG = "AvisoYape"
        const val PAQUETE_YAPE = "com.bcp.innovacxion.yapeapp"

        val FRASES_PAGO = listOf(
            "te envió un pago",
            "te envio un pago",
            "te yapeó",
            "te yapeo"
        )

        // Captura el número después de "S/": 1, 2.5, 10.50
        val REGEX_MONTO = Regex("""S/\s*([0-9]+(?:\.[0-9]+)?)""")
    }

    override fun onCreate() {
        super.onCreate()
        SonidoYape.iniciarVoz(this)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.notification.flags and Notification.FLAG_GROUP_SUMMARY != 0) return

        val extras = sbn.notification.extras
        val titulo = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: ""
        val texto = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

        Log.d(TAG, "Paquete=${sbn.packageName} | Título=$titulo | Texto=$texto")

        if (sbn.packageName != PAQUETE_YAPE) return

        val completo = "$titulo $texto".lowercase()
        if (FRASES_PAGO.any { completo.contains(it) }) {
            val monto = REGEX_MONTO.find(texto)?.groupValues?.get(1)
            Log.d(TAG, "Monto detectado: $monto")
            SonidoYape.reproducir(this, monto)
        }
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        requestRebind(ComponentName(this, YapeListener::class.java))
    }
}