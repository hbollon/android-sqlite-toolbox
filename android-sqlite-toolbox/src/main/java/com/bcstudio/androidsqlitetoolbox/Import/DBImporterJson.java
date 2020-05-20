package com.bcstudio.androidsqlitetoolbox.Import;

import android.util.Log;

import com.bcstudio.androidsqlitetoolbox.Constants;

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
        Log.d(Constants.PACKAGE_NAME, "Json file content : " + sb.toString());
        return sb.toString();
    }

    @Override
    protected void parseData(String data) {
        try {
            jsonDb = (JSONObject) (new JSONObject(data)).get(importConfig.getDatabaseName());
            Log.d(Constants.PACKAGE_NAME, "JSONObject content : " + jsonDb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            jsonDb = new JSONObject();
        }
    }
}
