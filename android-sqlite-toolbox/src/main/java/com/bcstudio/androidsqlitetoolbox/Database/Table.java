package com.bcstudio.androidsqlitetoolbox.Database;

public class Table {
    private String tableName;
    private Column[] columns;
    private String sql = "";

    public Table(String tableName, Column[] columns) {
        this.tableName = tableName.replace(" ", "_");
        this.columns = columns;

        initSQL();
    }

    private void initSQL(){
        sql = " CREATE TABLE " + tableName + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
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
    }

    public Column[] getColumns() {
        return columns;
    }

    public void setColumns(Column[] columns) {
        this.columns = columns;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
