package com.supermenote.note.basedb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.supermenote.note.utils.BaseUtils;
import com.thinkernote.ThinkerNote.Action.TNAction;
import com.thinkernote.ThinkerNote.Action.TNAction.TNActionResult;
import com.thinkernote.ThinkerNote.General.TNActionType;
import com.thinkernote.ThinkerNote.General.TNSettings;
import com.thinkernote.ThinkerNote.General.TNUtils;
import com.thinkernote.ThinkerNote.Utils.MLog;
import com.thinkernote.ThinkerNote.base.TNApplication;

import java.util.Vector;

/**
 * 数据库基类
 * <p>
 * 创建，升级，操作等，通过封装的辅助类，最终都调用该类进行操作。
 */
public class NoteDb extends SQLiteOpenHelper {
    private static final String TAG = "TNDatabase";
    private static NoteDb singleton = null;
    private final static int DB_VER = 2;
    private final static String DB_NAME = "ThinkerNote3.db";
    private SQLiteDatabase db = null;

    private int changeBits;

    public NoteDb() {
        super(BaseUtils.getAppContext(), DB_NAME, null, DB_VER);

        db = getWritableDatabase();

    }

    public static NoteDb getInstance() {
        if (singleton == null) {
            synchronized (NoteDb.class) {
                if (singleton == null) {
                    singleton = new NoteDb();
                }
            }
        }

        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase aDB) {
//        MLog.d(TAG, "onCreate");
        aDB.execSQL(TNSQLString.USER_CREATE_TABLE);
        aDB.execSQL(TNSQLString.CAT_CREATE_TABLE);
        aDB.execSQL(TNSQLString.TAG_CREATE_TABLE);
        aDB.execSQL(TNSQLString.NOTE_CREATE_TABLE);
        aDB.execSQL(TNSQLString.ATT_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase aDB, int oldVer, int newVer) {
        if (oldVer == 1 && newVer == 2) {
            aDB.execSQL("ALTER TABLE `Category` ADD `strIndex` TEXT(8) NOT NULL DEFAULT ''");
        }
    }

    public void resetDb() {
        beginTransaction();
        try {
            //drop tables
            getInstance().db.execSQL(TNSQLString.USER_DROP_TABLE);
            getInstance().db.execSQL(TNSQLString.CAT_DROP_TABLE);
            getInstance().db.execSQL(TNSQLString.TAG_DROP_TABLE);
            getInstance().db.execSQL(TNSQLString.NOTE_DROP_TABLE);
            getInstance().db.execSQL(TNSQLString.ATT_DROP_TABLE);

            //create tables
            getInstance().db.execSQL(TNSQLString.USER_CREATE_TABLE);
            getInstance().db.execSQL(TNSQLString.CAT_CREATE_TABLE);
            getInstance().db.execSQL(TNSQLString.TAG_CREATE_TABLE);
            getInstance().db.execSQL(TNSQLString.NOTE_CREATE_TABLE);
            getInstance().db.execSQL(TNSQLString.ATT_CREATE_TABLE);

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    public void DBReset(TNAction aAction) {
        beginTransaction();
        try {
            //drop tables
            getInstance().db.execSQL(TNSQLString.USER_DROP_TABLE);
            getInstance().db.execSQL(TNSQLString.CAT_DROP_TABLE);
            getInstance().db.execSQL(TNSQLString.TAG_DROP_TABLE);
            getInstance().db.execSQL(TNSQLString.NOTE_DROP_TABLE);
            getInstance().db.execSQL(TNSQLString.ATT_DROP_TABLE);

            //create tables
            getInstance().db.execSQL(TNSQLString.USER_CREATE_TABLE);
            getInstance().db.execSQL(TNSQLString.CAT_CREATE_TABLE);
            getInstance().db.execSQL(TNSQLString.TAG_CREATE_TABLE);
            getInstance().db.execSQL(TNSQLString.NOTE_CREATE_TABLE);
            getInstance().db.execSQL(TNSQLString.ATT_CREATE_TABLE);

            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    private long insert(String sql, String[] args) {
        int start = 0, end = 0;
        String tableName = "";
        ContentValues values = new ContentValues();

        start = sql.indexOf("`");
        end = sql.indexOf("`", start + 1);
        tableName = sql.substring(start, end + 1);

        for (int i = 0; i < args.length; i++) {
            start = sql.indexOf("`", end + 1);
            end = sql.indexOf("`", start + 1);
            values.put(sql.substring(start, end + 1), args[i]);
        }
        return db.insertOrThrow(tableName, null, values);
    }

    private Vector<Vector<String>> select(String sql, String[] args) {
        Cursor cursor = db.rawQuery(sql, args);

        Vector<Vector<String>> allData = new Vector<Vector<String>>();
        while (cursor.moveToNext()) {
            Vector<String> rowData = new Vector<String>();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String value = cursor.getString(i);
                if (value != null)
                    rowData.add(value);
                else
                    rowData.add("0");
            }
            allData.add(rowData);
        }
        cursor.close();
//        MLog.d("TNDB--select打印：" + allData.size() + "个笔记。", allData.toString());

        return allData;
    }

    private void execute(String sql, String[] args) {
        db.execSQL(sql, args);
    }

    /**
     * 使用没问题
     *
     * @param sql
     * @param args
     * @return
     */
    public Object execSQL(String sql, Object... args) {
        String[] valus = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            valus[i] = String.valueOf(args[i]);
        }
        printSql(sql, valus);
        if (sql.startsWith("SELECT")) {
            return select(sql, valus);
        } else if (sql.startsWith("INSERT")) {
            //TODO 原版插入 有问题
//            return insert(sql, valus);
            //TODO 本人修改的插入
            return insertSQL(sql, valus);
        } else {
            execute(sql, valus);
        }

        return null;
    }

    //TODO 不用 delete
    public void executeSQL(TNAction aAction) {
//        MLog.d("TNDB--executeSQL", aAction.inputs.toString());
        try {
            String sql = (String) aAction.inputs.get(0);
            if (sql.startsWith("SELECT")) {
                String[] args = new String[aAction.inputs.size() - 1];
                for (int i = 1; i < aAction.inputs.size(); i++) {
                    args[i - 1] = aAction.inputs.get(i).toString();
                }
                Cursor cursor = db.rawQuery(sql, args);

                Vector<Vector<String>> allData = new Vector<Vector<String>>();
                while (cursor.moveToNext()) {
                    Vector<String> rowData = new Vector<String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        String value = cursor.getString(i);
                        if (value != null)
                            rowData.add(value);
                        else
                            rowData.add("0");
                    }
                    allData.add(rowData);
                }
                aAction.outputs.add(allData);
                cursor.close();
            } else if (sql.startsWith("INSERT")) {
                int start = 0, end = 0;
                String tableName = "";
                ContentValues values = new ContentValues();

                start = sql.indexOf("`");
                end = sql.indexOf("`", start + 1);
                tableName = sql.substring(start, end + 1);

                for (int i = 1; i < aAction.inputs.size(); i++) {
                    start = sql.indexOf("`", end + 1);
                    end = sql.indexOf("`", start + 1);
                    values.put(sql.substring(start, end + 1), aAction.inputs.get(i).toString());
                }
                long id = db.insertOrThrow(tableName, null, values);
                aAction.outputs.add(id);
            } else {
                Object[] args = new Object[aAction.inputs.size() - 1];
                for (int i = 1; i < aAction.inputs.size(); i++) {
                    args[i - 1] = aAction.inputs.get(i);
                }
                db.execSQL(sql, args);

            }
            aAction.result = TNActionResult.Finished;
        } catch (SQLiteException e) {
            e.printStackTrace();
            MLog.e("数据库异常：" + e.toString());
        }
    }
//==================================sjy 更改 开始======================================


    public void deleteSQL(String sql, Object[] args) {
        try {
            db.execSQL(sql, args);

        } catch (SQLiteException e) {
            e.printStackTrace();
            TNApplication.getInstance().DbReportError("username:" + TNSettings.getInstance().username + " SQLiteException:" + e.toString());
        }
    }

    /**
     * NoteEditAct使用
     *
     * @param sql
     * @param args
     * @return
     */
    public long insertSQL(String sql, Object[] args) {
        long id = -1;
        try {
            db = getWritableDatabase();
            int start = 0, end = 0;
            String tableName = "";
            ContentValues values = new ContentValues();

            //取tablename
            start = sql.indexOf("`");
            end = sql.indexOf("`", start + 1);
            tableName = sql.substring(start, end + 1);


            //打印要拼接的数据信息
            StringBuffer str = new StringBuffer();
            for (Object obj : args) {
                str.append(obj == null ? "" : obj.toString() + " ");
            }
//            MLog.e("saveNote--insertSQL", "tableName:" + tableName, "start=" + start, "end=" + end, "sql=" + sql + "\nargs=" + str.toString());

            //取内容
            for (int i = 0; i < args.length; i++) {
                start = sql.indexOf("`", end + 1);
                end = sql.indexOf("`", start + 1);
                //sql中
                values.put(sql.substring(start, end + 1), args[i] != null ? args[i].toString() : "");

                //打印
                int s = start;
                int e = end;
            }

            id = db.insertOrThrow(tableName, null, values);
//            MLog.d("saveNote", "TNDb--insertSQL-->noteLocalId=" + id);
        } catch (Exception e) {
            MLog.e("TNDb--insertSQL", "插入数据库失败:" + e.toString());
        }
        return id;
    }


//==================================sjy 更改 结束======================================

    public static void beginTransaction() {
        getInstance().db.beginTransaction();
    }

    public static void setTransactionSuccessful() {
        getInstance().db.setTransactionSuccessful();
    }

    public static void endTransaction() {
        getInstance().db.endTransaction();
    }

    public static boolean isChanges(int aChange) {
        return (getInstance().changeBits & aChange) != 0;
    }

    public static void addChange(int aChange) {
        if ((getInstance().changeBits & aChange) == 0)
            getInstance().changeBits += aChange;
    }

    public static void removeChange(int aChange) {
        if ((getInstance().changeBits & aChange) != 0)
            getInstance().changeBits -= aChange;
    }

    private void printSql(String sql, String[] args) {
        String values = "";
        for (String arg : args) {
            arg = "`" + arg + "` ";
            values = values + arg;
        }
//        MLog.w("TNDb--execSQL--printSql:", sql + "\r\n" + values);
    }
}
