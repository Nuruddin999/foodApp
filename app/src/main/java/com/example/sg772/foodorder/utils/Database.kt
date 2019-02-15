package com.example.sg772.foodorder.utils

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import com.example.sg772.foodorder.Model.Order

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import java.util.ArrayList

open class Database(context: Context?) :
    SQLiteAssetHelper(context,
        db_name, null,
        db_ver
    ){
    companion object {
        const val db_name="databasesSQLite.db"
        const val db_ver=1

    }
    fun getCarts(): ArrayList<Order>{
        var db:SQLiteDatabase=readableDatabase
        var queryBuilder=SQLiteQueryBuilder()
        var sqlSelect= arrayOf("ProductID","ProductName","Quantity","Price","Discount")
        var orderDetailTable: String="Order_details"
queryBuilder.tables=orderDetailTable
        var cursor: Cursor=queryBuilder.query(db, sqlSelect, null,null,null,null,null)
        val result=ArrayList<Order>()
        if (cursor.moveToFirst()){
        /*    do {
                result.add(object : Order(cursor.getInt(
                    cursor.getColumnIndex("ProductID")),
                    cursor.getString(cursor.getColumnIndex("ProductName")),
                    cursor.getString(cursor.getColumnIndex("Quantity")),
                    cursor.getString(cursor.getColumnIndex("Price")),
                    cursor.getString(cursor.getColumnIndex("Discount"))
                )
                {})

            } while (cursor.moveToNext())*/
        }
        return result
    }
    fun addToCart(order:Order): Unit {
        var db:SQLiteDatabase=readableDatabase

        var query:String= String.format("INSERT INTO Order_details(ProductID, ProductName,Quantity,Price,Discount) VALUES ('%s','%s','%s','%s','%s')", order.ProductID,
            order.ProductName,
            order.Quantity,
            order.Price,
            order.Discount)
        db.execSQL(query)
    }
    fun cleanCart(){
        var db:SQLiteDatabase=readableDatabase
        var query: String= String.format("DELETE FROM OrderDetail")
        db.execSQL(query)
    }
}