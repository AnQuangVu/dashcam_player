package com.example.dashcam_player

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.OptIn
import io.flutter.plugin.platform.PlatformView

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.DefaultDataSourceFactory


@OptIn(UnstableApi::class)
class PlayerView(
    context: Context, id: Int, creationParams: Map<String, Any>
) : PlatformView {

    var exoPlayer: ExoPlayer? = null
    private var playerView: PlayerView? = null
    private val view: View

    private val handlerHide: Handler = Handler(Looper.getMainLooper())

    init {
        // Inflate the layout and initialize the GLSurfaceView
        view = LayoutInflater.from(context).inflate(R.layout.player_view, null)
        val urlVideo = creationParams["urlVideo"] as String
        playerView = view.findViewById(R.id.playerView)

        playerView?.useController = false
        exoPlayer = ExoPlayer.Builder(context).build()
        val audioAttributes = AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .setUsage(C.USAGE_MEDIA).build()
        exoPlayer?.setAudioAttributes(audioAttributes, /* handleAudioFocus= */ true)
        playerView?.setPlayer(exoPlayer)

        val dataSourceFactory = DefaultHttpDataSource.Factory()
        if (urlVideo.startsWith("http")) {
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(urlVideo))
            exoPlayer?.setMediaSource(mediaSource)
        } else {
            exoPlayer?.setMediaItem(MediaItem.fromUri(urlVideo))
        }

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
