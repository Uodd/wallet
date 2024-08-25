package com.example.wallet.db

import android.content.ContentValues
import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import androidx.compose.ui.node.LayoutModifierNode
import androidx.room.util.TableInfo
import com.example.wallet.UserData


object Users : BaseColumns {
    const val TABLE_NAME = "usersData"
    const val COLUMN_FIRST_NAME = "firstname"
    const val COLUMN_LAST_NAME = "lastname"
    const val COLUMN_SALDO = "saldo"
    const val COLUMN_TIME = "time"

}

private const val SQL_CREATE_USERS =
    "CREATE TABLE ${Users.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${Users.COLUMN_FIRST_NAME} TEXT," +
            "${Users.COLUMN_TIME} TEXT," +
            "${Users.COLUMN_SALDO} REAL," +
            "${Users.COLUMN_LAST_NAME} TEXT)"//Creation time

private const val SQL_DELETE_USERS = "DROP TABLE IF EXISTS ${Users.TABLE_NAME}"

class UsersDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
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
class UsersDb(context: Context){
    private val db = UsersDbHelper(context)

    //TODO ADD CHECK IF: USER ALREADY EXISTS
    fun save(user: UserData):Long?{
        val dbw= db.writableDatabase
        val values = ContentValues().apply {
            put(Users.COLUMN_FIRST_NAME,user.firstName)
            put(Users.COLUMN_LAST_NAME,user.lastName)
            put(Users.COLUMN_SALDO,user.saldo)
            put(Users.COLUMN_TIME,user.time)
        }
        val rowID= dbw?.insert(Users.TABLE_NAME,null,values)
        dbw.close()
        Log.v("INFO","Saved $user")
        return rowID
    }

    fun getUsers(ID:Int?=null):MutableList<UserData>{
        val dbr= db.readableDatabase
        val TAG="GET USERS"
        Log.v(TAG,"0")
        val users= mutableListOf<UserData>()
        try{
            val selection = if (ID==null) null else "${BaseColumns._ID} = ? "
            val selectionArgs= if (ID==null) null else arrayOf(ID.toString())

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
                        val ide = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                        val firstname = getString(getColumnIndexOrThrow(Users.COLUMN_FIRST_NAME))
                        val lastname = getString(getColumnIndexOrThrow(Users.COLUMN_LAST_NAME))
                        val time = getString(getColumnIndexOrThrow(Users.COLUMN_TIME))
                        val saldo = getFloat(getColumnIndexOrThrow(Users.COLUMN_SALDO))

                        users.add(UserData(ide,firstname,lastname,saldo,time))
                        Log.v("AAAA",UserData(ide,firstname,lastname,saldo,time).toString())
                    }
                }
                cursor.close()

            }catch (e:IllegalArgumentException){
                Log.v("EXCEPTION",e.toString())
                cursor.close()
                dbr.close()
            }
            cursor.close()
        }catch (e: CursorIndexOutOfBoundsException){
            Log.v("EXCEPTION",e.toString())
            dbr.close()
        }
        Log.v("INFO","Get $users")
        return users
    }
}

