package com.bcstudio.androidsqlitetoolbox.Import;

import android.util.Log;

import com.bcstudio.androidsqlitetoolbox.Constants;
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
    private Set<String> excludedFields;

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

    /**
     * Add table to exclude list
     * @param tableName Table name
     */
    public void setExcludeTable(String tableName) {
        if (excludedTables == null) {
            excludedTables = new HashSet<>();
        }
        excludedTables.add(tableName.trim().toUpperCase());
    }

    /**
     * Check if table name is exclude
     * @param tableName Table name
     * @return bool
     */
    public boolean isExcludeTable(String tableName) {
        if (excludedTables == null) {
            return false;
        }
        return excludedTables.contains(tableName.trim().toUpperCase());
    }

    /**
     * Add field to exclude list
     * @param fieldName Field name
     */
    public void setExcludeField(String fieldName) {
        if (excludedFields == null) {
            excludedFields = new HashSet<>();
        }
        excludedFields.add(fieldName.trim().toUpperCase());
    }

    /**
     * Check if field name is exclude
     * @param fieldName Field name
     * @return bool
     */
    public boolean isExcludeField(String fieldName) {
        if (excludedFields == null) {
            return false;
        }
        return excludedFields.contains(fieldName.trim().toUpperCase());
    }

    public String getDatabaseName() {
        return databaseName;
    }


}
