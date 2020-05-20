package com.bcstudio.androidsqlitetoolbox.Import;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ImportConfig {
    public enum ImportType {
        JSON;
    }

    // Package variables
    File directory;

    private ImportType importType;
    private String databaseName;
    private Set<String> excludedTables;

    public ImportConfig(String databaseName, File directory, ImportType importType) {
        this.directory = directory;
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
