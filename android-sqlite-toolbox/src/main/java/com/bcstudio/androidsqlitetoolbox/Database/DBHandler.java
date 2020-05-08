package com.bcstudio.androidsqlitetoolbox.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.bcstudio.androidsqlitetoolbox.Constants;
import com.bcstudio.androidsqlitetoolbox.Export.DBExporterCsv;
import com.bcstudio.androidsqlitetoolbox.Export.DBExporterJson;
import com.bcstudio.androidsqlitetoolbox.Export.ExportConfig;
import com.bcstudio.androidsqlitetoolbox.FileUtils;
import com.bcstudio.androidsqlitetoolbox.Http.FileUploadService;
import com.bcstudio.androidsqlitetoolbox.Http.ServiceGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private SQLiteDatabase db;
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
    }

    public DBHandler(Context context, String dbName, SQLiteDatabase.CursorFactory curFactory, int version, DatabaseErrorHandler dbErrHandler) {
        super(context, dbName, curFactory, version);
        this.appContext = context;
        this.DB_NAME = dbName;
        this.curFactory = curFactory;
        this.version = version;
        this.dbErrHandler = dbErrHandler;
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
     * Delete entry from db
     * Required table name and entry id
     *
     * @param id Entry id
     * @param table Table name
     * @return bool
     */
    public boolean deleteData(String id, String table){
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(table,"id=?",new String[]{id});
        return res != -1;
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
     * Export db in csv
     */
    public void exportDbToCSV(){
        try {
            SQLiteDatabase db = openDataBase();
            ExportConfig config = new ExportConfig(db, DB_NAME, ExportConfig.ExportType.CSV, appContext);
            DBExporterCsv exporter = new DBExporterCsv(config);
            exporter.export();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Export db in json
     */
    public void exportDbToJSON(){
        try {
            SQLiteDatabase db = openDataBase();
            ExportConfig config = new ExportConfig(db, DB_NAME, ExportConfig.ExportType.JSON, appContext);
            DBExporterJson exporter = new DBExporterJson(config);
            exporter.export();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Synchronization function used to send db instance in json file to remote api
     */
    public void syncDb(){
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        File dbJsonPath = new File(FileUtils.getAppDir(appContext) + "/databases/" + DB_NAME + ".json");
        Log.d(Constants.PACKAGE_NAME, dbJsonPath.getAbsolutePath());

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(Uri.fromFile(dbJsonPath).toString()),
                        dbJsonPath
                );

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("value", dbJsonPath.getName(), requestFile);

        String titleString = "DB sync";
        RequestBody title =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, titleString);

        Call<ResponseBody> call = service.upload(title, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
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
