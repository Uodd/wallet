package com.example.wallet

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


object CreditCards : BaseColumns {
    const val TABLE_NAME = "cards"
    const val COLUMN_NUMBER = "number"
    const val COLUMN_SECURE = "secure"
    const val COLUMN_EXPIRE = "expire"
    const val COLUMN_USERID = "userid"
    const val COLUMN_PUBLIC = "public"
}


private const val SQL_CREATE_CARDS =
    "CREATE TABLE ${CreditCards.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${CreditCards.COLUMN_NUMBER} BLOB," +
            "${CreditCards.COLUMN_SECURE} BLOB," +
            "${CreditCards.COLUMN_EXPIRE} BLOB," +
            "${CreditCards.COLUMN_USERID} BLOB," +
            "${CreditCards.COLUMN_PUBLIC} TEXT)"

private const val SQL_DELETE_CARDS = "DROP TABLE IF EXISTS ${CreditCards.TABLE_NAME}"

class CreditCardsDb(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_CARDS)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_CARDS)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "creditCards.db"
    }
}
