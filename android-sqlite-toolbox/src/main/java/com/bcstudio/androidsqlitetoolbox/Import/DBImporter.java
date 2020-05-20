package com.bcstudio.androidsqlitetoolbox.Import;

import android.util.Pair;

import com.bcstudio.androidsqlitetoolbox.Database.DBHandler;
import com.bcstudio.androidsqlitetoolbox.Database.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

public abstract class DBImporter {
    protected final DBHandler db;
    protected final File srcFile;
    protected final ImportConfig importConfig;

    protected String parsedData;
    protected JSONObject jsonDb;

    DBImporter(ImportConfig importConfig) throws FileNotFoundException {
        this.importConfig = importConfig;
        this.db = importConfig.db;
        this.srcFile = importConfig.srcFile;

        BufferedReader reader = new BufferedReader(new FileReader(srcFile));
        parseData(readFile(reader));
    }

    protected abstract void parseData(String data);

    protected abstract String readFile(BufferedReader bf);

    public ArrayList<Pair<String, JSONArray>> getTables() throws JSONException {
        Iterator<String> keys = jsonDb.keys();
        ArrayList<Pair<String, JSONArray>> tables = new ArrayList<>();
        while(keys.hasNext()) {
            String key = keys.next();
            tables.add(new Pair<>(key, jsonDb.getJSONArray(key)));
        }
        return tables;
    }

    public JSONArray getTable(String tableName) throws JSONException {
        return jsonDb.getJSONArray(tableName);
    }

    public void restore() throws JSONException {
        ArrayList<Pair<String, JSONArray>> tables = getTables();
        for(int i = 0; i<tables.size(); i++){

            if(!importConfig.isExcludeTable(tables.get(i).first)){
                if(db.getTableIndexFromName(tables.get(i).first) != -1){

                    LinkedHashSet<Data> tableData = new LinkedHashSet<>();
                    for(int j = 0; j < tables.get(i).second.length(); j++) {
                        JSONObject row = tables.get(i).second.getJSONObject(j);
                        Iterator<String> keys = row.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            tableData.add(new Data(key, row.getString(key)));
                        }
                        db.addDataInTable(tables.get(i).first, tableData);
                    }
                }
            }
        }

    }




}
