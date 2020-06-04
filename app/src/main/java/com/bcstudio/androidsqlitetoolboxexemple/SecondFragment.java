package com.bcstudio.androidsqlitetoolboxexemple;

import android.database.sqlite.SQLiteDatabase;
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
import com.bcstudio.androidsqlitetoolbox.Export.DBExporterJson;
import com.bcstudio.androidsqlitetoolbox.Export.ExportConfig;

public class SecondFragment extends Fragment {
    private DBHandler db;
    private String DEV_TEST_DB = "DEV_TEST_DB";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DBHandler(getContext(), DEV_TEST_DB, null, 1);

        view.findViewById(R.id.buttonExportDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.exportDbToCSV();
                db.exportDbToJSON();

                /*try {
                    ExportConfig config = new ExportConfig(db.openDataBase(), DEV_TEST_DB, ". json", getContext());
                    DBExporterJson exporter = new DBExporterJson(config);
                    exporter.export();

                    config = new ExportConfig(db.openDataBase(), DEV_TEST_DB, ".j son", getContext());
                    exporter = new DBExporterJson(config);
                    exporter.export();

                    config = new ExportConfig(db.openDataBase(), DEV_TEST_DB, ".js7on", getContext());
                    exporter = new DBExporterJson(config);
                    exporter.export();

                    config = new ExportConfig(db.openDataBase(), DEV_TEST_DB, ".7", getContext());
                    exporter = new DBExporterJson(config);
                    exporter.export();

                    config = new ExportConfig(db.openDataBase(), DEV_TEST_DB, ".Json", getContext());
                    exporter = new DBExporterJson(config);
                    exporter.export();

                    config = new ExportConfig(db.openDataBase(), DEV_TEST_DB, ".jsoN", getContext());
                    exporter = new DBExporterJson(config);
                    exporter.export();

                    config = new ExportConfig(db.openDataBase(), DEV_TEST_DB, " .json ", getContext());
                    exporter = new DBExporterJson(config);
                    exporter.export();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });
        view.findViewById(R.id.buttonImportDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Constants.PACKAGE_NAME, "importing db...");
                if(db.importDataFromJSON())
                    Log.d(Constants.PACKAGE_NAME, "import finish");
                else
                    Log.w(Constants.PACKAGE_NAME, "import failed");
            }
        });
    }
}
