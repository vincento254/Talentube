package com.example.talentube.View;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import com.example.talentube.R;




import android.content.Intent;


import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private Button openproductActivityBtn,openUploadActivityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        openproductActivityBtn= findViewById ( R.id.productActivityBtn);
        openUploadActivityBtn = findViewById ( R.id.openUploadActivityBtn );

        openproductActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ItemsActivity.class);
                startActivity(i);
            }
        });
        openUploadActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(i);
            }
        });

    }


}
