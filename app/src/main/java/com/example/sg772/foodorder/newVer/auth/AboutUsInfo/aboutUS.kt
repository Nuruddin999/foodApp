package com.example.sg772.foodorder.newVer.auth.AboutUsInfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.sg772.foodorder.R
import com.example.sg772.foodorder.newVer.auth.userauth.authActivity
import com.example.sg772.foodorder.utils.Session
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class aboutUS: Fragment() {
lateinit var call_button: android.support.v7.widget.CardView
    lateinit var find_button:CardView
    lateinit var message_buttin:CardView
    lateinit var username:TextView
    lateinit var logout:ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var View: View = inflater.inflate(R.layout.about_us_fragment, container, false)
        var db=Session(context!!)
        var us=db.readDataSess()
        logout=View.findViewById(R.id.log_out)
        logout.setOnClickListener {
            db.deleteAllSess()
            var intent=Intent(context,authActivity::class.java)
            startActivity(intent)
        }
        username=View.findViewById(R.id.user_name_about)
        for (u in us){
            username.setText(u.username)
        }
call_button=View.findViewById(R.id.call_button)
        find_button=View.findViewById(R.id.find_button)
        message_buttin=View.findViewById(R.id.message_button)
        call_button.setOnClickListener {
            var intent=Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:+79884425157"))
            startActivity(intent)
        }
        find_button.setOnClickListener {
            var addres=String.format(Locale.ENGLISH, "geo:%f,%f", 51.5231202,0.1119507 )
            var intent=Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://www.google.com/maps/place/Sushi+Rolls+-+Sushi+Catering,+Chef+Hire+and+Sushi+Platter+Delivery/@52.567428,-3.8010239,7z/data=!4m8!1m2!2m1!1sfood+delivery+london!3m4!1s0x48761b630fdcb375:0xc4bda11b268974fb!8m2!3d51.523439!4d-0.111693"))
            startActivity(intent)
        }
        message_buttin.setOnClickListener {
            var messageDialogFragment=messageDialogFragment()
            messageDialogFragment.show(fragmentManager,"")
        }
        return View
    }
}