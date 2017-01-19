package cs.android;

import android.media.MediaPlayer;

import cs.android.viewbase.CSViewController;

import static cs.java.lang.CSLang.is;

public class CSMediaPlayer extends CSViewController {

    private MediaPlayer mediaPlayer;

    public CSMediaPlayer(CSViewController parent) {
        super(parent);
    }

    public void play(int resource) {
        if (mediaPlayer != null) {
            stop();
            reset();
            release();
        }
        mediaPlayer = MediaPlayer.create(context(), resource);
        mediaPlayer.start();
    }

    public void stop() {
        try {
            mediaPlayer.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            mediaPlayer.release();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        try {
            mediaPlayer.reset();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        super.onPause();
        if (is(mediaPlayer)) {
            reset();
            release();
        }
    }
}
