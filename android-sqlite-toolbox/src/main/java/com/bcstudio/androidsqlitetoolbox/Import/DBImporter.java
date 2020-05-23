package com.bcstudio.androidsqlitetoolbox.Import;

import android.util.Log;
import android.util.Pair;

import com.bcstudio.androidsqlitetoolbox.Constants;
import com.bcstudio.androidsqlitetoolbox.Database.DBHandler;
import com.bcstudio.androidsqlitetoolbox.Database.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

public abstract class DBImporter {
    protected final DBHandler db;
    protected final File srcFile;
    protected final ImportConfig importConfig;

    protected String parsedData;
    protected JSONObject jsonDb;

    DBImporter(ImportConfig importConfig) throws IOException {
        this.importConfig = importConfig;
        this.db = importConfig.db;
        this.srcFile = importConfig.srcFile;

        BufferedReader reader = new BufferedReader(new FileReader(srcFile));
        parseData(readFile(reader));
    }

    /**
     * Convert data to JSONObject
     * @param data File data string from readFile()
     */
    protected abstract void parseData(String data);

    /**
     * Read and build a string from a file
     * @param bf BufferedReader
     * @return File data string
     * @throws IOException
     */
    protected abstract String readFile(BufferedReader bf) throws IOException;

    protected ArrayList<Pair<String, JSONArray>> getTables() throws JSONException {
        Iterator<String> keys = jsonDb.keys();
        ArrayList<Pair<String, JSONArray>> tables = new ArrayList<>();
        while(keys.hasNext()) {
            String key = keys.next();
            tables.add(new Pair<>(key, jsonDb.getJSONArray(key)));
            Log.d(Constants.PACKAGE_NAME, "getTables() : Table -> " + key);
        }
        return tables;
    }

    protected JSONArray getTable(String tableName) throws JSONException {
        return jsonDb.getJSONArray(tableName);
    }

    /**
     * Import data to db from file
     * Ignore id, created_at and update_at fields and add new data after the rest
     * @throws JSONException
     */
    public void importData() throws JSONException {
        ArrayList<Pair<String, JSONArray>> tables = getTables();
        for (int i = 0; i < tables.size(); i++) {
            Log.d(Constants.PACKAGE_NAME, "importData() : table -> " + tables.get(i).first);
            importConfig.setExcludeField("id");
            importConfig.setExcludeField("created_at");
            importConfig.setExcludeField("updated_at");
            if (!importConfig.isExcludeTable(tables.get(i).first)) {
                Log.d(Constants.PACKAGE_NAME, "importData() : table -> " + tables.get(i).first + " is not excluded from import, continuing...");
                if (db.getTableIndexFromName(tables.get(i).first) != -1) {
                    Log.d(Constants.PACKAGE_NAME, "importData() : table -> " + tables.get(i).first + " exist in db, continuing...");
                    LinkedHashSet<Data> tableData = new LinkedHashSet<>();
                    for (int j = 0; j < tables.get(i).second.length(); j++) {
                        JSONObject row = tables.get(i).second.getJSONObject(j);
                        Iterator<String> keys = row.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            if(!importConfig.isExcludeField(key)) {
                                tableData.add(new Data(key, row.getString(key)));
                                Log.d(Constants.PACKAGE_NAME, "importData() : Colomn -> " + key + " Value -> " + row.getString(key));
                            } else Log.d(Constants.PACKAGE_NAME, "importData() : Colomn -> " + key + " Ignore : field exclude");
                        }
                        db.addDataInTable(tables.get(i).first, tableData);
                    }
                }
            }
        }
    }

    /**
     * Restore data to db from file
     * Keep id, created_at and updated_at fields (so it will don't add data with already taken id or it need a fresh database)
     * @throws JSONException
     */
    public void restore() throws JSONException {
        ArrayList<Pair<String, JSONArray>> tables = getTables();
        for(int i = 0; i<tables.size(); i++){
            Log.d(Constants.PACKAGE_NAME, "restore() : table -> " + tables.get(i).first);
            if(!importConfig.isExcludeTable(tables.get(i).first)){
                Log.d(Constants.PACKAGE_NAME, "restore() : table -> " + tables.get(i).first + " is not excluded from import, continuing...");
                if(db.getTableIndexFromName(tables.get(i).first) != -1){
                    Log.d(Constants.PACKAGE_NAME, "restore() : table -> " + tables.get(i).first + " exist in db, continuing...");
                    LinkedHashSet<Data> tableData = new LinkedHashSet<>();
                    for(int j = 0; j < tables.get(i).second.length(); j++) {
                        JSONObject row = tables.get(i).second.getJSONObject(j);
                        Iterator<String> keys = row.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            if(!importConfig.isExcludeField(key)
                                    || (key.trim().toUpperCase().equals("ID")
                                        || key.trim().toUpperCase().equals("CREATED_AT")
                                        || key.trim().toUpperCase().equals("UPDATED_AT"))) {
                                tableData.add(new Data(key, row.getString(key)));
                                Log.d(Constants.PACKAGE_NAME, "restore() : Colomn -> " + key + " Value -> " + row.getString(key));
                            }
                        }
                        db.addDataInTable(tables.get(i).first, tableData);
                    }
                }
            }
        }

    }




}
