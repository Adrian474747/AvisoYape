package com.example.avisoyape

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat

// Y cambia la declaración de la clase:
class MainActivity : ComponentActivity() {

    private lateinit var tvEstado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvEstado = findViewById(R.id.tvEstado)

        findViewById<Button>(R.id.btnPermiso).setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        findViewById<Button>(R.id.btnBateria).setOnClickListener {
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                startActivity(
                    Intent(
                        Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                        Uri.parse("package:$packageName")
                    )
                )
            }
        }

        findViewById<Button>(R.id.btnProbar).setOnClickListener {
            SonidoYape.iniciarVoz(this)
            SonidoYape.reproducir(this, monto = "2.5", ignorarAntirrebote = true)
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarEstado()
    }

    private fun actualizarEstado() {
        val activo = NotificationManagerCompat.getEnabledListenerPackages(this)
            .contains(packageName)
        if (activo) {
            tvEstado.text = "✅ Activo: escuchando Yape"
            tvEstado.setTextColor(0xFF2E7D32.toInt())
        } else {
            tvEstado.text = "❌ Falta activar el acceso a notificaciones"
            tvEstado.setTextColor(0xFFC62828.toInt())
        }
    }
}