<div align="center">

<img src="assets/logo.png" alt="Logo de Aviso Yape" width="160" />

# Aviso Yape

**Un aviso en voz alta para cada pago que recibes.**
La app suena "¡YAPE!" y te dice el monto, para que ningún pago pase desapercibido en tu negocio.

![Android](https://img.shields.io/badge/Android-7.0%2B-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF?logo=kotlin&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-24-blue)
![Estado](https://img.shields.io/badge/estado-funcional-brightgreen)
![Licencia](https://img.shields.io/badge/licencia-MIT-lightgrey)

</div>

---

## 📖 Sobre el proyecto

En un negocio con mucho movimiento, a veces la notificación de un pago por Yape no suena, o suena y nadie la escucha. **Aviso Yape** nace de ese problema real: es una app para Android que escucha las notificaciones del celular y, cuando detecta que llegó un pago, reproduce un sonido fuerte que dice **"YAPE"** y luego **lee el monto en voz alta**.

Los pagos de otras plataformas hacia Yape también se cubren, porque llegan como notificación de la propia app de Yape.

## ✨ Características

- 🔔 **Detección automática** de pagos recibidos mediante `NotificationListenerService`.
- 📣 **Audio fuerte** con "¡YAPE!", reproducido como alarma para que suene incluso con el celular en silencio.
- 🗣️ **Lectura del monto** con Text-to-Speech en español: `S/ 2.5` se escucha como *"2 soles con 50"*.
- 🔐 **Privacidad**: no lee en voz alta el código de seguridad y no usa internet.
- 🛡️ **Anti-duplicados**: evita que suene dos veces si Yape actualiza la misma notificación.
- 🔄 **Reconexión automática** del servicio si el sistema lo desconecta.
- ✅ **Pantalla de estado** que indica si el permiso está activo, con botones para configurar y probar el sonido.

## 🎬 Cómo funciona

```
Llega un pago ──► Yape publica una notificación
                          │
                          ▼
              YapeListener la intercepta
                          │
        ¿Es de Yape y dice "te envió un pago"?
                          │ sí
                          ▼
         Extrae el monto con una expresión regular
                          │
                          ▼
       Suena "¡YAPE!"  ──►  dice "2 soles con 50"
```

Ejemplo de notificación real que reconoce:

> **Confirmación de Pago**
> *Nombre A.* te envió un pago por S/ 2.5. El cód. de seguridad es: 000

| Notificación | La app dice |
|---|---|
| `S/ 1` | "YAPE… 1 sol" |
| `S/ 2.5` | "YAPE… 2 soles con 50" |
| `S/ 10` | "YAPE… 10 soles" |
| `S/ 25.80` | "YAPE… 25 soles con 80" |

## 📸 Capturas

<!-- Agrega aquí tus capturas: crea la carpeta assets/ y sube las imágenes -->
<div align="center">
  <img src="assets/pantalla-principal.png" alt="Pantalla principal" width="250" />
</div>

## 🛠️ Tecnologías

| Componente | Uso |
|---|---|
| **Kotlin** | Lenguaje principal |
| **NotificationListenerService** | Escuchar las notificaciones del sistema |
| **MediaPlayer** + `AudioAttributes.USAGE_ALARM` | Reproducir el audio "YAPE" a volumen máximo |
| **TextToSpeech** | Leer el monto en español (Perú) |
| **Regex** | Extraer el monto del texto de la notificación |
| **Android Studio** | Entorno de desarrollo |

## 📂 Estructura

```
AvisoYape/
├── app/src/main/
│   ├── java/com/example/avisoyape/
│   │   ├── MainActivity.kt      # Pantalla: estado, permisos y botón de prueba
│   │   ├── YapeListener.kt      # Servicio que detecta el pago y extrae el monto
│   │   └── SonidoYape.kt        # Audio + voz del monto
│   ├── res/raw/yape.mp3         # Audio "¡YAPE!"
│   ├── res/layout/              # Diseño de la pantalla
│   └── AndroidManifest.xml      # Declaración del servicio
└── ...
```

## 🚀 Instalación

### Opción A: desde Android Studio

1. Clona el repositorio:
   ```bash
   git clone https://github.com/TU-USUARIO/AvisoYape.git
   ```
2. Ábrelo en **Android Studio** y espera a que termine el *Gradle sync*.
3. Conecta tu celular con **Depuración USB** activada y pulsa **Run ▶**.

### Opción B: instalar el APK

1. Descarga el APK desde la sección **[Releases](../../releases)**.
2. Ábrelo en el celular y permite la instalación de apps de fuentes desconocidas.

> Si Play Protect o el Bloqueador automático de Samsung lo bloquean, es normal en apps que no vienen de Play Store: desactiva la protección temporalmente para instalar y vuelve a activarla después.

## ⚙️ Configuración inicial

1. Abre la app y pulsa **"Activar acceso a notificaciones"**. Busca *Aviso Yape* y actívalo.
   - Si aparece en gris: **Ajustes → Aplicaciones → Aviso Yape → ⋮ → Permitir ajustes restringidos**.
2. Pulsa **"Quitar restricción de batería"** y acepta.
3. Pulsa **"Probar sonido"** para confirmar que se escucha bien.

### 📱 Para que no falle en Samsung (One UI)

Para **Aviso Yape** y para **Yape**:

- **Batería → Sin restricciones** (Ajustes → Aplicaciones → *app* → Batería).
- **Apps que nunca se suspenden**: Ajustes → Batería → Límites de uso en segundo plano → agrégalas, y desactiva *Poner en suspensión las apps sin usar*.
- **Bloquear en Recientes**: ícono de la app → *Mantener abierta*.
- Modo ahorro de energía apagado y sin "No molestar" estricto.

## 🔧 Personalización

En `YapeListener.kt` puedes ajustar:

```kotlin
const val PAQUETE_YAPE = "com.bcp.innovacxion.yapeapp"   // app que se escucha

val FRASES_PAGO = listOf(                                 // frases que activan el aviso
    "te envió un pago",
    "te yapeó"
)
```

Para ver los textos reales que llegan, filtra **Logcat** por la etiqueta `AvisoYape`.

## 🔒 Privacidad

- La app **no usa internet** ni envía datos a ningún servidor.
- Todo el procesamiento ocurre **dentro del teléfono**.
- No guarda las notificaciones. Solo las lee para detectar un pago y extraer el monto.
- El código de seguridad de la notificación **no se lee en voz alta**.

## 🗺️ Próximas mejoras

- [ ] Soporte para montos con coma de miles (`S/ 1,200`).
- [ ] Servicio en primer plano para mayor estabilidad.
- [ ] Elegir el audio y el volumen desde la app.
- [ ] Historial de pagos del día con total acumulado.
- [ ] Soporte para Plin y otras billeteras.

## ⚠️ Aviso legal

Este es un proyecto personal e independiente. **No está afiliado, patrocinado ni respaldado por Yape ni por el BCP.** "Yape" es una marca de sus respectivos propietarios y se menciona solo para describir la funcionalidad de la app.

## 👤 Autor

**Adrian Anderson Castro Cuba Angeles**
 
🌐 [Portafolio](https://portafolio-adrian-43f48.web.app/) · 💼 [LinkedIn](https://www.linkedin.com/in/adri%C3%A1n-anderson-castro-cuba-angeles-0290a6282/) · 🐙 [GitHub](https://github.com/Adrian474747)
 
📧 [adriancastrocubaangeles@gmail.com](mailto:adriancastrocubaangeles@gmail.com) · 💬 [WhatsApp +51 976 136 219](https://wa.me/51976136219)
 
---
 
<div align="center">
Hecho con ☕ en Lima, Perú · Si te sirvió, deja una ⭐ al repositorio
</div>
