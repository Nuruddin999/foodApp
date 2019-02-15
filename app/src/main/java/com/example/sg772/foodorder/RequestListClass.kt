package com.example.sg772.foodorder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class RequestListClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val reqName=itemView.findViewById(R.id.request_list_item_name) as TextView
    val reqQuantity=itemView.findViewById(R.id.request_list_item_quantity) as TextView
    val reqPrice=itemView.findViewById(R.id.request_list_item_price) as TextView
    val reqStatus=itemView.findViewById(R.id.request_list_item_status) as TextView
    val reqTotal=itemView.findViewById(R.id.request_list_item_total) as TextView
}