package duancuoiky.lechihai.trochoidoantendongvat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnBatDau, btnKyLuc, btnThoat, btnAmThanh;
    DatabaseReference database;
    boolean isSoundOn = true;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBatDau = findViewById(R.id.btnBatDau);
        btnKyLuc = findViewById(R.id.btnKyLuc);
        btnThoat = findViewById(R.id.btnThoat);
        btnAmThanh = findViewById(R.id.btnAmThanh);

        sharedPreferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        isSoundOn = sharedPreferences.getBoolean("sound_on", true);
        updateSoundButtonText();

        database = FirebaseDatabase.getInstance().getReference();
        // DataHelper.importAnimalData(); // Đã nạp dữ liệu thành công, comment lại để bảo vệ dữ liệu Firebase

        btnBatDau.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        btnAmThanh.setOnClickListener(v -> {
            isSoundOn = !isSoundOn;
            sharedPreferences.edit().putBoolean("sound_on", isSoundOn).apply();
            updateSoundButtonText();
        });

        btnKyLuc.setOnClickListener(v -> {
            database.child("record").child("highScore")
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        Integer highScore = snapshot.getValue(Integer.class);

                        if (highScore == null) {
                            highScore = 0;
                        }

                        Toast.makeText(
                                MainActivity.this,
                                "Kỷ lục cao nhất: " + highScore,
                                Toast.LENGTH_LONG
                        ).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(
                                MainActivity.this,
                                "Không đọc được kỷ lục",
                                Toast.LENGTH_SHORT
                        ).show();
                    });
        });

        btnThoat.setOnClickListener(v -> finish());
    }

    private void updateSoundButtonText() {
        if (isSoundOn) {
            btnAmThanh.setText("BẬT ÂM");
        } else {
            btnAmThanh.setText("TẮT ÂM");
        }
    }
}