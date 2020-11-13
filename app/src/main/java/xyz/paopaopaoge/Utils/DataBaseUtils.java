package xyz.paopaopaoge.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseUtils {

    class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(@Nullable Context context) {
            super(context, "gamedata", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String query = "CREATE TABLE IF NOT EXISTS tableScore (uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "score INTEGER DEFAULT (0), " +
                    "maxNum INTEGER NOT NULL DEFAULT (0), " +
                    "hasUndo BOOLEAN DEFAULT 0 NOT NULL, " +
                    "uploadTime DATETIME NOT NULL DEFAULT (datetime('now', 'localtime')));";
            sqLiteDatabase.execSQL(query);

            query = "CREATE TABLE lastGameRecord (gameData TEXT NOT NULL, gameFlag TEXT NOT NULL);";
            sqLiteDatabase.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

    public class ResultRow {
        private String uname = null;
        private int score = 0;
        private int maxNum = 0;
        private boolean hasUndo = false;
        private String upLoadTime = null;

        public ResultRow(String _uname, int _score, int _maxNum, boolean _hasUndo, String _upLoadTime) {
            this.uname = _uname;
            this.score = _score;
            this.maxNum = _maxNum;
            this.hasUndo = _hasUndo;
            this.upLoadTime = _upLoadTime;
        }

        public String getUname() {
            return uname;
        }

        public int getScore() {
            return score;
        }

        public int getMaxNum() {
            return maxNum;
        }

        public boolean isHasUndo() {
            return hasUndo;
        }

        public String getUpLoadTime() {
            return upLoadTime;
        }
    }

    private Context context = null;
    private SQLiteDatabase db = null;

    public DataBaseUtils(Context context) {
        this.context = context;
        DatabaseOpenHelper helper = new DatabaseOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public void insertScore(int score, int maxNum, boolean hasUndo) {
        String query = String.format("INSERT INTO tableScore VALUES (null, %d, %d, %d, datetime('now', 'localtime'));", score, maxNum, hasUndo ? 1 : 0);
        db.execSQL(query);
    }

    public List<ResultRow> queryScore() {
        //"SELECT score, maxNum, hasUndo, uploadTime FROM tableScore ORDER BY score DESC;"
        List<ResultRow> results = new ArrayList<ResultRow>();
        Cursor cursor = db.query("tableScore", new String[]{"score", "maxNum", "hasUndo", "uploadTime"}, null, null, null, null, "score DESC", null);
        while (cursor.moveToNext())
            results.add(new ResultRow("me", cursor.getInt(0), cursor.getInt(1), cursor.getInt(2) == 1 ? true : false, cursor.getString(3)));
        return results;
    }

    public int queryBestScore() {
        Cursor cursor = db.query("tableScore", new String[]{"score"}, null, null, null, null, "score DESC", null);
        if (cursor.getCount() == 0) {
            return 0;
        } else {
            cursor.moveToNext();
            return cursor.getInt(0);
        }
    }

    public Map<String, String> queryLastGameRecord() {
        Map<String, String> record = null;
        Cursor cursor = db.query("lastGameRecord", new String[]{"gameData", "gameFlag"}, null, null, null, null, null, null);
        if (cursor.getCount() != 0){
            record = new HashMap<String, String>();
            cursor.moveToNext();
            record.put("gameData", cursor.getString(0));
            record.put("gameFlag", cursor.getString(1));
            db.execSQL("DELETE FROM lastGameRecord;");
        }
        cursor.close();
        return record;
    }

    public void insertLastGameRecord(String gameDataString, String gameFlagString) {
        ContentValues values = new ContentValues();
        values.put("gameData", gameDataString);
        values.put("gameFlag", gameFlagString);
        db.insert("lastGameRecord", null, values);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        db.close();
    }
}
