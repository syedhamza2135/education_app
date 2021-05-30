package english.grammar.app.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import english.grammar.app.Models.ConversationModel;
import english.grammar.app.Models.ConversationTypesModel;
import english.grammar.app.Models.DialoguesModel;
import english.grammar.app.Models.PartOfSpeechModel;
import english.grammar.app.Models.QuizModel;
import english.grammar.app.Models.TensesExampleModel;
import english.grammar.app.Models.TensesModel;
import english.grammar.app.PhrasesViewModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Conversation_DBManager {

    public String DATABASE_NAME = "conversations.sqlite";
    static final int DATABASE_VERSION = 19;
    final Context context;
    SQLiteDatabase db;
    DatabaseHelper DBHelper;

    public Conversation_DBManager(Context ctx) {
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
    public Conversation_DBManager open() throws SQLException {
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
            DBHelper.close();

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

    public List<ConversationModel> getConversations() {
        List<ConversationModel> data = new ArrayList<>();
        String query = "select * from conversation";
        Cursor cursor = db.rawQuery(query, null);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                ConversationModel conversationModel = new ConversationModel();
                conversationModel.setId(cursor.getString(0));
                conversationModel.setName(cursor.getString(1));
                data.add(conversationModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<ConversationTypesModel> getConversationTypes(String id) {
        List<ConversationTypesModel> data = new ArrayList<>();
        String[] selection = {id};
        String query = "select * from ConversationTypes where conversation_id = ?";
        Cursor cursor = db.rawQuery(query, selection);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                ConversationTypesModel conversationTypesModel = new ConversationTypesModel();
                conversationTypesModel.setId(cursor.getString(0));
                conversationTypesModel.setType(cursor.getString(1));
                conversationTypesModel.setConversation_id(cursor.getString(2));
                data.add(conversationTypesModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<DialoguesModel> getDialogues(String id) {
        List<DialoguesModel> data = new ArrayList<>();
        String[] selection = {id};
        String query = "select * from Dialogues where conversationType_Id = ?";
        Cursor cursor = db.rawQuery(query, selection);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                DialoguesModel dialoguesModel = new DialoguesModel();
                dialoguesModel.setId(cursor.getString(0));
                dialoguesModel.setDialog(cursor.getString(1));
                dialoguesModel.setPerson_name(cursor.getString(2));
                dialoguesModel.setConversationType_Id(cursor.getString(3));
                data.add(dialoguesModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<PhrasesViewModel> getPhrases() {
        List<PhrasesViewModel> data = new ArrayList<>();
        String query = "select * from phrases";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (data.size() > 0) {
                data.clear();
            }
            if (cursor.moveToFirst()) {
                do {
                    PhrasesViewModel phrasesViewModel = new PhrasesViewModel();
                    phrasesViewModel.id = cursor.getString(0);
                    phrasesViewModel.eng_phrase = cursor.getString(1);
                    phrasesViewModel.urdu_phrase = cursor.getString(2);
                    data.add(phrasesViewModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<QuizModel> getAllQuiz() {
        List<QuizModel> data = new ArrayList<>();
        String query = "select * from quiztbl where practice_id = 1";
        Cursor cursor = db.rawQuery(query, null);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                QuizModel quizModel = new QuizModel();
                quizModel.setId(cursor.getString(0));
                quizModel.setQuestion(cursor.getString(1));
                quizModel.setOpt1(cursor.getString(2));
                quizModel.setOpt2(cursor.getString(3));
                quizModel.setOpt3(cursor.getString(4));
                quizModel.setOpt4(cursor.getString(5));
                quizModel.setOpt5(cursor.getString(6));
                quizModel.setAnswer(cursor.getString(7));
                data.add(quizModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }


    public List<QuizModel> getQuiz(final String id) {
        String[] selecction = {id};
        List<QuizModel> data = new ArrayList<>();
        String query = "select * from quiztbl where practice_id = 1 and id = ?";
        Cursor cursor = db.rawQuery(query, selecction);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                QuizModel quizModel = new QuizModel();
                quizModel.setId(cursor.getString(0));
                quizModel.setQuestion(cursor.getString(1));
                quizModel.setOpt1(cursor.getString(2));
                quizModel.setOpt2(cursor.getString(3));
                quizModel.setOpt3(cursor.getString(4));
                quizModel.setOpt4(cursor.getString(5));
                quizModel.setOpt5(cursor.getString(6));
                quizModel.setAnswer(cursor.getString(7));
                data.add(quizModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<TensesModel> getTensesNames(final String maintense) {
        List<TensesModel> data = new ArrayList<>();
        String[] selection = {maintense};
        String query = "select * from Tenses WHERE MainTense = ?";
        Cursor cursor = db.rawQuery(query, selection);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                TensesModel tensesModel = new TensesModel();
                tensesModel.setId(cursor.getString(0));
                tensesModel.setName(cursor.getString(1));
                tensesModel.setDef_urdu(cursor.getString(2));
                tensesModel.setMethod_urdu(cursor.getString(3));
                tensesModel.setDef_eng(cursor.getString(4));
                tensesModel.setMethod_eng(cursor.getString(5));
                tensesModel.setMain_tense(cursor.getString(6));
                data.add(tensesModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<TensesModel> getTenses(final String id) {
        List<TensesModel> data = new ArrayList<>();
        String[] selection = {id};
        String query = "select * from Tenses WHERE id = ?";
        Cursor cursor = db.rawQuery(query, selection);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                TensesModel tensesModel = new TensesModel();
                tensesModel.setId(cursor.getString(0));
                tensesModel.setName(cursor.getString(1));
                tensesModel.setDef_urdu(cursor.getString(2));
                tensesModel.setMethod_urdu(cursor.getString(3));
                tensesModel.setDef_eng(cursor.getString(4));
                tensesModel.setMethod_eng(cursor.getString(5));
                tensesModel.setMain_tense(cursor.getString(6));
                data.add(tensesModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<TensesModel> getTenses() {
        List<TensesModel> data = new ArrayList<>();
        String query = "select * from Tenses";
        Cursor cursor = db.rawQuery(query, null);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                TensesModel tensesModel = new TensesModel();
                tensesModel.setId(cursor.getString(0));
                tensesModel.setName(cursor.getString(1));
                tensesModel.setDef_urdu(cursor.getString(2));
                tensesModel.setMethod_urdu(cursor.getString(3));
                tensesModel.setDef_eng(cursor.getString(4));
                tensesModel.setMethod_eng(cursor.getString(5));
                data.add(tensesModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<PartOfSpeechModel> getPartsOfSpeech() {
        List<PartOfSpeechModel> data = new ArrayList<>();
        String query = "select * from partsofspeech";
        Cursor cursor = db.rawQuery(query, null);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                PartOfSpeechModel partOfSpeechModel = new PartOfSpeechModel();
                partOfSpeechModel.setId(cursor.getString(0));
                partOfSpeechModel.setName_eng(cursor.getString(1));
                partOfSpeechModel.setName_urdu(cursor.getString(2));
                partOfSpeechModel.setDef_eng(cursor.getString(3));
                partOfSpeechModel.setDef_urdu(cursor.getString(4));
                partOfSpeechModel.setEg_eng(cursor.getString(5));
                data.add(partOfSpeechModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<PartOfSpeechModel> getPartsOfSpeech(final String id) {
        List<PartOfSpeechModel> data = new ArrayList<>();
        String[] selection = {id};
        String query = "select * from partsofspeech where id = ?";
        Cursor cursor = db.rawQuery(query, selection);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                PartOfSpeechModel partOfSpeechModel = new PartOfSpeechModel();
                partOfSpeechModel.setId(cursor.getString(0));
                partOfSpeechModel.setName_eng(cursor.getString(1));
                partOfSpeechModel.setName_urdu(cursor.getString(2));
                partOfSpeechModel.setDef_eng(cursor.getString(3));
                partOfSpeechModel.setDef_urdu(cursor.getString(4));
                partOfSpeechModel.setEg_eng(cursor.getString(5));
                data.add(partOfSpeechModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<TensesExampleModel> getTenseExample(final String tenseid) {
        List<TensesExampleModel> data = new ArrayList<>();
        String[] selectiuon = {tenseid};
        String query = "select * from tenses_examples where tense_id = ?";
        Cursor cursor = db.rawQuery(query, selectiuon);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                TensesExampleModel tensesExampleModel = new TensesExampleModel();
                tensesExampleModel.setId(cursor.getString(0));
                tensesExampleModel.setUrduexample(cursor.getString(1));
                tensesExampleModel.setEnglishexample(cursor.getString(2));
                data.add(tensesExampleModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

}


