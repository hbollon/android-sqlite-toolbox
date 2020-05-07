package com.bcstudio.androidsqlitetoolboxexemple;

import android.os.Bundle;

import com.bcstudio.androidsqlitetoolbox.Database.Column;
import com.bcstudio.androidsqlitetoolbox.Database.DBHandler;
import com.bcstudio.androidsqlitetoolbox.Database.Table;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class DBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_b);
        Button createDb = findViewById(R.id.buttonCreateDb);
        Button addTableDb = findViewById(R.id.buttonAddTableDb);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
