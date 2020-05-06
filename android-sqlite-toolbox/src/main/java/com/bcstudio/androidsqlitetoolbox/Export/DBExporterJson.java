package com.bcstudio.androidsqlitetoolbox.Export;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBExporterJson extends DBExporter{
    private JSONObject jsonRoot;
    private JSONObject jsonDB;
    private JSONObject row;
    private JSONArray table;


    public DBExporterJson(ExportConfig config) {
        super(config);
        jsonRoot = new JSONObject();
    }

    @Override
    protected void prepairExport(String dbName) throws Exception {
        jsonDB = new JSONObject();
        jsonRoot.put(dbName, jsonDB);
    }

    @Override
    protected String getExportAsString() throws Exception {
        return jsonRoot.toString(1);
    }

    @Override
    protected void startTable(String tableName) throws Exception {
        table = new JSONArray();
        jsonDB.put(tableName, table);
    }

    @Override
    protected void endTable() throws Exception {

    }

    @Override
    protected void endRow() throws Exception {
        table.put(row);
    }

    @Override
    protected void populateRowWithField(String columnName, String string) throws Exception {
        row.put(columnName, string);
    }

    @Override
    protected void startRow() throws Exception {
        row = new JSONObject();
    }
}
