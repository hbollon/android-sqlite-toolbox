package com.bcstudio.androidsqlitetoolbox.Database;

public class Data {
    private String columnName = "";
    private String value = "";

    public Data(String columnName, String value) {
        this.columnName = columnName.replace(" ", "_");
        this.value = value;
    }

    public Data(String columnName, int value) {
        this.columnName = columnName.toUpperCase();
        this.value = String.valueOf(value);
    }

    public Data(String columnName, double value) {
        this.columnName = columnName.toUpperCase();
        this.value = String.valueOf(value);
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
