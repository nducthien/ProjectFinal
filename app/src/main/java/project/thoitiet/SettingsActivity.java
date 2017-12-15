package project.thoitiet;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    CheckBox ckbAgree;
    RadioButton rbC;
    RadioButton rbF;
    RadioGroup rgMa;
    RatingBar rbRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ckbAgree = findViewById(R.id.ckbAgree);
        rbC = findViewById(R.id.rbC);
        rbF = findViewById(R.id.rbF);
        rbRating = findViewById(R.id.rbRating);

        rgMa = findViewById(R.id.rgMa);
        rgMa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rbC:
                        Toast.makeText(getBaseContext(), "Bạn chọn °C", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbF:
                        Toast.makeText(getBaseContext(), "Bạn chọn °F", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
