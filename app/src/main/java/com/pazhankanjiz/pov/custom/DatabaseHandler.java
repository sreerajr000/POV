package com.pazhankanjiz.pov.custom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pazhankanjiz.pov.model.AnswerModel;
import com.pazhankanjiz.pov.model.QuestionModel;

import static com.pazhankanjiz.pov.constant.DatabaseConstants.*;
public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + ID + " VARCHAR PRIMARY KEY," + CONTENT + " VARCHAR(255),"
                + POSTED_BY + " VARCHAR," + BACKGROUND + " INTEGER,"
                + LIKE + " INTEGER," + DISLIKE + " INTEGER," + RATING
                + " INTEGER," + HASHTAG + " VARCHAR" + ")";

        String CREATE_ANSWERS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + ID + " VARCHAR PRIMARY KEY," + CONTENT + " VARCHAR(255),"
                + POSTED_BY + " VARCHAR," + LIKE + " INTEGER," + DISLIKE
                + " INTEGER," + RATING + " INTEGER"+ ")";

        db.execSQL(CREATE_QUESTIONS_TABLE);
        db.execSQL(CREATE_ANSWERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);

        // Create tables again
        onCreate(db);
    }


    public void addAnswer(AnswerModel answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, answer.getId());
        values.put(CONTENT, answer.getContent());
        values.put(POSTED_BY, answer.getPostedBy());
        values.put(LIKE, answer.getLikeCount());
        values.put(DISLIKE, answer.getDislikeCount());
        values.put(RATING, answer.getRating());

        db.insert(TABLE_ANSWERS, null, values);
        db.close();
    }

    public AnswerModel getAnswer(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ANSWERS, new String[] { ID,
                        CONTENT, POSTED_BY, LIKE, DISLIKE, RATING}, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AnswerModel answer = new AnswerModel(cursor.getString(1),
                cursor.getString(0), cursor.getString(2), cursor.getLong(3),
                cursor.getLong(4), cursor.getInt(5));
        return answer;
    }

    public void addQuestion(QuestionModel question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, question.getId());
        values.put(CONTENT, question.getContent());
        values.put(POSTED_BY, question.getPostedBy());
        values.put(BACKGROUND, question.getBackground());
        values.put(LIKE, question.getLikeCount());
        values.put(DISLIKE, question.getDislikeCount());
        values.put(RATING, question.getRating());
        values.put(HASHTAG, question.getHashtag());

        db.insert(TABLE_QUESTIONS, null, values);
        db.close();
    }

    public QuestionModel getQuestion(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTIONS, new String[] { ID,
                        CONTENT, POSTED_BY, BACKGROUND, LIKE, DISLIKE, RATING, HASHTAG }, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        QuestionModel question = new QuestionModel(cursor.getString(1),
                cursor.getString(0), cursor.getString(2), cursor.getString(3),
                cursor.getLong(4), cursor.getLong(5), cursor.getInt(6),
                cursor.getString(7));
        return question;
    }


  /*  // code to add the new contact
    // code to get the single contact


    // code to get all contacts in a list view
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
*/
}