package com.example.dashcam_player

import android.content.Context
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class G3StreamFactory(val metaDataInStream: SharedMetaData): PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        val creationParams = args as? Map<String, Any> ?: emptyMap<String, Any>()
        val streamView = G3StreamView(context, viewId, creationParams, metaDataInStream)
        return streamView
    }
}