package com.example.storyex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.storyex.QuizContract;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GoQuiz";
    public static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;

        final String SQL_CRATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuizContract.QuestionTable.TABLE_NAME + " ( " +
                QuizContract.QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.QuestionTable.COLUMN_STORY + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION4 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_ANSWER_NR + " INTEGER " +
                " ) ";

        db.execSQL(SQL_CRATE_QUESTIONS_TABLE);
        fillQuestionsTable(); //insert data to table
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionTable.TABLE_NAME);
    }

    private void addQuestions(Questions questions){
        ContentValues cv = new ContentValues();
        cv.put(QuizContract.QuestionTable.COLUMN_STORY, questions.getStory());
        cv.put(QuizContract.QuestionTable.COLUMN_QUESTION, questions.getQuestion());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION1, questions.getOption1());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION2, questions.getOption2());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION3, questions.getOption3());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION4, questions.getOption4());
        cv.put(QuizContract.QuestionTable.COLUMN_ANSWER_NR, questions.getAnswerNr());

        db.insert(QuizContract.QuestionTable.TABLE_NAME, null, cv);


    }

    private void fillQuestionsTable(){

        Questions q1 = new Questions("blablabla", "What is next ?", "done", "nope", "bla", "num", 1);
        addQuestions(q1);

        Questions q2 = new Questions("blablabla", "Nope done yet ?", "nope", "done", "bla", "num", 1);
        addQuestions(q2);

        Questions q3 = new Questions("blablabla", "easy qustions ?", "bla", "nope", "hahh", "num", 1);
        addQuestions(q3);

        Questions q4 = new Questions("blablabla", "whats wrong ?", "num", "nope", "nope", "bla", 1);
        addQuestions(q4);

    }

    public ArrayList<Questions> getAllQuestions(){
        ArrayList<Questions> questionsList = new ArrayList<>();

        db = getReadableDatabase();
        String Projection[] = {
                QuizContract.QuestionTable._ID,
                QuizContract.QuestionTable.COLUMN_STORY,
                QuizContract.QuestionTable.COLUMN_QUESTION,
                QuizContract.QuestionTable.COLUMN_OPTION1,
                QuizContract.QuestionTable.COLUMN_OPTION2,
                QuizContract.QuestionTable.COLUMN_OPTION3,
                QuizContract.QuestionTable.COLUMN_OPTION4,
                QuizContract.QuestionTable.COLUMN_ANSWER_NR,
        };

        Cursor data = db.query(QuizContract.QuestionTable.TABLE_NAME,
                Projection,
                null,
                null,
                null,
                null,
                null);

        if (data.moveToFirst()){
            do {
                Questions question = new Questions();
                question.setStory(data.getString(data.getColumnIndex(QuizContract.QuestionTable.COLUMN_STORY)));
                question.setQuestion(data.getString(data.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                question.setOption1(data.getString(data.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION1)));
                question.setOption2(data.getString(data.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION2)));
                question.setOption3(data.getString(data.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION3)));
                question.setOption4(data.getString(data.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION4)));
                question.setAnswerNr(data.getInt(data.getColumnIndex(QuizContract.QuestionTable.COLUMN_ANSWER_NR)));

                questionsList.add(question);
            }while (data.moveToNext());

        }
        data.close();   //close cursor
        return questionsList;
    }

}
