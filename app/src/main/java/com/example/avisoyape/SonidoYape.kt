package com.example.avisoyape

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import java.util.Locale

object SonidoYape {

    private var player: MediaPlayer? = null
    private var tts: TextToSpeech? = null
    private var ultimaVez = 0L

    private val atributos = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    // Se llama una vez al iniciar el servicio, para que la voz esté lista
    fun iniciarVoz(context: Context) {
        if (tts != null) return
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("es", "PE")
                tts?.setAudioAttributes(atributos)
            }
        }
    }

    fun reproducir(
        context: Context,
        monto: String? = null,
        ignorarAntirrebote: Boolean = false
    ) {
        val ahora = System.currentTimeMillis()
        // Evita que suene 2 veces si Yape actualiza la misma notificación
        if (!ignorarAntirrebote && ahora - ultimaVez < 4000) return
        ultimaVez = ahora

        try {
            val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audio.setStreamVolume(
                AudioManager.STREAM_ALARM,
                audio.getStreamMaxVolume(AudioManager.STREAM_ALARM),
                0
            )

            player?.release()
            val mp = MediaPlayer.create(
                context.applicationContext,
                R.raw.yape,
                atributos,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )

            if (mp == null) {
                decirMonto(monto)
                return
            }

            player = mp.apply {
                setOnCompletionListener {
                    it.release()
                    if (player === it) player = null
                    decirMonto(monto)   // al terminar "YAPE", dice el monto
                }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun decirMonto(monto: String?) {
        if (monto.isNullOrBlank()) return
        tts?.speak(montoHablado(monto), TextToSpeech.QUEUE_FLUSH, null, "monto")
    }

    // "2.5" -> "2 con 50 soles" | "1" -> "1 sol" | "0.5" -> "50 céntimos"
    private fun montoHablado(monto: String): String {
        val partes = monto.split(".")
        val soles = partes[0].toIntOrNull() ?: return "$monto soles"
        val centimos = if (partes.size > 1)
            partes[1].padEnd(2, '0').take(2).toIntOrNull() ?: 0 else 0

        return when {
            soles == 0 && centimos > 0 -> "$centimos céntimos"
            centimos == 0 && soles == 1 -> "1 sol"
            centimos == 0 -> "$soles soles"
            soles == 1 -> "1 sol con $centimos"
            else -> "$soles soles con $centimos"
        }
    }
}