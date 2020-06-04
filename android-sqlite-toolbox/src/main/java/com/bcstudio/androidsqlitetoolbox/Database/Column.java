package com.bcstudio.androidsqlitetoolbox.Database;

import java.util.Objects;

public class Column {

    private String columnName, columnDataType;

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
            finalDatatype = finalDatatype.concat(columnDataTypes[i]);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Column)) return false;
        Column column = (Column) o;
        return Objects.equals(columnName, column.columnName) &&
                Objects.equals(columnDataType, column.columnDataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, columnDataType);
    }
}
