package com.retiredbrainiacs.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.retiredbrainiacs.R
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.more_pop_up.view.*


class MoreFragment : Fragment(){
    lateinit var v : View
    lateinit var v1 : View
    var dialog : Dialog ?= null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
v = inflater.inflate(R.layout.more_pop_up,container,false)
        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView
        v1.titletxt.text = "More"
        if(v1.btn_logout.visibility == View.VISIBLE) {
            v1.btn_logout.visibility = View.GONE
        }
work()
      // openDialog()
        return  v
    }
fun work(){
        v.lay_store_pop.setOnClickListener {
            fragmentManager!!.beginTransaction().replace(R.id.frame_layout,StoreFragment()).addToBackStack("more").commit()

        }
    v.lay_account_pop.setOnClickListener {
        fragmentManager!!.beginTransaction().replace(R.id.frame_layout,Account()).addToBackStack("more").commit()

    }
    }



    //private View pic;

     class MyDialogFragment : DialogFragment() {

     override  fun onCreateDialog(savedInstanceState: Bundle): Dialog {
            val view = activity!!.layoutInflater.inflate(R.layout.more_pop_up, LinearLayout(activity), false)



            // Build dialog
            val builder = Dialog(activity!!)


         //builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
           // builder.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            builder.setContentView(view)
            return builder
        }
    }
}