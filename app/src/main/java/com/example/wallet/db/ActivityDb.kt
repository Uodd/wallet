package com.example.wallet.db

import android.content.ContentValues
import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.wallet.ActivityData

object Activities : BaseColumns {
    const val TABLE_NAME = "activity"
    const val COLUMN_STATUS = "status"
    const val COLUMN_TIME = "time"
    const val COLUMN_USER_ID ="userid"
}

private const val SQL_CREATE_ACTIVITY =
    "CREATE TABLE ${Activities.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${Activities.COLUMN_STATUS} INTEGER," +
            "${Activities.COLUMN_USER_ID} INTEGER," +
            "${Activities.COLUMN_TIME} TEXT)"

private const val SQL_DELETE_ACTIVITY = "DROP TABLE IF EXISTS ${Activities.TABLE_NAME}"

class ActivityDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ACTIVITY)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ACTIVITY)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ActivityData.db"
    }
}

class ActivityDb(context: Context){
    private val db = ActivityDbHelper(context)
    fun save(activity : ActivityData){
        val dbw= db.writableDatabase
        val values = ContentValues().apply {
            put(Activities.COLUMN_USER_ID,activity.userid)
            put(Activities.COLUMN_STATUS,activity.status)
            put(Activities.COLUMN_TIME,activity.time)
        }
        val rowID= dbw?.insert(Activities.TABLE_NAME,null,values)
        dbw.close()
    }
    //SELECT * FROM table ORDER BY column DESC LIMIT 1;
    //TODO a little order wouldn't hurt
    fun getLast(): ActivityData?{
        val dbr= db.readableDatabase
        var activity : ActivityData?=null
        try{
            val cursor = dbr.query(
                Activities.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "${Activities.COLUMN_TIME} DESC LIMIT 1"
            )
            try{
                with(cursor) {
                    moveToNext()
                    val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                    val userid =getInt(getColumnIndexOrThrow(Activities.COLUMN_USER_ID))
                    val status = getInt(getColumnIndexOrThrow(Activities.COLUMN_STATUS))
                    val time = getString(getColumnIndexOrThrow(Activities.COLUMN_TIME))

                    activity= ActivityData(id,userid,status,time)
                }
            }catch (e:IllegalArgumentException){
                Log.v("EXCEPTION",e.toString())
                dbr.close()
                return null
            }
            cursor.close()
        }catch (e: CursorIndexOutOfBoundsException){
            Log.v("EXCEPTION",e.toString())
            dbr.close()
            return null
        }
        dbr.close()
        return activity
    }

}


