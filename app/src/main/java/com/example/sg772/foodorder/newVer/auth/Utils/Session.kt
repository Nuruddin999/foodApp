package com.example.sg772.foodorder.newVer.auth.Utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.sg772.foodorder.newVer.auth.userauth.User
import java.util.ArrayList
val S_NAME = "Sess"
val S_TABLE_NAME = "CURSESS"
val S_USER_PHONE="phone"
val S_COL_USER = "user"
open class Session(var context: Context): SQLiteOpenHelper(context,
    S_NAME, null,1){


    override fun onCreate(db: SQLiteDatabase?) {
val create="CREATE TABLE $S_TABLE_NAME ( $S_COL_USER VARCHAR(300), $S_USER_PHONE VARCHAR(300))"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
    fun insertDataSess(username:String, phone:String){

        val db=this.writableDatabase
        var cv= ContentValues()
        cv.put(S_COL_USER,username)
        cv.put(S_USER_PHONE,phone)

        var result=db.insert(S_TABLE_NAME,null,cv)
        if (result == -1.toLong()){
            Toast.makeText(context,"failed", Toast.LENGTH_LONG).show()
        } else {
            Log.d("SESSION","OK")}
    }
    fun readDataSess(): ArrayList<User> {
        var list= ArrayList<User>()
        val db=this.writableDatabase
        val query="select * from "+ S_TABLE_NAME
//+" where "+ COL_USER+"="+"'"+usr+"'"
        val result=db.rawQuery(query, null)
        if (result.moveToFirst()){
            do {
                var user= User()
                user.username=result.getString(result.getColumnIndex(COL_USER))
                user.username=result.getString(result.getColumnIndex(USER_PHONE))

                list.add(user)

            } while (result.moveToNext())
        }
        result.close()
        return list
    }
    fun deleteAllSess(){
        val db=this.writableDatabase
        val query="delete  from "+ S_TABLE_NAME
        db.execSQL(query)

    }

}