package com.bcstudio.androidsqlitetoolboxexemple;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bcstudio.androidsqlitetoolbox.Constants;
import com.bcstudio.androidsqlitetoolbox.Database.Column;
import com.bcstudio.androidsqlitetoolbox.Database.DBHandler;
import com.bcstudio.androidsqlitetoolbox.Database.Data;
import com.bcstudio.androidsqlitetoolbox.Http.FileUploadService;

import okhttp3.ResponseBody;
import retrofit2.Call;

import java.io.FileNotFoundException;

public class FirstFragment extends Fragment {
    private DBHandler db;
    private String DEV_TEST_DB = "DEV_TEST_DB";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DBHandler(getContext(), DEV_TEST_DB, null, 1);
        db.addTable("Exemple1", new Column("Col1", "text"), new Column("Col2", "text"), new Column("Col3", "text"));
        db.addTable("Exemple2", new Column("Col1", "text"), new Column("Col2", "text"), new Column("Col3", "text"));
        db.addTable("Exemple3", new Column("Col1", "text"), new Column("Col2", "text"), new Column("Col3", "text"));

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        view.findViewById(R.id.buttonCreateDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.isTableEmpty("Exemple1") || db.isTableEmpty("Exemple2") || db.isTableEmpty("Exemple3")) {
                    db.deleteAllDataFrom("Exemple1");
                    db.deleteAllDataFrom("Exemple2");
                    db.deleteAllDataFrom("Exemple3");

                    db.addDataInTable("Exemple1", new Data("Col1", "Col1Data1"),
                            new Data("Col2", "Col2Data1"),
                            new Data("Col3", "Col3Data1"));
                    db.addDataInTable("Exemple1", new Data("Col1", "Col1Data1"),
                            new Data("Col2", "Col2Data1"),
                            new Data("Col3", "Col3Data1"));
                    db.addDataInTable("Exemple1", new Data("Col1", "Col1Data2"),
                            new Data("Col2", "Col2Data2"),
                            new Data("Col3", "Col3Data2"));

                    db.addDataInTable("Exemple2", new Data("Col1", "Col1Data1"),
                            new Data("Col2", "Col2Data1"),
                            new Data("Col3", "Col3Data1"));
                    db.addDataInTable("Exemple2", new Data("Col1", "Col1Data1"),
                            new Data("Col2", "Col2Data1"),
                            new Data("Col3", "Col3Data1"));
                    db.addDataInTable("Exemple2", new Data("Col1", "Col1Data2"),
                            new Data("Col2", "Col2Data2"),
                            new Data("Col3", "Col3Data2"));

                    db.addDataInTable("Exemple3", new Data("Col1", "Col1Data1"),
                            new Data("Col2", "Col2Data1"),
                            new Data("Col3", "Col3Data1"));
                    db.addDataInTable("Exemple3", new Data("Col1", "Col1Data1"),
                            new Data("Col2", "Col2Data1"),
                            new Data("Col3", "Col3Data1"));
                    db.addDataInTable("Exemple3", new Data("Col1", "Col1Data2"),
                            new Data("Col2", "Col2Data2"),
                            new Data("Col3", "Col3Data2"));
                }

                /*db.deleteAllDataFrom("Exemple1");
                db.deleteRow("Exemple2", 6);
                db.deleteRowWhere("Exemple2", new Data("Col1", "Col1Data1"));
                db.updateData("Exemple3", 6, new Data("Col2", "testUpdate"), new Data("Col3", "testUpdate2"));*/

                /*Cursor cr = db.getMultipleDataFromTable("Exemple1", "Col1", "Col3");
                Log.d(Constants.PACKAGE_NAME, "-> db.getMultipleDataFromTable(\"Exemple1\", \"Col1\", \"Col3\"");
                while(cr.moveToNext()){
                    for (int i=0; i<cr.getColumnCount(); i++)
                        Log.d(Constants.PACKAGE_NAME, cr.getString(i));
                }

                cr = db.getMultipleDataFromTableWhere("Exemple1", "Col1=\"Col1Data1\"", "Col1", "Col3");
                Log.d(Constants.PACKAGE_NAME, "-> db.getMultipleDataFromTableWhere(\"Exemple1\", \"Col1=\"Col1Data1\", \"Col1\", \"Col3\"");
                while(cr.moveToNext()){
                    for (int i=0; i<cr.getColumnCount(); i++)
                        Log.d(Constants.PACKAGE_NAME, cr.getString(i));
                }*/
            }
        });
        view.findViewById(R.id.buttonAddTableDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db == null)
                    db = new DBHandler(getContext(), DEV_TEST_DB, null, 1);
                db.addTable("Test", new Column("Col1", "text"), new Column("Col2", "text"));
            }
        });
        view.findViewById(R.id.buttonSyncDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db == null)
                    db = new DBHandler(getContext(), DEV_TEST_DB, null, 1){
                        // Enable to custom sync request
                        @Override
                        public Call<ResponseBody> requestBuilder(String jsonData) {
                            return super.requestBuilder(jsonData);
                        }
                    };
                try {
                    db.syncDb(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        view.findViewById(R.id.buttonDeleteData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db == null)
                    db = new DBHandler(getContext(), DEV_TEST_DB, null, 1);
                db.deleteAllDataFrom("Exemple1");
                db.deleteAllDataFrom("Exemple2");
                db.deleteAllDataFrom("Exemple3");
            }
        });
    }
}
