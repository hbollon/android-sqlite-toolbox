package com.bcstudio.androidsqlitetoolbox.Export;

public class DBExporterCsv extends DBExporter {
    private boolean newTable;
    private String dataRow;
    private String row;
    private String table;
    private String dbData;

    public DBExporterCsv(ExportConfig config) {
        super(config);
    }

    @Override
    protected void prepairExport(String dbName) throws Exception {
        this.newTable = true;
        this.row = "";
        this.dataRow = "";
        this.table = "";
        this.dbData = "";
    }

    @Override
    protected String getExportAsString() throws Exception {
        return dbData;
    }

    @Override
    protected void startTable(String tableName) throws Exception {
        newTable = true;
        table = "";
        table = table.concat("\"table="+tableName+"\"\n");
    }

    @Override
    protected void endTable() throws Exception {
        dataRow = "";
        dbData = dbData.concat(table+"\n");
    }

    @Override
    protected void endRow() throws Exception {
        if(newTable) {
            table = table.concat(dataRow + "\n");
            table = table.concat(row + "\n");
            newTable = false;
        }
        else
            table = table.concat(row + "\n");
    }

    @Override
    protected void populateRowWithField(String columnName, String string) throws Exception {
        if(newTable) {
            dataRow = dataRow.concat("\"" + columnName + "\",");
            row = row.concat("\"" + string + "\",");
        }
        else
            row = row.concat("\"" + string + "\",");
    }

    @Override
    protected void startRow() throws Exception {
        row = "";
    }
}
