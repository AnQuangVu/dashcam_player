package com.example.dashcam_player

import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.C
import java.io.File
import io.flutter.plugin.common.MethodChannel.Result

/** DashcamPlayerPlugin */
class DashcamPlayerPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private var dashcamView: PlayerView? = null
  private var metaDataInStream: SharedMetaData = SharedMetaData(null)
  private var g3StreamOnlineView: G3StreamOnlineView? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "dashcam_player")
    channel.setMethodCallHandler(this)
    flutterPluginBinding.getPlatformViewRegistry().registerViewFactory("player", PlayerFactory(this))
    flutterPluginBinding.getPlatformViewRegistry().registerViewFactory("g3_stream", G3StreamFactory(metaDataInStream))
    flutterPluginBinding.getPlatformViewRegistry().registerViewFactory("g3_stream_online", G3StreamOnlineFactory(this))
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "pauseVideo") {
        val exoPlayer = getExoPlayer()
        exoPlayer?.pause()
        result.success(null)
    } else if (call.method == "playVideo") {
        val exoPlayer = getExoPlayer()
        exoPlayer?.play()
        result.success(null)
    } else if (call.method == "seekTo") {
      val position = call.argument<Double>("position") // Giá trị có thể là null
      val exoPlayer = getExoPlayer()
      if (exoPlayer != null && position != null) {
        val duration = exoPlayer.duration
        if (duration > 0) {
          val seekPosition = (position * duration) // Tính toán vị trí tua dựa trên phần trăm
          exoPlayer.seekTo(seekPosition.toLong()) // Tua đến vị trí tính toán được
        }
      }
    } else if (call.method == "replay") {
        val exoPlayer = getExoPlayer()
        exoPlayer?.seekTo(0)
        exoPlayer?.play()
        result.success(null)
    } else if(call.method == "getDuration") {
        val exoPlayer = getExoPlayer()
        val duration = exoPlayer?.duration
        if (duration != C.TIME_UNSET) { // kiểm tra nếu duration hợp lệ
            val res = (duration?.div(1000))?.toInt() // chuyển đổi sang giây
            result.success(res)
        } else {
            result.success(null)// duration không xác định
        }
    } else if (call.method == "getMetadataInStream") {
        result.success(metaDataInStream.getGPSData())
    } else if (call.method == "playNextFileInStream") {
        val path: String = call.argument("path")!!
        val uri = Uri.fromFile(File(path))
        val mediaItem = MediaItem.fromUri(uri)

        getExoPlayerG3()?.addMediaItem(mediaItem)
        getExoPlayerG3()?.prepare()
        getExoPlayerG3()?.play()
        if (g3StreamOnlineView?.statePlay == Player.STATE_ENDED) {
            Thread.sleep(500)
            getExoPlayerG3()?.seekTo(0)
            g3StreamOnlineView?.statePlay = 2
        }
        result.success(null)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
    dashcamView?.dispose()
    dashcamView = null
    g3StreamOnlineView?.dispose()
    g3StreamOnlineView = null
  }


  // Hàm để lưu trữ tham chiếu đến DashcamView
  fun setDashcamView(view: PlayerView) {
    this.dashcamView = view
  }

  // Hàm truy cập ExoPlayer
  fun getExoPlayer(): ExoPlayer? {
    return dashcamView?.exoPlayer
  }
  fun setG3StreamOnlineView(view: G3StreamOnlineView) {
    this.g3StreamOnlineView = view
  }

  fun getExoPlayerG3(): ExoPlayer? {
    return g3StreamOnlineView?.exoPlayer
  }
}
