package duancuoiky.lechihai.trochoidoantendongvat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EndGameActivity extends AppCompatActivity {

    TextView txtDiemDatDuoc, txtThongBaoKyLuc;
    Button btnChoiLai, btnVeMenu;
    DatabaseReference database;
    int score = 0;

    MediaPlayer soundGameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        txtDiemDatDuoc = findViewById(R.id.txtDiemDatDuoc);
        txtThongBaoKyLuc = findViewById(R.id.txtThongBaoKyLuc);
        btnChoiLai = findViewById(R.id.btnChoiLai);
        btnVeMenu = findViewById(R.id.btnVeMenu);

        database = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        boolean isSoundOn = sharedPreferences.getBoolean("sound_on", true);

        // Phát âm thanh khi vào màn hình kết thúc game
        soundGameOver = MediaPlayer.create(this, R.raw.amthanhendgame);
        if (soundGameOver != null && isSoundOn) {
            soundGameOver.start();
        }

        // Nhận điểm từ GameActivity
        score = getIntent().getIntExtra("score", 0);
        txtDiemDatDuoc.setText("Điểm của bạn: " + score);

        kiemTraVaCapNhatKyLuc();

        btnChoiLai.setOnClickListener(v -> {
            Intent intent = new Intent(EndGameActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });

        btnVeMenu.setOnClickListener(v -> {
            Intent intent = new Intent(EndGameActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void kiemTraVaCapNhatKyLuc() {
        database.child("record").child("highScore").get().addOnSuccessListener(snapshot -> {
            int highScore = 0;

            if (snapshot.exists()) {
                Integer value = snapshot.getValue(Integer.class);
                if (value != null) {
                    highScore = value;
                }
            }

            if (score > highScore) {
                database.child("record").child("highScore").setValue(score);
                txtThongBaoKyLuc.setText("CHÚC MỪNG! KỶ LỤC MỚI!");
                txtThongBaoKyLuc.setTextColor(android.graphics.Color.RED);
            } else {
                txtThongBaoKyLuc.setText("Kỷ lục hiện tại: " + highScore);
                txtThongBaoKyLuc.setTextColor(android.graphics.Color.BLACK);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (soundGameOver != null) {
            soundGameOver.release();
            soundGameOver = null;
        }
    }
}