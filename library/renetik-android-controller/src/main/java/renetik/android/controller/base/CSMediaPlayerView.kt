package renetik.android.controller.base

import android.media.MediaPlayer
import android.view.View

class CSMediaPlayerView(parent: CSActivityView<out View>) : CSActivityView<View>(parent) {

    private var mediaPlayer: MediaPlayer? = null

    fun play(resource: Int) {
        stop()
        reset()
        release()
        mediaPlayer = MediaPlayer.create(this, resource)
        mediaPlayer?.start()
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    fun release() {
        try {
            mediaPlayer?.release()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    fun reset() {
        try {
            mediaPlayer?.reset()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        reset()
        release()
    }
}
