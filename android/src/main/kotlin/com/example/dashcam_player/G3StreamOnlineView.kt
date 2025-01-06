package com.example.dashcam_player

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import io.flutter.plugin.platform.PlatformView
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
class G3StreamOnlineView(val context: Context, val viewId: Int, val creationParams: Map<String, Any>): PlatformView {
    private var view: View
    private var playerView: PlayerView? = null
    var exoPlayer: ExoPlayer? = null
    var statePlay = 0

    init {
        view = LayoutInflater.from(context).inflate(R.layout.g3_stream_online_view, null)
        playerView = view.findViewById(R.id.playerView)
        initExplore()
    }

    override fun getView(): View {
        return view
    }

    @UnstableApi private fun initExplore() {
        exoPlayer = ExoPlayer.Builder(context).build()
        playerView!!.setPlayer(exoPlayer)
        playerView?.useController = false
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    exoPlayer?.pause()
                    statePlay = playbackState
                }
            }
        })
        exoPlayer?.prepare()
        exoPlayer?.setForegroundMode(true)
    }

    override fun dispose() {
        exoPlayer?.release()
        exoPlayer = null
    }
}