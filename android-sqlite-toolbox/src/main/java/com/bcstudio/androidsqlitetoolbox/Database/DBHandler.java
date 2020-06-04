package com.bcstudio.androidsqlitetoolbox.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;
import android.util.Log;

import com.bcstudio.androidsqlitetoolbox.Constants;
import com.bcstudio.androidsqlitetoolbox.Exceptions.MissingSyncUrlException;
import com.bcstudio.androidsqlitetoolbox.Export.DBExporterCsv;
import com.bcstudio.androidsqlitetoolbox.Export.DBExporterJson;
import com.bcstudio.androidsqlitetoolbox.Export.ExportConfig;
import com.bcstudio.androidsqlitetoolbox.FileUtils;
import com.bcstudio.androidsqlitetoolbox.Http.FileUploadService;
import com.bcstudio.androidsqlitetoolbox.Http.ServiceGenerator;
import com.bcstudio.androidsqlitetoolbox.Import.DBImporterJson;
import com.bcstudio.androidsqlitetoolbox.Import.ImportConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

/**
 * Database helper class used for interact with db and table for operation like:
 * - insert data
 * - delete data
 * - select and get single or multiple data from tables with or without where arg
 * - update data
 * - create database and add tables
 * - export db
 * - sync db to remote api
 */

public class DBHandler extends SQLiteOpenHelper {
    private final String DB_NAME;

    private DatabaseErrorHandler dbErrHandler;
    private SQLiteDatabase.CursorFactory curFactory;
    private Context appContext;
    private int version;

    private ArrayList<Table> tables = new ArrayList<>();
    public ArrayList<Table> getTables() {
        return tables;
    }
    public void setTables(ArrayList<Table> tables) {
        this.tables = tables;
    }

    public DBHandler(Context context, String dbName, SQLiteDatabase.CursorFactory curFactory, int version) {
        super(context, dbName, curFactory, version);
        this.appContext = context;
        this.DB_NAME = dbName;
        this.curFactory = curFactory;
        this.version = version;

        refreshTablesSet(true);
    }

    public DBHandler(Context context, String dbName, SQLiteDatabase.CursorFactory curFactory, int version, DatabaseErrorHandler dbErrHandler) {
        super(context, dbName, curFactory, version);
        this.appContext = context;
        this.DB_NAME = dbName;
        this.curFactory = curFactory;
        this.version = version;
        this.dbErrHandler = dbErrHandler;

        refreshTablesSet(true);
    }

