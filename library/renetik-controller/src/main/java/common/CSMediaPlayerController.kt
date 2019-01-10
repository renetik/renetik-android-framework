package renetik.android.controller.common

import android.media.MediaPlayer
import android.view.View
import renetik.android.controller.base.CSViewController

class CSMediaPlayerController(parent: CSViewController<*>) : CSViewController<View>(parent) {

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
