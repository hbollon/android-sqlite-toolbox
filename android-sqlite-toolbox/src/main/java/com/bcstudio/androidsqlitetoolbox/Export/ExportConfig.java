package com.bcstudio.androidsqlitetoolbox.Export;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bcstudio.androidsqlitetoolbox.Componants.DbInteractionConfig;
import com.bcstudio.androidsqlitetoolbox.FileUtils;

import java.io.File;
import java.io.IOException;

public class ExportConfig extends DbInteractionConfig {

    public enum ExportType {
        JSON, CSV
    }

    // Package variables
    SQLiteDatabase db;
    File directory;

    private ExportType exportType;
    private String exportCustomFileExtension = "";

    /**
     * Constructor
     * @param db SQLiteDatabase instance
     * @param databaseName Db name
     * @param exportType ExportType (define if file will be export to CSV or JSON)
     * @param appContext Application context
     * @throws IOException
     */
    public ExportConfig(SQLiteDatabase db, String databaseName, ExportType exportType, Context appContext) throws IOException {
        this.db = db;
        this.exportType = exportType;
        this.databaseName = databaseName;

        if( !FileUtils.isExternalStorageWritable() ){
            throw new IOException("Cannot write to external storage");
        }
        this.directory = FileUtils.createDirIfNotExist(FileUtils.getAppDir(appContext) + "/databases/");
    }

    /**
     * Alternative constructor for custom file extension (not handled by ExportType)
     * @param db SQLiteDatabase instance
     * @param databaseName Db name
     * @param exportCustomFileExtension File extension
     * @param appContext Application context
     * @throws IOException
     */
    public ExportConfig(SQLiteDatabase db, String databaseName, String exportCustomFileExtension, Context appContext) throws IOException {
        this.db = db;
        this.exportCustomFileExtension = exportCustomFileExtension;
        this.databaseName = databaseName;

        if(!FileUtils.isExternalStorageWritable()){
            throw new IOException("Cannot write to external storage");
        }
        this.directory = FileUtils.createDirIfNotExist(FileUtils.getAppDir(appContext) + "/databases/");
    }

    /**
     * Return corresponding file extension
     * @return File extension string
     * @throws IllegalArgumentException
     */
    public String getFileExtension() throws IllegalArgumentException {
        if(exportType != null) {
            switch (exportType) {
                case CSV:
                    return ".csv";
                case JSON:
                    return ".json";
                default:
                    throw new IllegalArgumentException("File format unhandled or invalid");
            }
        } else {
            if(!exportCustomFileExtension.trim().equals("") && exportCustomFileExtension.trim().matches("\\.[a-z]+"))
                return exportCustomFileExtension.trim();
            throw new IllegalArgumentException("File format unhandled or invalid");
        }
    }
}
