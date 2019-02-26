package com.example.sg772.foodorder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class RequestListClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val reqId=itemView.findViewById(R.id.request_list_item_id) as TextView
    val reqName=itemView.findViewById(R.id.request_list_item_name) as TextView
    val reqStatus=itemView.findViewById(R.id.request_list_item_status) as TextView
    val reqAddress=itemView.findViewById(R.id.request_list_item_address) as TextView
}