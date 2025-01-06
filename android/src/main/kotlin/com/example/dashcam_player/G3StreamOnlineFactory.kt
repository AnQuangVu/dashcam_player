package com.example.dashcam_player

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class G3StreamOnlineFactory(private val plugin: DashcamPlayerPlugin): PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        val creationParams = args as? Map<String, Any> ?: emptyMap<String, Any>()
        val streamView = G3StreamOnlineView(context, viewId, creationParams)
        plugin.setG3StreamOnlineView(streamView)
        return streamView
    }
}