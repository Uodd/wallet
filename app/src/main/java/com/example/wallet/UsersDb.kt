package com.example.wallet

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


object Users : BaseColumns {
    const val TABLE_NAME = "usersData"
    const val COLUMN_FIRST_NAME = "firstname"
    const val COLUMN_LAST_NAME = "lastname"
}


private const val SQL_CREATE_USERS =
    "CREATE TABLE ${Users.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${Users.COLUMN_FIRST_NAME} TEXT," +
            "${Users.COLUMN_LAST_NAME} TEXT)"

private const val SQL_DELETE_USERS = "DROP TABLE IF EXISTS ${Users.TABLE_NAME}"

class UsersDb(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USERS)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_USERS)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "UsersData.db"
    }

}

