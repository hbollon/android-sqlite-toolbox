package com.bcstudio.androidsqlitetoolbox.Database;

public class Column {

    private String columnName = "", columnDataType = "";

    public Column(String columnName, String... columnDataTypes) {
        this.columnName = columnName.replaceAll(" ", "_");
        String finalDatatype = "";
        for (int i = 0; i < columnDataTypes.length; i++) {
            if (!columnDataTypes[i].startsWith(" ")) {
                columnDataTypes[i] = " " + columnDataTypes[i];
            }
            if (!columnDataTypes[i].endsWith(" ")) {
                columnDataTypes[i] = columnDataTypes[i] + " ";
            }
            finalDatatype += columnDataTypes[i];
        }
        this.columnDataType = finalDatatype.toUpperCase();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

}
