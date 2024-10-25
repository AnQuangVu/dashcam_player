package com.example.dashcam_player

import android.content.Context
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class PlayerFactory(private val plugin: DashcamPlayerPlugin) : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        val creationParams = args as? Map<String, Any> ?: emptyMap<String, Any>()
        val dashcamView = PlayerView(context, viewId, creationParams)
        plugin.setDashcamView(dashcamView) // Lưu trữ DashcamView trong plugin
        return dashcamView
    }
}