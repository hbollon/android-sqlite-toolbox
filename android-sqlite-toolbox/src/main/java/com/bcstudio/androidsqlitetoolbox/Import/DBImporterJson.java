package com.bcstudio.androidsqlitetoolbox.Import;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

public class DBImporterJson extends DBImporter{

    public DBImporterJson(ImportConfig importConfig) throws IOException {
        super(importConfig);
    }

    @Override
    protected String readFile(BufferedReader bf) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    @Override
    protected void parseData(String data) {
        try {
            jsonDb = (JSONObject) (new JSONObject(data)).get(importConfig.getDatabaseName());
        } catch (JSONException e) {
            e.printStackTrace();
            jsonDb = new JSONObject();
        }
    }
}
