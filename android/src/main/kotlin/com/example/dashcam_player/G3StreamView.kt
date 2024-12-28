package com.example.dashcam_player

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.widget.TextView
import io.flutter.plugin.platform.PlatformView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.io.ByteArrayOutputStream
import java.net.URI
import java.nio.ByteBuffer
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.Executors
import java.util.zip.Inflater
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import android.widget.FrameLayout
import android.graphics.Rect


class G3StreamView(
    context: Context,
    id: Int,
    creationParams: Map<String, Any>,
    val metaDataInStream: SharedMetaData?,
) : PlatformView {
    private var textureView: TextureView? = null
    private var webSocketClient: WebSocketClient? = null
    private var queueFrame: Queue<ByteArray> = LinkedList<ByteArray>()
    private var startStream: Boolean = false
    private var view: View
    private val paint = Paint()
    val frameExecutor = Executors.newSingleThreadExecutor()
    private var currentMessage: String? = ""
    private var metadataView: TextView? = null

    init {
        view = LayoutInflater.from(context).inflate(R.layout.g3_stream_view, null)
        textureView = view.findViewById(R.id.textureView)
        textureView?.let { textureView ->
            textureView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }
        metadataView = view.findViewById(R.id.meta_data)
        initWebSocket()
        showView()
    }

    fun initWebSocket() {
        // URI của WebSocket server (thay <server-ip> và <port> bằng địa chỉ IP và cổng của server)
        val uri: URI
        try {
            uri =
                URI("ws://192.168.43.1:9090") // Thay <server-ip> bằng địa chỉ IP của thiết bị server
        } catch (e: Exception) {
            Log.d("WebSocket", "Error: ${e.message}")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            webSocketClient = object : WebSocketClient(uri) {
                override fun onOpen(handshakedata: ServerHandshake?) {}

                override fun onMessage(message: String?) {
                    if (message != currentMessage) {
                        currentMessage = message
                        metaDataInStream?.metaData = message
                    }
                }

                override fun onMessage(bytes: ByteBuffer?) {
                    if (bytes != null && bytes.hasRemaining()) {
                        startStream = true
                        val nv21 = bytes.array()
                        queueFrame.add(nv21)
                        frameExecutor.execute {
                            if (webSocketClient?.isOpen == true) {
                                webSocketClient?.send("Next frame please")
                            } else {
                                Log.d("WebSocket", "Connection is not open")
                            }
                        }

                    }
                }

                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                    println("Disconnected from server $code $reason")
                }

                override fun onError(ex: Exception) {
                    Log.d("WebSocket", "Error: ${ex.message}")
                }
            }
            webSocketClient?.connect()
        }
    }

    fun showView() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (startStream) break
                delay(2000)
            }
            while (true) {
                if (queueFrame.isNotEmpty()) {
                    val nv21 = queueFrame.poll()
                    if (nv21 != null) {
                        displayFrame(decompressData(nv21))
                        CoroutineScope(Dispatchers.Main).launch {
                            metadataView?.text = currentMessage?.split(": ")?.first()
                        }
                        frameExecutor.execute {
                            if (webSocketClient?.isOpen == true) {
                                webSocketClient?.send("Next frame please")
                            }
                        }
                        delay(25)
                    }
                }
            }
        }
    }


    fun displayFrame(nv21: ByteArray) {
        try {
            // Decode the byte array to a Bitmap
            val bitmap = BitmapFactory.decodeByteArray(nv21, 0, nv21.size)
            if (bitmap != null && !bitmap.isRecycled) {
                // Lock the canvas for drawing
                val canvas = textureView?.lockCanvas()
                canvas?.let {
                    // Define the destination rectangle for full-screen rendering
                    val destRect = Rect(0, 0, it.width, it.height)
                    // Draw the Bitmap onto the canvas
                    it.drawBitmap(bitmap, null, destRect, null)
                    // Post the canvas
                    textureView?.unlockCanvasAndPost(it)
                }
            } else {
                Log.w("WebSocket", "Bitmap is null or recycled")
            }
        } catch (e: Exception) {
            Log.e("WebSocket", "Error displaying frame: ${e.message}", e)
        }
    }

    private fun decompressData(compressedData: ByteArray): ByteArray {
        val inflater = Inflater()
        inflater.setInput(compressedData)
        val outputStream = ByteArrayOutputStream(compressedData.size)
        val buffer = ByteArray(1024)
        try {
            while (!inflater.finished()) {
                val count = inflater.inflate(buffer)
                outputStream.write(buffer, 0, count)
            }
            outputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return outputStream.toByteArray()
    }


    override fun getView(): View {
        return view
    }

    override fun dispose() {
        queueFrame.clear()
    }

}