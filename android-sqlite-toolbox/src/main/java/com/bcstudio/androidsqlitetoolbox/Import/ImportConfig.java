package com.bcstudio.androidsqlitetoolbox.Import;

import android.database.sqlite.SQLiteDatabase;

import com.bcstudio.androidsqlitetoolbox.Database.DBHandler;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ImportConfig {
    public enum ImportType {
        JSON;
    }

    // Package variables
    DBHandler db;
    File srcFile;

    private ImportType importType;
    private String databaseName;
    private Set<String> excludedTables;

    public ImportConfig(DBHandler database, File srcFile, ImportType importType) {
        this.srcFile = srcFile;
        this.db = database;
        this.importType = importType;
        this.databaseName = db.getDBName();
    }

    public ImportConfig(String databaseName, File srcFile, ImportType importType) {
        this.srcFile = srcFile;
        this.databaseName = databaseName;
        this.importType = importType;
    }

    public void setExcludeTable(String tableName) {
        if (excludedTables == null) {
            excludedTables = new HashSet<>();
        }
        excludedTables.add(tableName);
    }

    /**
     * Check if table id excluded
     * @param tableName
     * @return bool
     */
    public boolean isExcludeTable(String tableName) {
        if (excludedTables == null) {
            return false;
        }
        return excludedTables.contains(tableName);
    }

    public String getDatabaseName() {
        return databaseName;
    }


}
