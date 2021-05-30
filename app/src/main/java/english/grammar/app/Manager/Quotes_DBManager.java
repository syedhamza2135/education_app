package english.grammar.app.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import english.grammar.app.Models.QuotesModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Quotes_DBManager {

    public String DATABASE_NAME = "quotesdb.sqlite";
    static final int DATABASE_VERSION = 17;
    final Context context;
    SQLiteDatabase db;
    DatabaseHelper DBHelper;

    public Quotes_DBManager(Context ctx) {
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
    public Quotes_DBManager open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    public void createDataBase() throws IOException {

        boolean mDataBaseExist = checkDataBase();

        if (mDataBaseExist) {
            // do nothing - database already exist

        } else {

            DBHelper.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * This method checks whether database is exists or not
     **/
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = context.getDatabasePath(DATABASE_NAME).getPath()
                    .toString();

            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
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

    public List<QuotesModel> getQuote() {
        List<QuotesModel> data = new ArrayList<>();
        String query = "select * from quotes";
        Cursor cursor = db.rawQuery(query, null);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                QuotesModel quotesModel = new QuotesModel();
                quotesModel.setId(cursor.getString(0));
                quotesModel.setEng_quote(cursor.getString(1));
                quotesModel.setAuthor(cursor.getString(2));
                quotesModel.setFav(cursor.getString(3));
                data.add(quotesModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<QuotesModel> getQuoteIDs() {
        List<QuotesModel> data = new ArrayList<>();
        String query = "select id from quotes";
        Cursor cursor = db.rawQuery(query, null);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                QuotesModel quotesModel = new QuotesModel();
                quotesModel.setId(cursor.getString(0));
                data.add(quotesModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<QuotesModel> getFavQuote() {
        List<QuotesModel> data = new ArrayList<>();
        String query = "select * from quotes where favourite = 1";
        Cursor cursor = db.rawQuery(query, null);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                QuotesModel quotesModel = new QuotesModel();
                quotesModel.setId(cursor.getString(0));
                quotesModel.setEng_quote(cursor.getString(1));
                quotesModel.setAuthor(cursor.getString(2));
                quotesModel.setFav(cursor.getString(3));
                data.add(quotesModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public void updateMessageFav(final String id, final String fav) {
        ContentValues cv = new ContentValues();
        cv.put("favourite", fav);
        long obj = db.update("quotes", cv, "id = ?", new String[]{id});
        if (obj > 0) {
            Log.d("msg", "updateMessageFav: saved");
        } else {
            Log.d("msg", "updateMessageFav: failed");
        }
    }

}


