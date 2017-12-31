package cs.android;

import android.media.MediaPlayer;

import cs.android.viewbase.CSViewController;

import static cs.java.lang.CSLang.is;

public class CSMediaPlayer extends CSViewController {

    private MediaPlayer _mediaPlayer;

    public CSMediaPlayer(CSViewController parent) {
        super(parent);
    }

    public void play(int resource) {
        if (_mediaPlayer != null) {
            stop();
            reset();
            release();
        }
        _mediaPlayer = MediaPlayer.create(context(), resource);
        _mediaPlayer.start();
    }

    public void stop() {
        try {
            _mediaPlayer.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            _mediaPlayer.release();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        try {
            _mediaPlayer.reset();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        super.onPause();
        if (is(_mediaPlayer)) {
            reset();
            release();
        }
    }
}
