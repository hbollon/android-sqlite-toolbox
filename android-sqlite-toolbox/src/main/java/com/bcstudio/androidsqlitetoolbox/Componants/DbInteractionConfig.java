package com.bcstudio.androidsqlitetoolbox.Componants;

import java.util.HashSet;
import java.util.Set;

public abstract class DbInteractionConfig {

    protected String databaseName;
    protected Set<String> excludedTables;
    protected Set<String> excludedFields;

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
