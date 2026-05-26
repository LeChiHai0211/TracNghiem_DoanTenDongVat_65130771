package duancuoiky.lechihai.trochoidoantendongvat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class InfoActivity extends AppCompatActivity {

    TextView txtTenDongVat, txtThongTin;
    ImageView imgAnimalInfo;
    Button btnTiepTheo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        txtTenDongVat = findViewById(R.id.txtTenDongVat);
        txtThongTin = findViewById(R.id.txtThongTin);
        imgAnimalInfo = findViewById(R.id.imgAnimalInfo);
        btnTiepTheo = findViewById(R.id.btnTiepTheo);

        String ten = getIntent().getStringExtra("ten");
        String thongTin = getIntent().getStringExtra("thongTin");
        String hinhAnh = getIntent().getStringExtra("hinhAnh");

        txtTenDongVat.setText(ten);
        txtThongTin.setText(thongTin);

        // Hiển thị ảnh nguyên vẹn trong màn hình thông tin
        if (hinhAnh != null && hinhAnh.startsWith("http")) {
            Glide.with(this)
                    .load(hinhAnh)
                    .fitCenter()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_delete)
                    .into(imgAnimalInfo);
        } else {
            int imageId = getResources().getIdentifier(hinhAnh, "drawable", getPackageName());
            Glide.with(this)
                    .load(imageId != 0 ? imageId : android.R.drawable.ic_menu_gallery)
                    .fitCenter()
                    .into(imgAnimalInfo);
        }

        btnTiepTheo.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }
}