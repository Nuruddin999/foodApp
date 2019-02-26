package com.example.sg772.foodorder.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.sg772.foodorder.Model.Order
import com.google.firebase.auth.FirebaseAuth
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import java.util.ArrayList

val DB_NAME = "Order"
val TABLE_NAME = "cart"
val COL_USER = "user"
val COL_PRODUCTID = "id"
val COL_PRODUCTNAME = "ProductName"
val COL_PRODUCTQUANTITY = "Quantity"
val COL_PRODUCTPRICE = "Price"
val COL_PRODUCTDISCOUNT = "Discount"

open class DBHelper(var context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTAble = " CREATE TABLE " + TABLE_NAME +
                "(" +
                COL_PRODUCTID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USER + " VARCHAR (300)," +
                COL_PRODUCTNAME + " VARCHAR (300)," +
                COL_PRODUCTQUANTITY + " VARCHAR (300)," +
                COL_PRODUCTPRICE + " VARCHAR (300)," +
                COL_PRODUCTDISCOUNT + " VARCHAR (300)"+
                ")"
        db?.execSQL(createTAble)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    fun insertData(order: Order){

        val db=this.writableDatabase
        var cv=ContentValues()
        cv.put(COL_USER,order.User)
        cv.put(COL_PRODUCTNAME,order.ProductName)
        cv.put(COL_PRODUCTQUANTITY,order.Quantity)
        cv.put(COL_PRODUCTPRICE,order.Price)
        cv.put(COL_PRODUCTDISCOUNT,order.Discount)
        var result=db.insert(TABLE_NAME,null,cv)
        if (result == -1.toLong()){
            Toast.makeText(context,"failed",Toast.LENGTH_LONG).show()
        } else {Toast.makeText(context,"success",Toast.LENGTH_LONG).show()}
    }
    fun readData(): ArrayList<Order>{
        var list=ArrayList<Order>()
        val db=this.writableDatabase
val usr: String? = FirebaseAuth.getInstance().currentUser!!.email
val query="select * from "+ TABLE_NAME+" where "+ COL_USER+"="+"'"+usr+"'"
        val result=db.rawQuery(query, null)
        if (result.moveToFirst()){
            do {
                var order=Order()
                order.ProductID=result.getString(result.getColumnIndex(COL_PRODUCTID)).toInt()
                order.User=result.getString(result.getColumnIndex(COL_USER))
                order.ProductName=result.getString(result.getColumnIndex(COL_PRODUCTNAME))
                order.Quantity=result.getString(result.getColumnIndex(COL_PRODUCTQUANTITY))
                order.Price=result.getString(result.getColumnIndex(COL_PRODUCTPRICE))
                order.Discount= result.getString(result.getColumnIndex(COL_PRODUCTDISCOUNT))
                list.add(order)

            } while (result.moveToNext())
        }
        result.close()
        return list
    }
    fun deleteData(name: String){
        val db=this.writableDatabase
        val query="delete  from "+ TABLE_NAME+ " where "+ COL_PRODUCTNAME+"="+"'"+name+"'"
        val result=db.execSQL(query)
    }
    fun deleteAll(){
        val db=this.writableDatabase
        val query="delete  from "+ TABLE_NAME
        db.execSQL(query)

    }
    }