    /**
     * Update tables array attribute with sqlite_master data
     * Convert all existing tables to Table and Column objects and add them to tables property
     * Ignore sql tables (sqlite_sequence, android_metadata)
     *
     * @param override Allow function to override Table instance in tables if it already exist
     */
    public void refreshTablesSet(boolean override){
        Cursor c = openDataBase().rawQuery("SELECT * FROM sqlite_master WHERE type='table' AND name!='sqlite_sequence' AND name!='android_metadata'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String currentTableName = c.getString(c.getColumnIndex("name"));
                String sqlQuery = c.getString(c.getColumnIndex("sql"));

                String[] sqlQuerySplitted = sqlQuery.split("\\s+");

                ArrayList<Column> columnList = new ArrayList<>();
                String columnName = null;
                ArrayList<String> columnArgs = new ArrayList<>();

                for (int i = 4; i<sqlQuerySplitted.length-1; i++) {
                    if(columnName == null)
                        columnName = sqlQuerySplitted[i];
                    else if(!sqlQuerySplitted[i].equals(",")) {
                        columnArgs.add(sqlQuerySplitted[i]);
                        if(i == sqlQuerySplitted.length-2)
                            columnList.add(new Column(columnName, columnArgs.toArray(new String[0])));
                    }
                    else if(sqlQuerySplitted[i].equals(",")){
                        if(!columnName.equals("ID"))
                            columnList.add(new Column(columnName, columnArgs.toArray(new String[0])));
                        columnArgs.clear();
                        columnName = null;
                    }
                }

                if(getTableIndexFromName(currentTableName) != -1){
                    if(override)
                        tables.set(getTableIndexFromName(currentTableName), new Table(currentTableName, columnList.toArray(new Column[0])));
                }
                else
                    tables.add(new Table(currentTableName, columnList.toArray(new Column[0])));

                c.moveToNext();
            }
        }
        c.close();
    }

    /**
     * Add table in existing database and upgrade it
     * @param tableName Name of the new table
     * @param columns List of column
     */
    public void addTable(String tableName, Column... columns) {
        Table table = new Table(
                tableName,
                columns
        );

        if(getTableIndexFromName(tableName) != -1)
            tables.set(getTableIndexFromName(tableName), table);
        else
            tables.add(table);

        openDataBase().execSQL(table.getSql());
    }

    /**
     * Open writable db instance
     * @return Writable db instance
     */
    public synchronized SQLiteDatabase openDataBase() {
        return getWritableDatabase();
    }

    /**
     * Delete db
     * @return Success
     */
    public boolean deleteDatabase() {
        return appContext.deleteDatabase(DB_NAME);
    }

    /**
     * Delete all data from table
     * @param tableName Table name
     */
    public void deleteAllDataFrom(String tableName) {
        SQLiteDatabase db = openDataBase();
        db.execSQL("DELETE FROM " + tableName);
    }

    /**
     * Delete row from table
     * @param tableName Table name
     * @param rowIndex Row id
     * @return Success bool
     */
    public boolean deleteRow(String tableName, int rowIndex) {
        SQLiteDatabase db = openDataBase();
        return db.delete(tableName, "id = ?", new String[]{String.valueOf(rowIndex)}) == 1;
    }

    /**
     * Delete row from table with where clause
     * @param tableName Table name
     * @param data Data instance
     * @return Success bool
     */
    public boolean deleteRowWhere(String tableName, Data data) {
        SQLiteDatabase db = openDataBase();
        return db.delete(tableName, data.getColumnName() + " = ?", new String[]{String.valueOf(data.getValue())}) == 1;
    }

    /**
     * Insert Data instance into db table
     * @param tableName Table name
     * @param data Array of Data
     * @return Success bool
     */
    public boolean addDataInTable(String tableName, Data... data) {
        if(getTableIndexFromName(tableName) == -1)
            return false;

        ContentValues cv = new ContentValues();
        for (Data datum : data) {
            if (datum.getColumnName().isEmpty()) {
                return false;
            } else {
                cv.put(datum.getColumnName(), datum.getValue());
            }
        }

        long result = openDataBase().insert(tableName, null, cv);
        return result != -1;
    }

    /**
     * Insert Data instance into db table
     * @param tableName Table name
     * @param data Array of Data
     * @return Success bool
     */
    public boolean addDataInTable(String tableName, Set<Data> data) {
        if(getTableIndexFromName(tableName) == -1)
            return false;

        ContentValues cv = new ContentValues();
        for (Data datum : data) {
            if (datum.getColumnName().isEmpty()) {
                return false;
            } else {
                cv.put(datum.getColumnName(), datum.getValue());
            }
        }

        long result = openDataBase().insert(tableName, null, cv);
        return result != -1;
    }

    /**
     * Update row from table with Data array
     * @param tableName Table name
     * @param rowIndex Row id
     * @param data Data array
     * @return Success bool
     */
    public boolean updateData(String tableName, int rowIndex, Data... data) {
        ContentValues cv = new ContentValues();
        for (Data datum : data) {
            if (datum.getColumnName().isEmpty()) {
                return false;
            } else {
                cv.put(datum.getColumnName(), datum.getValue());
            }
        }
        return openDataBase().update(tableName, cv, "id = ?", new String[]{String.valueOf(rowIndex)}) > 0;
    }

    /**
     * Return Cursor pointing on first element of desired table
     *
     * @param table Table name
     * @return Cursor
     */
    public Cursor getAllDataFromTable(String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + table, null);
    }

    /**
     * Return Cursor pointing on first element of desired table with where
     *
     * @param table Table name
     * @return Cursor
     */
    public Cursor getAllDataFromTable(String table, String where)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + table + " WHERE " + where, null);
    }

    /**
     * Return Cursor pointing on first element of desired column of table
     *
     * @param table Table name
     * @param column Data name
     * @return Cursor
     */
    public Cursor getOneDataFromTable(String table, String column)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + column + " FROM " + table, null);
    }

    /**
     * Return Cursor pointing on first element of desired data of table with where parameter
     *
     * @param table Table name
     * @param column Data name
     * @param where Where constraint
     * @return Cursor
     */
    public Cursor getOneDataFromTable(String table, String column, String where)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + column + " FROM " + table + " WHERE " + where, null);
    }

    /**
     * Return Cursor pointing on first element of desired columns of table
     *
     * @param table Table name
     * @param columns Data name
     * @return Cursor
     */
    public Cursor getMultipleDataFromTable(String table, String... columns)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String catColumn = columns[0];
        for (int i = 1; i < columns.length; i++) {
            catColumn = catColumn.concat(", "+columns[i]);
        }
        Log.d(Constants.PACKAGE_NAME, "SELECT " + catColumn + " FROM " + table);
        return db.rawQuery("SELECT " + catColumn + " FROM " + table, null);
    }

    /**
     * Return Cursor pointing on first element of desired columns of table with where parameter
     *
     * @param table Table name
     * @param where Where string argument
     * @param columns Data name
     * @return Cursor
     */
    public Cursor getMultipleDataFromTableWhere(String table, String where, String... columns)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String catColumn = columns[0];
        for (int i = 1; i < columns.length; i++) {
            catColumn = catColumn.concat(", "+columns[i]);
        }
        Log.d(Constants.PACKAGE_NAME, "SELECT " + catColumn + " FROM " + table + " WHERE " + where);
        return db.rawQuery("SELECT " + catColumn + " FROM " + table + " WHERE " + where, null);
    }

    /**
     * Drop all table of the db
     */
    private void dropAllTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();

        while(c.moveToNext()) {
            tables.add(c.getString(0));
        }
        c.close();

        for(String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }
    }

    /**
     * Debug function for reset database
     * Drop all tables and re-create them
     */
    public void resetDB()
    {
        dropAllTables();
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    public String getDBName()
    {
        return DB_NAME;
    }

    /**
     * Used to get table index in tables array with his name
     * Can also be used to verify existence of the table
     * @param tableName Name of the table
     * @return Table index or -1 if not found
     */
    public int getTableIndexFromName(String tableName){
        for(int i = 0; i<tables.size(); i++){
            if(tables.get(i).getTableName().trim().equals(tableName))
                return i;
        }
        return -1;
    }

    /**
     * Return the number of elements in the table
     *
     * @param table Table name
     * @return Number of entry of table
     */
    public int getNumEntries(String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return (int)DatabaseUtils.queryNumEntries(db, table);
    }

    /**
     * Check if table in db is empty
     *
     * @param table Table instance
     * @return boolean
     */
    public boolean isTableEmpty(Table table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + table.getTableName(), null);
        boolean empty;

        empty = !mCursor.moveToFirst();
        mCursor.close();

        return empty;
    }
    /**
     * Check if table in db is empty
     *
     * @param table Table name
     * @return boolean
     */
    public boolean isTableEmpty(String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + table, null);
        boolean empty;

        empty = !mCursor.moveToFirst();
        mCursor.close();

        return empty;
    }

    /**
     * Export db to csv
     * @return Success boolean
     */
    public boolean exportDbToCSV(){
        try {
            SQLiteDatabase db = openDataBase();
            ExportConfig config = new ExportConfig(db, DB_NAME, ExportConfig.ExportType.CSV, appContext);
            DBExporterCsv exporter = new DBExporterCsv(config);
            exporter.export();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Export db to json
     * @return Success boolean
     */
    public boolean exportDbToJSON(){
        try {
            SQLiteDatabase db = openDataBase();
            ExportConfig config = new ExportConfig(db, DB_NAME, ExportConfig.ExportType.JSON, appContext);
            DBExporterJson exporter = new DBExporterJson(config);
            exporter.export();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Restore db from json export
     * @return Success boolean
     */
    public boolean restoreDbFromJSON(){
        try {
            File dbJsonPath = new File(FileUtils.getAppDir(appContext) + "/databases/" + DB_NAME + ".json");
            if(dbJsonPath.exists() && !dbJsonPath.isDirectory()) {
                ImportConfig config = new ImportConfig(this, dbJsonPath, ImportConfig.ImportType.JSON);
                DBImporterJson importer = new DBImporterJson(config);
                importer.restore();
            }
            else{
                throw new FileNotFoundException("Db json file not found for restore");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Import data from json export to db
     * @return Success boolean
     */
    public boolean importDataFromJSON(){
        try {
            File dbJsonPath = new File(FileUtils.getAppDir(appContext) + "/databases/" + DB_NAME + ".json");
            if(dbJsonPath.exists() && !dbJsonPath.isDirectory()) {
                ImportConfig config = new ImportConfig(this, dbJsonPath, ImportConfig.ImportType.JSON);
                DBImporterJson importer = new DBImporterJson(config);
                importer.importData();
            }
            else{
                throw new FileNotFoundException("Db json file not found for import");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Import data from custom json file to db
     * @param file Json file with compatible data
     * @return Success boolean
     */
    public boolean importDataFromJSON(File file){
        try {
            if(file.exists() && !file.isDirectory()) {
                ImportConfig config = new ImportConfig(this, file, ImportConfig.ImportType.JSON);
                DBImporterJson importer = new DBImporterJson(config);
                importer.importData();
            }
            else{
                throw new FileNotFoundException("Db json file not found for import");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Change the base url for db sync
     * @param url Api url
     */
    public void setSyncBaseUrl(String url){
        Constants.SYNC_URL = url;
    }

    /**
     * Synchronization function used to send db instance in json file to remote api
     *
     * @param autoExport Enable auto export to json before sync
     */
    public void syncDb(boolean autoExport) throws Exception {
        if(Constants.SYNC_URL == null || Constants.SYNC_URL.trim().equals("")) {
            if (autoExport)
                exportDbToJSON();

            File dbJsonPath = new File(FileUtils.getAppDir(appContext) + "/databases/" + DB_NAME + ".json");
            if (dbJsonPath.exists() && !dbJsonPath.isDirectory()) {
                Log.d(Constants.PACKAGE_NAME, dbJsonPath.getAbsolutePath());

                String jsonContent;
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(dbJsonPath));
                    JsonObject js = new Gson().fromJson(bufferedReader, JsonObject.class);
                    jsonContent = new Gson().toJson(js);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                Call<ResponseBody> call = requestBuilder(jsonContent);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        Log.v("Upload", "success");
                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Upload error:", Objects.requireNonNull(t.getMessage()));
                    }
                });
            } else {
                throw new FileNotFoundException("Db json file not found for synchronizing");
            }
        }
        else{
            throw new MissingSyncUrlException("Constants.SYNC_URL empty or null");
        }
    }

    /**
     * Request builder method, used to build custom request for syncDb method
     * Can be override to build custom request, need an interface template for the request model (cf : FileUploadService)
     * By default, it will create a request composed by : description ("DB sync") and a content (jsonData)
     *
     * @param jsonData Exported json data
     * @return Call<ResponseBody> build with specific model
     */
    public Call<ResponseBody> requestBuilder(String jsonData){
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        String titleString = "DB sync";
        RequestBody title =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, titleString);

        RequestBody content =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, jsonData);

        return service.upload(title, content);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < tables.size(); i++) {
            db.execSQL(tables.get(i).getSql());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int n) {
        for (int i = 0; i < tables.size(); i++) {
            db.execSQL("DROP TABLE IF EXISTS " + tables.get(i).getTableName());
        }
        onCreate(db);
    }
}
