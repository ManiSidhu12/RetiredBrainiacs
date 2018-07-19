package com.retiredbrainiacs.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.SimpleExpandableAdapter
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.Global
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.model.SimpleChild
import com.retiredbrainiacs.model.SimpleParentItem
import com.retiredbrainiacs.model.language.LanguageRoot
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.language_screen.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.StringReader
import java.util.ArrayList

class Languages : AppCompatActivity(){
    lateinit  var listDataHeader: ArrayList<String>
    var listDataChild: HashMap<String, ArrayList<HashMap<String,String>>> ?= null
lateinit var global : Global
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.language_screen)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        var v = supportActionBar!!.customView
        v.titletxt.text = "Language"

        recycler_language.layoutManager = LinearLayoutManager(this@Languages)
global = Global()


        if(CommonUtils.getConnectivityStatusString(this@Languages).equals("true")) {
            getLanguage()
        }
        else{
            CommonUtils.openInternetDialog(this@Languages)
        }

btn_skip1_language.setOnClickListener {
startActivity(Intent(this@Languages,Interests::class.java))
}
        //  prepareListData()

    }
    private fun prepareListData() {
        listDataHeader = ArrayList<String>()
      //  listDataChild = HashMap<String,ArrayList<String>>()

        // Adding child data
        listDataHeader.add("Known Language")
        listDataHeader.add("Preferred Language")
        listDataHeader.add("Spoken Language")

        // Adding child data
        val list1 = ArrayList<String>()
        list1.add("Arabic")
        list1.add("Chinese")
        list1.add("English")
        list1.add("Farsi")



       /* listDataChild!!.put(listDataHeader.get(0), list1) // Header, Child data
        listDataChild!!.put(listDataHeader.get(1), list1)
        listDataChild!!.put(listDataHeader.get(2), list1)*/
    }

    private fun generateMockData(): List<ParentListItem> {
        val parentListItems = ArrayList<ParentListItem>()
        for (i in 0 until listDataHeader.size) {
            val simpleParentItem = SimpleParentItem()
            simpleParentItem.setTitle(listDataHeader[i])
           // simpleParentItem.setId(root.getChildren().get(i).getId())

            val childItemList = ArrayList<SimpleChild>()
            if (listDataHeader!= null) {
                for (j in 0 until global.listDataChild!!.get(listDataHeader.get(i))!!.size) {

            childItemList.add(SimpleChild(global.listDataChild.get(listDataHeader.get(i))!!.get(j).get("key_title"),global.listDataChild!!.get(listDataHeader.get(i))!!.get(j).get("key_value"),global.listDataChild!!.get(listDataHeader.get(i))!!.get(j).get("chked")))
                    simpleParentItem.setChildItemList(childItemList)
                }
            } else {
            }
            parentListItems.add(simpleParentItem)

        }
        return parentListItems
    }

    //===== Get Language API =====
    private fun getLanguage(){
        var url = GlobalConstants.API_URL+"list_language"
        progress_lang.visibility = View.VISIBLE
        recycler_language.visibility = View.GONE
        lay_bottom_lang.visibility = View.GONE

        val postRequest = object : StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            progress_lang.visibility = View.GONE
            recycler_language.visibility = View.VISIBLE
            lay_bottom_lang.visibility = View.VISIBLE
        var obj = JSONObject(response)

            if(obj.getString("status").equals("true")){
                val arr = obj.getJSONArray("languages")
                var obj1 : JSONObject ?= null
                for(i in 0 until arr.length()){
                    obj1 = arr.getJSONObject(i)
                }
                listDataHeader = ArrayList<String>()
                listDataChild = HashMap<String, ArrayList<HashMap<String,String>>> ()

                val iterator = obj1!!.keys()

                while (iterator.hasNext()) {
                    val key = iterator.next() as String
                   val arr1 = obj1.getJSONArray(key)
                    var map : HashMap<String,String> ?= null
                    val listValues = ArrayList<HashMap<String, String>>()

                    for(i in 0 until arr1.length()){
                        val obj2 = arr1.getJSONObject(i)
                         map = HashMap<String, String>()
                        map.put("key_value",obj2.getString("key_value"))
                        map.put("key_title",obj2.getString("key_title"))
                        map.put("chked",obj2.getString("chked"))

                    }
                    listValues.add(map!!)

                    listDataHeader.add(key)
                    global.listValues = listValues

                  // Log.e("map",listValues.toString())


                }
                for(i in 0 until listDataHeader.size) {
                    Log.e("header",listDataHeader.get(i))
                    listDataChild!!.put(listDataHeader.get(i), global.listValues)
                    Log.e("list12",listDataChild.toString())


                }
                global.listDataChild = listDataChild
                Log.e("list",global.listDataChild.toString())
                recycler_language.adapter = SimpleExpandableAdapter(this@Languages,generateMockData())

            }
        },

                Response.ErrorListener {
                    progress_lang.visibility = View.GONE
                    recycler_language.visibility = View.GONE
                    lay_bottom_lang.visibility = View.GONE }) {

        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}