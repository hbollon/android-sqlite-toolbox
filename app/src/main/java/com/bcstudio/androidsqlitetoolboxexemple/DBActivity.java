package com.bcstudio.androidsqlitetoolboxexemple;

import android.database.Cursor;
import android.os.Bundle;

import com.bcstudio.androidsqlitetoolbox.Constants;
import com.bcstudio.androidsqlitetoolbox.Database.Column;
import com.bcstudio.androidsqlitetoolbox.Database.DBHandler;
import com.bcstudio.androidsqlitetoolbox.Database.Table;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
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

        String DEV_TEST_DB = "DEV_TEST_DB";
        DBHandler db = new DBHandler(this, DEV_TEST_DB, null, 1);
        db.deleteDatabase();
        db.addTable("Exemple1", new Column("Col1", "text"), new Column("Col2", "int"), new Column("Col3", "int"));
        db.addTable("Exemple2", new Column("Col1", "text"), new Column("Col2", "text"), new Column("Col3", "int"));
        db.addTable("Exemple3", new Column("Col1", "int"), new Column("Col2", "int"), new Column("Col3", "int"));
        db.addTable("Exemple4", new Column("Col1", "int"), new Column("Col2", "text"), new Column("Col3", "int"));

        Cursor cr = db.getMultipleDataFromTable("Exemple1", "Col1", "Col3");
        Log.d(Constants.PACKAGE_NAME, "-> db.getMultipleDataFromTable(DEV_TEST_DB, \"Exemple1\", \"Exemple3\")");
        while(cr.moveToNext()){
            for (int i=0; i<cr.getColumnCount(); i++)
                Log.d(Constants.PACKAGE_NAME, cr.getString(i));
        }

        cr = db.getMultipleDataFromTableWhere("Exemple1", "Col1=\"text\"", "Col1", "Col3");
        Log.d(Constants.PACKAGE_NAME, "-> db.getMultipleDataFromTable(DEV_TEST_DB, \"Col1=\\\"text\\\"\", \"Exemple1\", \"Exemple3\")");
        while(cr.moveToNext()){
            for (int i=0; i<cr.getColumnCount(); i++)
                Log.d(Constants.PACKAGE_NAME, cr.getString(i));
        }

    }

}
