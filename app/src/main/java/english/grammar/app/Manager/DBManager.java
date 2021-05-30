package english.grammar.app.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import english.grammar.app.DictionaryViewModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    public String DATABASE_NAME = "dictionary.sqlite";
    static final int DATABASE_VERSION = 16;
    final Context context;
    SQLiteDatabase db;
    DatabaseHelper DBHelper;

    public DBManager(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.disableWriteAheadLogging();
        }

    }

    // ---opens the database---
    public DBManager open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    public long copyDataBase() throws IOException {
        String DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath()
                .toString();

        // Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        return length;
    }

    public List<DictionaryViewModel> filterWordsMeanings(String a) {
        List<DictionaryViewModel> data = new ArrayList<>();
        String[] selection = {a + '%'};
        String query = "select * from grouptbl where word like ?";
        Cursor cursor = db.rawQuery(query, selection);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                DictionaryViewModel dictionaryModel = new DictionaryViewModel();
                dictionaryModel.word = cursor.getString(0);
                dictionaryModel.meaning = cursor.getString(1);
                data.add(dictionaryModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
}


