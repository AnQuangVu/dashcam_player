package com.example.dashcam_player

import androidx.annotation.NonNull
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** DashcamPlayerPlugin */
class DashcamPlayerPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private var dashcamView: PlayerView? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "dashcam_player")
    channel.setMethodCallHandler(this)
    flutterPluginBinding.getPlatformViewRegistry().registerViewFactory("player", PlayerFactory(this))
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
            result.success(null) // duration không xác định
        }
    }else
    {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
    dashcamView?.dispose()
    dashcamView = null
  }


  // Hàm để lưu trữ tham chiếu đến DashcamView
  fun setDashcamView(view: PlayerView) {
    this.dashcamView = view
  }

  // Hàm truy cập ExoPlayer
  fun getExoPlayer(): ExoPlayer? {
    return dashcamView?.exoPlayer
  }
}
