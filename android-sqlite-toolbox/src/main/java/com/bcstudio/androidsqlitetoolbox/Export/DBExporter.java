package com.bcstudio.androidsqlitetoolbox.Export;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import com.bcstudio.androidsqlitetoolbox.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Abstract class designed to export db tables
 * Must be extends
 */
public abstract class DBExporter {
    protected final SQLiteDatabase db;
    protected final File directory;
    protected final ExportConfig config;

    /**
     * Constructor
     * @param config ExportConfig instance containing export config
     */
    public DBExporter(ExportConfig config) {
        this.config = config;
        db = config.db;
        directory = config.directory;
    }

    /**
     * Main export function
     * Skip metadata and excluded tables
     * @throws Exception
     */
    public void export() throws Exception {
        String dbName = config.getDatabaseName();
        if (dbName == null) {
            throw new IllegalArgumentException("ExportConfig.databaseName must not be null");
        }
        Log.i(Constants.PACKAGE_NAME, "exporting database - " + dbName);

        prepairExport(dbName);

        // get the tables
        String sql = "select * from sqlite_master";
        Cursor c = this.db.rawQuery(sql, new String[0]);
        Log.d(Constants.PACKAGE_NAME, "select * from sqlite_master, cur size " + c.getCount());
        while (c.moveToNext()) {
            String tableName = c.getString(c.getColumnIndex("name"));
            Log.d(Constants.PACKAGE_NAME, "table name " + tableName);

            // skip metadata, sequence, and uidx (unique indexes)
            if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")
                    && !tableName.startsWith("uidx") && !tableName.startsWith("idx_") && !tableName.startsWith("_idx")
                    && !config.isExcludeTable(tableName)) {
                try {
                    this.exportTable(tableName);
                } catch (SQLiteException e) {
                    Log.w(Constants.PACKAGE_NAME, "Error exporting table " + tableName, e);
                }
            }
        }
        c.close();
        this.writeToFile(getExportAsString(), dbName + config.getFileExtension());
        Log.i(Constants.PACKAGE_NAME, "exporting database complete");
    }

    /**
     * Export table content
     * @param tableName
     * @throws Exception
     */
    private void exportTable(final String tableName) throws Exception {
        Log.d(Constants.PACKAGE_NAME, "exporting table - " + tableName);

        String sql = "select * from " + tableName;
        Cursor c = this.db.rawQuery(sql, new String[0]);

        startTable(tableName);
        while (c.moveToNext()) {
            startRow();
            String id = c.getString(1);
            if (id == null || TextUtils.isEmpty(id)) {
                id = c.getString(0);
            }
            for (int i = 0; i < c.getColumnCount(); i++) {
                populateRowWithField(c.getColumnName(i), c.getString(i));
            }
            endRow();
        }
        c.close();
        endTable();
    }

    /**
     * Write exported data to dest file
     * @param payload
     * @param exportFileName
     * @throws IOException
     */
    private void writeToFile(String payload, String exportFileName) throws IOException {
        File file = new File(directory, exportFileName);
        if (file.exists()) {
            file.delete();
        }

        ByteBuffer buff = ByteBuffer.wrap(payload.getBytes());
        try (FileChannel channel = new FileOutputStream(file).getChannel()) {
            channel.write(buff);
        } finally {
            Log.i(Constants.PACKAGE_NAME, "Exported DB to " + file.toString());
        }
    }

    /**
     * Can be used to generate filename with time
     * @return
     */
    private String createBackupFileName(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmm");
        return "_backup_" + sdf.format(new Date());
    }

    /**
     * Return exported data to string
     * @return exported data string
     * @throws Exception
     */
    abstract protected String getExportAsString() throws Exception;

    /**
     * Initialize export
     * @param dbName
     * @throws Exception
     */
    abstract protected void prepairExport(String dbName) throws Exception;

    /**
     * Handle instructions at row end
     * @throws Exception
     */
    abstract protected void endRow() throws Exception;

    /**
     * Add data to row
     * @param columnName
     * @param string
     * @throws Exception
     */
    abstract protected void populateRowWithField(String columnName, String string) throws Exception;

    abstract protected void startRow() throws Exception;

    /**
     * Handle instructions at table end
     * @throws Exception
     */
    abstract protected void endTable() throws Exception;

    /**
     * Handle instructions at table start
     * @param tableName
     * @throws Exception
     */
    abstract protected void startTable(String tableName) throws Exception;
}
