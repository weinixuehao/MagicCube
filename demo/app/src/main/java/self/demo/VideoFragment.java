package self.demo;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import self.demo.view.video.RoundedTextureView;

/**
 * 视屏播放的UI处理逻辑
 * 请先熟悉fragment和mediaplayer的生命周期，然后再修改此类。
 *
 */
public class VideoFragment extends VisibleHitFragment {
    private static final String TAG = VideoFragment.class.getSimpleName();
    private RoundedTextureView roundedTextureView;
    private MediaPlayer mMediaPlayer;
    private SurfaceTexture mSurfaceTexture;
    private int redius;
    private Surface surface;
    //    private MediaController mediaController;

    public static VideoFragment newInstance(int radius) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putInt("radius", radius);
        videoFragment.setArguments(args);
        return videoFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause ");
        if (isPlaying()) {
            pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume ");
        if (isVisibleView() && !isPlaying()) {
            start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView ");
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        if (surface != null) {
            surface.release();
            surface = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
        }
        roundedTextureView = null;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redius = getArguments().getInt("radius");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView " + redius);
        final View contentView = inflater.inflate(R.layout.video_layout, container, false);
        roundedTextureView = (RoundedTextureView) contentView.findViewById(R.id.roundedTextureview);
        roundedTextureView.setCornerRadius(redius);
        //media player and media controller的初始化操作。
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(
                    container.getContext(),
                    Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.big_buck_bunny));
            mMediaPlayer.prepare();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaController = new MediaController(getContext());
//            mediaController.setMediaPlayer(VideoFragment.this);
//            mediaController.setAnchorView(roundedTextureView);
//            mediaController.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
            mMediaPlayer.release();
        }
        //圆角video控件，设置surface。
        roundedTextureView.setSurfaceProvider(new RoundedTextureView.SurfaceProvider() {
            @Override
            public void onSurfaceCreated(SurfaceTexture surfaceTexture) {
                Log.i(TAG, "onSurfaceCreated " + redius);
                mSurfaceTexture = surfaceTexture;
                surface = new Surface(surfaceTexture);
                mMediaPlayer.setSurface(surface);
//                mMediaPlayer.start();
            }
        });

        //进入全屏播放
        contentView.findViewById(R.id.full_screen_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FullScreenActivity.class);
                startActivity(intent);
            }
        });

        //控制meida controller bar的显示和隐藏。show显示三秒钟自动消失。
//        contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mediaController == null) {
//                    return;
//                }
//                if (mediaController.isShowing()) {
//                    mediaController.hide();
//                } else {
//                    mediaController.show();
//                }
//            }
//        });
        return contentView;
    }

    //此方法主要在fragment对用户可见的时候调用，一般在这里适合做数据的加载等。所以播放实际播放也是在这，此方法是我们自己实现的。
    //详细看基类的实现。
    @Override
    protected void onVisibleView(boolean visible) {
        Log.i(TAG, "visible " + visible + " position:" + redius);
        if (visible) {
            start();
//            if (!mediaController.isShowing()) {
//                mediaController.show();
//            }
        } else {
            pause();
//            if (mediaController.isShowing()) {
//                mediaController.hide();
//            }
        }
    }

    /**
     * 以下都是MediaPlayerControl都实现，用于通过mediacontroller(媒体操作条)控制视频的播放暂停等操作。
     */
//    @Override
    public void start() {
        mMediaPlayer.start();
    }
//    @Override
    public void pause() {
        mMediaPlayer.pause();
    }
//
//    @Override
//    public int getDuration() {
//        return mMediaPlayer.getDuration();
//    }
//
//    @Override
//    public int getCurrentPosition() {
//        return mMediaPlayer.getCurrentPosition();
//    }
//
//    @Override
//    public void seekTo(int pos) {
//        mMediaPlayer.seekTo(pos);
//    }
//
//    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }
//
//    @Override
//    public int getBufferPercentage() {
//        return 0;
//    }
//
//    @Override
//    public boolean canPause() {
//        return true;
//    }
//
//    @Override
//    public boolean canSeekBackward() {
//        return true;
//    }
//
//    @Override
//    public boolean canSeekForward() {
//        return true;
//    }
//
//    @Override
//    public int getAudioSessionId() {
//        return 0;
//    }
}
