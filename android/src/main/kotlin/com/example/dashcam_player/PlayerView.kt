package com.example.dashcam_player

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import com.google.android.exoplayer2.C
import io.flutter.plugin.platform.PlatformView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerView

class PlayerView(
    context: Context,
    id: Int,
    creationParams: Map<String, Any>
) : PlatformView {

    var exoPlayer: ExoPlayer? = null
    private var playerView: PlayerView? = null
    private val view: View

    init {
        // Inflate the layout and initialize the GLSurfaceView
        view = LayoutInflater.from(context).inflate(R.layout.player_view, null)
        val urlVideo = creationParams["urlVideo"] as String
        Log.d("PlayerView", "urlVideo: $urlVideo")
        playerView = view.findViewById<PlayerView>(R.id.playerView)
        playerView?.useController = false
        exoPlayer = ExoPlayer.Builder(context).build()
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .setUsage(C.USAGE_MEDIA)
            .build()
        exoPlayer?.setAudioAttributes(audioAttributes, /* handleAudioFocus= */ true)
        playerView?.setPlayer(exoPlayer)
        exoPlayer?.setMediaItem(MediaItem.fromUri(urlVideo))
        exoPlayer?.prepare()
        exoPlayer?.play()
    }

    override fun getView(): View {
        return view
    }

    override fun dispose() {
        if (exoPlayer != null) {
            exoPlayer!!.release()
            exoPlayer = null
        }
    }
}
