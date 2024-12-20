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
import com.google.android.exoplayer2.C
import io.flutter.plugin.platform.PlatformView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout

class PlayerView(
    context: Context, id: Int, creationParams: Map<String, Any>
) : PlatformView {

    var exoPlayer: ExoPlayer? = null
    private var playerView: PlayerView? = null
    private val view: View

    private val imgPlay: ImageView
    private val txtTimePlay: TextView
    private val txtTimeVideo: TextView
    private val progressBar: SeekBar
    private val viewProgressBar: LinearLayout
    private val viewOnTouchListener: RelativeLayout

    private val handlerHide: Handler = Handler(Looper.getMainLooper())

    init {
        // Inflate the layout and initialize the GLSurfaceView
        view = LayoutInflater.from(context).inflate(R.layout.player_view, null)
        val urlVideo = creationParams["urlVideo"] as String
        Log.d("PlayerView", "urlVideo: $urlVideo")
        playerView = view.findViewById<PlayerView>(R.id.playerView)
        playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        imgPlay = view.findViewById(R.id.img_play)
        txtTimePlay = view.findViewById(R.id.txt_time_play)
        txtTimeVideo = view.findViewById(R.id.txt_time_video)
        progressBar = view.findViewById(R.id.progress_bar)
        viewProgressBar = view.findViewById(R.id.view_progress_bar)
        viewOnTouchListener = view.findViewById(R.id.view_on_touch_listener)

        playerView?.useController = false
        exoPlayer = ExoPlayer.Builder(context).build()
        val audioAttributes = AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .setUsage(C.USAGE_MEDIA).build()
        exoPlayer?.setAudioAttributes(audioAttributes, /* handleAudioFocus= */ true)
        playerView?.setPlayer(exoPlayer)
        exoPlayer?.setMediaItem(MediaItem.fromUri(urlVideo))
        exoPlayer?.prepare()

        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    if (exoPlayer?.isPlaying == true) {
                        imgPlay.setImageResource(R.drawable.play) // Set pause icon
                        handlerHide.postDelayed({
                            viewProgressBar?.visibility = View.GONE // Ẩn view sau 3 giây
                        }, 3000)
                    } else {
                        // Video is paused
                        imgPlay.setImageResource(R.drawable.pause) // Set play icon
                        viewProgressBar?.visibility = View.VISIBLE
                        handlerHide.removeCallbacksAndMessages(null)
                    }
                }

                // Handle video end state (playback completed)
                if (playbackState == Player.STATE_ENDED) {
                    imgPlay.setImageResource(R.drawable.pause) // Reset to play icon when video ends
                    viewProgressBar?.visibility = View.VISIBLE
                }
            }
        })

        exoPlayer?.play()

        startProgressUpdater()
        imgPlay.setOnClickListener {
            togglePlayPause()
        }
        viewOnTouchListener?.setOnClickListener {
            if (viewProgressBar?.visibility == View.VISIBLE) {
                viewProgressBar?.visibility = View.GONE
                handlerHide.removeCallbacksAndMessages(null)
            } else {
                viewProgressBar?.visibility = View.VISIBLE
                handlerHide.removeCallbacksAndMessages(null)
                handlerHide.postDelayed({
                    viewProgressBar?.visibility = View.GONE // Ẩn view sau 3 giây
                }, 3000)
            }
        }

        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // Khi người dùng kéo seekbar, thực hiện seekTo() đến vị trí tương ứng
                    val newPosition = (progress * exoPlayer?.duration!! / 100)
                    exoPlayer?.seekTo(newPosition)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                // Tùy chọn: có thể dừng video khi người dùng bắt đầu kéo seekbar
//                exoPlayer?.pause()
            }

            //
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                // Tùy chọn: tiếp tục phát video sau khi người dùng đã kéo seekbar
//                exoPlayer?.play()
            }
        })
    }

    private fun startProgressUpdater() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                exoPlayer?.let {
                    // Update current time
                    val currentPosition = it.currentPosition
                    val duration = it.duration
                    val currentTimeString = formatTime(currentPosition)
                    val totalTimeString = formatTime(duration)

                    txtTimePlay.text = currentTimeString
                    txtTimeVideo.text = totalTimeString
                    progressBar.progress = (currentPosition * 100 / duration).toInt()
                }
                handler.postDelayed(this, 500) // Update every second
            }
        }
        handler.post(runnable)
    }

    // Helper function to format time in HH:mm:ss
    private fun formatTime(timeInMillis: Long): String {
        val seconds = (timeInMillis / 1000).toInt()
        val minutes = seconds / 60
        return String.format("%02d:%02d", minutes % 60, seconds % 60)
    }

    // Play/pause toggle
    fun togglePlayPause() {
        exoPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                imgPlay.setImageResource(R.drawable.pause) // Change to play icon
            } else {
                if (it.currentPosition >= it.duration) {
                    it.seekTo(0)
                    progressBar.progress = 0
                    txtTimePlay.text = formatTime(0) // Đặt thời gian hiện tại về 0
                }
                it.play()
                imgPlay.setImageResource(R.drawable.play) // Change to pause icon
            }
        }
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
