package duancuoiky.lechihai.trochoidoantendongvat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    TextView txtTenDongVat, txtThongTin;
    Button btnTiepTheo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        txtTenDongVat = findViewById(R.id.txtTenDongVat);
        txtThongTin = findViewById(R.id.txtThongTin);
        btnTiepTheo = findViewById(R.id.btnTiepTheo);

        String ten = getIntent().getStringExtra("ten");
        String thongTin = getIntent().getStringExtra("thongTin");

        txtTenDongVat.setText(ten);
        txtThongTin.setText(thongTin);

        btnTiepTheo.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }
}