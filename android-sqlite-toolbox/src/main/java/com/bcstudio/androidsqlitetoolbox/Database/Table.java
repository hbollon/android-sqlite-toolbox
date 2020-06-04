package com.bcstudio.androidsqlitetoolbox.Database;

import java.util.Arrays;
import java.util.Objects;

public class Table {
    private String tableName;
    private Column[] columns;
    private String sql = "";

    public Table(String tableName, Column[] columns) {
        this.tableName = tableName.replace(" ", "_");
        this.columns = columns;

        if(columns != null)
            initSQL();
    }

    private void initSQL(){
        sql = " CREATE TABLE IF NOT EXISTS " + tableName + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT , ";
        for (int i = 0; i < columns.length; i++) {
            sql += " " + columns[i].getColumnName() + " " + columns[i].getColumnDataType() + " ";
            if (i == columns.length - 1) {
                sql += " ) ";
            } else {
                sql += " , ";
            }
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        initSQL();
    }

    public Column[] getColumns() {
        return columns;
    }

    public void setColumns(Column[] columns) {
        this.columns = columns;
        initSQL();
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table)) return false;
        Table table = (Table) o;
        return Objects.equals(tableName, table.tableName) &&
                Arrays.equals(columns, table.columns);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(tableName);
        result = 31 * result + Arrays.hashCode(columns);
        return result;
    }
}
