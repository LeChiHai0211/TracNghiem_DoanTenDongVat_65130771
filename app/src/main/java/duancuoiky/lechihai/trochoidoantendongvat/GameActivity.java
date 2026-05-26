package duancuoiky.lechihai.trochoidoantendongvat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    TextView txtDiem;
    ImageView imgAnimal;
    Button btnA, btnB, btnC, btnD, btnMenu;

    DatabaseReference database;
    ArrayList<Animal> dsAnimal = new ArrayList<>();

    int viTri = 0;
    int diem = 0;
    Animal animalHienTai;

    ActivityResultLauncher<Intent> moManHinhThongTin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        txtDiem = findViewById(R.id.txtDiem);
        imgAnimal = findViewById(R.id.imgAnimal);

        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);
        btnMenu = findViewById(R.id.btnMenu);

        database = FirebaseDatabase.getInstance().getReference();

        moManHinhThongTin = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    viTri++;
                    hienCauHoi();
                }
        );

        docDuLieuFirebase();

        btnA.setOnClickListener(v -> kiemTraDapAn(btnA));
        btnB.setOnClickListener(v -> kiemTraDapAn(btnB));
        btnC.setOnClickListener(v -> kiemTraDapAn(btnC));
        btnD.setOnClickListener(v -> kiemTraDapAn(btnD));

        btnMenu.setOnClickListener(v -> hienMenu());
    }

    private void docDuLieuFirebase() {
        database.child("animals").get()
                .addOnSuccessListener(snapshot -> {
                    dsAnimal.clear();

                    for (DataSnapshot item : snapshot.getChildren()) {
                        Animal animal = item.getValue(Animal.class);
                        if (animal != null) {
                            dsAnimal.add(animal);
                        }
                    }

                    if (dsAnimal.size() == 0) {
                        Toast.makeText(this, "Chưa có dữ liệu động vật", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Collections.shuffle(dsAnimal);

                    if (dsAnimal.size() > 20) {
                        dsAnimal = new ArrayList<>(dsAnimal.subList(0, 20));
                    }

                    viTri = 0;
                    diem = 0;
                    txtDiem.setText("Điểm: 0");

                    hienCauHoi();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi đọc Firebase", Toast.LENGTH_SHORT).show()
                );
    }

    private void hienCauHoi() {
        if (viTri >= dsAnimal.size()) {
            ketThucGame();
            return;
        }

        resetMauNut();
        batTatNut(true);

        animalHienTai = dsAnimal.get(viTri);

        // Sử dụng Glide để hiển thị ảnh nguyên vẹn (fitCenter)
        if (animalHienTai.image != null && animalHienTai.image.startsWith("http")) {
            Glide.with(this)
                    .load(animalHienTai.image)
                    .fitCenter()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_delete)
                    .into(imgAnimal);
        } else {
            int imageId = getResources().getIdentifier(
                    animalHienTai.image,
                    "drawable",
                    getPackageName()
            );
            
            Glide.with(this)
                    .load(imageId != 0 ? imageId : android.R.drawable.ic_delete)
                    .fitCenter()
                    .into(imgAnimal);
        }

        ArrayList<String> dsDapAn = new ArrayList<>();

        dsDapAn.add(animalHienTai.name);

        if (animalHienTai.wrongAnswers != null) {
            Collections.shuffle(animalHienTai.wrongAnswers);

            for (int i = 0; i < animalHienTai.wrongAnswers.size() && i < 3; i++) {
                dsDapAn.add(animalHienTai.wrongAnswers.get(i));
            }
        }

        while (dsDapAn.size() < 4) {
            dsDapAn.add("Đáp án khác");
        }

        Collections.shuffle(dsDapAn);

        btnA.setText(dsDapAn.get(0));
        btnB.setText(dsDapAn.get(1));
        btnC.setText(dsDapAn.get(2));
        btnD.setText(dsDapAn.get(3));
    }

    private void kiemTraDapAn(Button btnChon) {
        batTatNut(false);

        String dapAnChon = btnChon.getText().toString();

        if (dapAnChon.equals(animalHienTai.name)) {
            btnChon.setBackgroundColor(Color.GREEN);
            diem += 10;
            txtDiem.setText("Điểm: " + diem);

            btnChon.postDelayed(() -> {
                Intent intent = new Intent(GameActivity.this, InfoActivity.class);
                intent.putExtra("ten", animalHienTai.name);
                intent.putExtra("thongTin", animalHienTai.info);
                intent.putExtra("hinhAnh", animalHienTai.image);
                moManHinhThongTin.launch(intent);
            }, 700);

        } else {
            btnChon.setBackgroundColor(Color.RED);
            hienNutDapAnDung();

            Toast.makeText(
                    this,
                    "Sai! Đáp án đúng: " + animalHienTai.name,
                    Toast.LENGTH_SHORT
            ).show();

            btnChon.postDelayed(() -> {
                viTri++;
                hienCauHoi();
            }, 1200);
        }
    }

    private void hienNutDapAnDung() {
        if (btnA.getText().toString().equals(animalHienTai.name)) {
            btnA.setBackgroundColor(Color.GREEN);
        } else if (btnB.getText().toString().equals(animalHienTai.name)) {
            btnB.setBackgroundColor(Color.GREEN);
        } else if (btnC.getText().toString().equals(animalHienTai.name)) {
            btnC.setBackgroundColor(Color.GREEN);
        } else if (btnD.getText().toString().equals(animalHienTai.name)) {
            btnD.setBackgroundColor(Color.GREEN);
        }
    }

    private void resetMauNut() {
        btnA.setBackgroundColor(Color.LTGRAY);
        btnB.setBackgroundColor(Color.LTGRAY);
        btnC.setBackgroundColor(Color.LTGRAY);
        btnD.setBackgroundColor(Color.LTGRAY);
    }

    private void batTatNut(boolean trangThai) {
        btnA.setEnabled(trangThai);
        btnB.setEnabled(trangThai);
        btnC.setEnabled(trangThai);
        btnD.setEnabled(trangThai);
    }

    private void hienMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnMenu);

        popupMenu.getMenu().add("Chơi lại");
        popupMenu.getMenu().add("Kỷ lục");
        popupMenu.getMenu().add("Thoát");

        popupMenu.setOnMenuItemClickListener(item -> {
            String chon = item.getTitle().toString();

            if (chon.equals("Chơi lại")) {
                docDuLieuFirebase();
            } else if (chon.equals("Kỷ lục")) {
                xemKyLuc();
            } else if (chon.equals("Thoát")) {
                finish();
            }

            return true;
        });

        popupMenu.show();
    }

    private void xemKyLuc() {
        database.child("record").child("highScore")
                .get()
                .addOnSuccessListener(snapshot -> {
                    Integer highScore = snapshot.getValue(Integer.class);

                    if (highScore == null) {
                        highScore = 0;
                    }

                    Toast.makeText(this, "Kỷ lục: " + highScore, Toast.LENGTH_LONG).show();
                });
    }

    private void ketThucGame() {
        batTatNut(false);
        Intent intent = new Intent(GameActivity.this, EndGameActivity.class);
        intent.putExtra("score", diem);
        startActivity(intent);
        finish();
    }
}