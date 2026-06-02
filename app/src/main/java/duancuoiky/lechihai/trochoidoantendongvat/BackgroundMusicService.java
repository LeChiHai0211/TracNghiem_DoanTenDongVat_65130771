package duancuoiky.lechihai.trochoidoantendongvat;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BackgroundMusicService extends Service {
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static final String ACTION_PAUSE = "PAUSE_MUSIC";
    public static final String ACTION_RESUME = "RESUME_MUSIC";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.amthanhchaynen);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(0.3f, 0.3f);
        }

        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (action.equals(ACTION_PAUSE)) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            } else if (action.equals(ACTION_RESUME)) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        } else {
            // Mặc định khởi chạy nhạc nếu không có action đặc biệt
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
        
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
