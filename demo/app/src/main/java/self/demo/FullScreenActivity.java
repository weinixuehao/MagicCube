package self.demo;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.MediaController;

import java.io.IOException;

/**
 * Created by chenlong on 16/11/4.
 */
public class FullScreenActivity extends Activity implements MediaController.MediaPlayerControl, TextureView.SurfaceTextureListener {
    private static final String TAG = FullScreenActivity.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private TextureView frameVideoView;
    private MediaController mediaController;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_layout);
        frameVideoView = (TextureView) findViewById(R.id.videoview);
        frameVideoView.setSurfaceTextureListener(this);
        frameVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaController == null) {
                    return;
                }
                if (mediaController.isShowing()) {
                    mediaController.hide();
                } else {
                    mediaController.show();
                }
            }
        });
    }

    @Override
    public void start() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer == null) {
            return -1;
        }
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.i(TAG, "onSurfaceTextureAvailable");
        Surface s = new Surface(surface);
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny));
            mediaPlayer.prepare();
            mediaPlayer.setSurface(s);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.seekTo(pos);
            mediaController = new MediaController(this);
            mediaController.setMediaPlayer(this);
            mediaController.setAnchorView(frameVideoView);
            mediaController.setEnabled(true);
            start();
            mediaController.show();
        } catch (IOException e) {
            mediaPlayer.release();
        }
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.i(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.i(TAG, "onSurfaceTextureDestroyed");
        pos = getCurrentPosition();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        surface.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Log.i(TAG, "onSurfaceTextureUpdated");
    }
}
