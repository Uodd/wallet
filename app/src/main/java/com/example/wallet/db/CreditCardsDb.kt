package com.example.wallet.db

import android.content.ContentValues
import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.wallet.CREATE_TXT
import com.example.wallet.CardData
import com.example.wallet.UserData


object CreditCards : BaseColumns {
    const val TABLE_NAME = "cards"
    const val COLUMN_NUMBER = "number"
    const val COLUMN_SECURE = "secure"
    const val COLUMN_EXPIRE = "expire"
    const val COLUMN_USERID = "userid"
    const val COLUMN_PUBLIC = "public"
    const val COLUMN_BRAND = "brand"
}


private const val SQL_CREATE_CARDS =
    "CREATE TABLE ${CreditCards.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${CreditCards.COLUMN_NUMBER} BLOB," +
            "${CreditCards.COLUMN_SECURE} BLOB," +
            "${CreditCards.COLUMN_EXPIRE} BLOB," +
            "${CreditCards.COLUMN_USERID} BLOB," +
            "${CreditCards.COLUMN_BRAND} TEXT," +
            "${CreditCards.COLUMN_PUBLIC} TEXT)"

private const val SQL_DELETE_CARDS = "DROP TABLE IF EXISTS ${CreditCards.TABLE_NAME}"

class CreditCardsDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
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

class CardsDb(context: Context){
    private val db = CreditCardsDbHelper(context)

    fun save (card:CardData):Long?{
        val dbw= db.writableDatabase
        val values = ContentValues().apply {
            put(CreditCards.COLUMN_NUMBER,card.number)
            put(CreditCards.COLUMN_SECURE,card.secure)
            put(CreditCards.COLUMN_EXPIRE,card.expire)
            put(CreditCards.COLUMN_PUBLIC,card.public)
            put(CreditCards.COLUMN_USERID,card.userid)
            put(CreditCards.COLUMN_BRAND,card.brand)
        }
        val rowID= dbw?.insert(Users.TABLE_NAME,null,values)
        dbw.close()
        return rowID
    }
    fun getCards(userID:Int):MutableList<CardData>{
        val dbr= db.readableDatabase
        val cards= mutableListOf<CardData>()
        try{
            val selection =  "${CreditCards.COLUMN_USERID}=?"
            val selectionArgs= arrayOf(userID.toString())

            val cursor =dbr.query(
                Users.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
            )
            try{
                with(cursor){
                    while(moveToNext()){
                        val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                        val number = getBlob(getColumnIndexOrThrow(CreditCards.COLUMN_NUMBER)).toString()
                        val secure = getBlob(getColumnIndexOrThrow(CreditCards.COLUMN_SECURE)).toString()
                        val expire = getBlob(getColumnIndexOrThrow(CreditCards.COLUMN_EXPIRE)).toString()
                        val userid = getInt(getColumnIndexOrThrow(CreditCards.COLUMN_USERID))
                        val public = getString(getColumnIndexOrThrow(CreditCards.COLUMN_PUBLIC))
                        val brand = getString(getColumnIndexOrThrow(CreditCards.COLUMN_BRAND))

                        cards.add(CardData(id,number,secure,expire,userid,public,brand))
                    }
                }
            }catch (e:IllegalArgumentException){
                Log.v("EXCEPTION",e.toString())
                dbr.close()
            }
        }catch (e: CursorIndexOutOfBoundsException){
            Log.v("EXCEPTION",e.toString())
            dbr.close()
        }
        return cards

    }
}