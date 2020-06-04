package com.bcstudio.androidsqlitetoolbox.Database;

import java.util.Objects;

public class Data {
    private String columnName;
    private String value;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        Data data = (Data) o;
        return Objects.equals(columnName, data.columnName) &&
                Objects.equals(value, data.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, value);
    }
}
