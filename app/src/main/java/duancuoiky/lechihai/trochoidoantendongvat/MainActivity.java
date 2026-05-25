package duancuoiky.lechihai.trochoidoantendongvat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnBatDau, btnKyLuc, btnThoat;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBatDau = findViewById(R.id.btnBatDau);
        btnKyLuc = findViewById(R.id.btnKyLuc);
        btnThoat = findViewById(R.id.btnThoat);

        database = FirebaseDatabase.getInstance().getReference();

        btnBatDau.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
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
}