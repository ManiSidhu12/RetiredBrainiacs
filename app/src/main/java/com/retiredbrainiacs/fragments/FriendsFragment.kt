package com.retiredbrainiacs.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.ChatListing
import com.retiredbrainiacs.adapters.AdapterFriends
import com.retiredbrainiacs.adapters.FriendsAdapter
import com.retiredbrainiacs.adapters.RequestAdapter
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.common.EventListener
import com.retiredbrainiacs.model.friend.AllFriendRoot
import com.retiredbrainiacs.model.friend.CommentCount
import com.retiredbrainiacs.model.friend.ListAll
import com.retiredbrainiacs.model.friend.ListFriend
import kotlinx.android.synthetic.main.all_classified_screen.view.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.friends_screen.view.*
import retrofit2.Retrofit
import java.io.StringReader
import java.util.*
import kotlin.collections.ArrayList

class FriendsFragment : Fragment(){
    lateinit var v : View
    lateinit var v1 : View
    lateinit var listener: EventListener
    //============== Retrofit =========
    lateinit var retroFit: Retrofit
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    lateinit var root : AllFriendRoot
    lateinit var adap : FriendsAdapter
    //==============
    var listFriends : MutableList<ListAll> = ArrayList()
    var listRequest : MutableList<CommentCount> = ArrayList()
    var listAccept : MutableList<ListFriend> = ArrayList()
    var fragType = "all"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.friends_screen,container,false)

        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView
        if(v1.btn_logout.visibility == View.VISIBLE) {
            v1.btn_logout.visibility = View.GONE
        }
        v1.btn_edit.visibility = View.GONE
        v1.titletxt.text = "Friends"


        Common.setFontRegular(activity!!,v1.titletxt)

        //========
        Common.setFontRegular(activity!!,v.txt_all_friend)
        Common.setFontRegular(activity!!,v.txt_request)
        Common.setFontRegular(activity!!,v.txt_accept)
        Common.setFontEditRegular(activity!!,v.edt_srch_friends)
        //========
        v.recycler_friends.layoutManager = LinearLayoutManager(activity!!)

        showSelection(v.txt_all_friend,v.img_all_friend,v.txt_request,v.txt_accept,v.img_request,v.img_accept,v.lay_all_friend,v.lay_request_friend,v.lay_accept_friend)
        //fragmentManager!!.beginTransaction().replace(R.id.frame_friends,AllFriendsFragment()).commit()
fragType = "all"

        work()
        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        retroFit = ApiClient.getClient()
        gson = Gson()
        //====================

        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getAllUsersAPI()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }

        return  v
    }


    fun work(){
        v.edt_srch_friends.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

               filter(v.edt_srch_friends.text.toString().toLowerCase(Locale.getDefault()))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        v.img_msg_friends.setOnClickListener {

            startActivity(Intent(activity!!, ChatListing::class.java))

        }
        v.lay_all_friend.setOnClickListener {
            fragType = "all"
            showSelection(v.txt_all_friend, v.img_all_friend, v.txt_request, v.txt_accept, v.img_request, v.img_accept, v.lay_all_friend, v.lay_request_friend, v.lay_accept_friend)
          //  fragmentManager!!.beginTransaction().replace(R.id.frame_friends,AllFriendsFragment()).commit()
            if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
                getAllUsersAPI()
            }
            else{
                CommonUtils.openInternetDialog(activity)
            }
        }
        v.lay_request_friend.setOnClickListener {
            fragType ="request"
            showSelection(v.txt_request, v.img_request, v.txt_all_friend, v.txt_accept, v.img_all_friend, v.img_accept,  v.lay_request_friend, v.lay_all_friend,v.lay_accept_friend)
            if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
                getRequestAPI()
            }
            else{
                CommonUtils.openInternetDialog(activity)
            }
         //   fragmentManager!!.beginTransaction().replace(R.id.frame_friends,RequestsFragment()).commit()

        }
        v.lay_accept_friend.setOnClickListener {
            fragType="friends"
            showSelection(v.txt_accept, v.img_accept, v.txt_all_friend, v.txt_request, v.img_all_friend, v.img_request, v.lay_accept_friend,v.lay_all_friend, v.lay_request_friend)
           // fragmentManager!!.beginTransaction().replace(R.id.frame_friends,Friends()).commit()
            if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
                getAllFriendsAPI()
            }
            else{
                CommonUtils.openInternetDialog(activity)
            }
        }
    }
    fun showSelection(txtSelected: TextView, imgSelected: ImageView, txt2: TextView, txt3: TextView, img2: ImageView, img3: ImageView, laySelected: RelativeLayout, lay1: RelativeLayout, lay2: RelativeLayout){
        txtSelected.setTextColor(resources.getColor(R.color.theme_color_orange))
        imgSelected.setColorFilter(ContextCompat.getColor(activity!!, R.color.theme_color_orange))
        laySelected.setBackgroundColor(Color.parseColor("#FFF5EF"))
        txt2.setTextColor(resources.getColor(R.color.black))
        txt3.setTextColor(resources.getColor(R.color.black))
        img2.setColorFilter(ContextCompat.getColor(activity!!, R.color.black))
        img3.setColorFilter(ContextCompat.getColor(activity!!, R.color.black))
        lay1.setBackgroundColor(Color.parseColor("#FFFFFF"))
        lay2.setBackgroundColor(Color.parseColor("#FFFFFF"))

    }
    //======= Get all Users API ====
    fun getAllUsersAPI() {

        var url = GlobalConstants.API_URL+"show_all_users"
        val pd = ProgressDialog.show(activity, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            v.recycler_friends.visibility = View.VISIBLE
            Log.e("response",response)
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<AllFriendRoot>(reader, AllFriendRoot::class.java)

            if (root != null) {
                if (root.status.equals("true")) {
                    v.recycler_friends.layoutManager = LinearLayoutManager(activity!!)
                    if(root.listAll != null && root.listAll.size > 0) {
                        v.recycler_friends.visibility = View.VISIBLE

                        adap = FriendsAdapter(activity!!, root.listAll, service, retroFit, gson)
                        v.recycler_friends.adapter = adap
                    }
                    else{
                        v.recycler_friends.visibility = View.GONE

                    }
                } else {
                    Common.showToast(activity!!, "No User Found...")
                    v.recycler_friends.visibility = View.GONE

                    //  v.recycler_feed.adapter = FeedsAdapter(activity!!,t.posts)

                }
            }
        },

                Response.ErrorListener { pd.dismiss()
                    v.recycler_friends.visibility = View.VISIBLE}) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(activity).userId
                Log.e("map all users", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)
    }

    //=== get requests api====
    fun getRequestAPI() {

        var url = GlobalConstants.API_URL+"pending_friend_request"
        val pd = ProgressDialog.show(activity, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            v.recycler_friends.visibility = View.VISIBLE

            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
             root = gson.fromJson<AllFriendRoot>(reader, AllFriendRoot::class.java)

            if (root != null) {
                if (root.status.equals("true")) {
                    v.recycler_friends.layoutManager = LinearLayoutManager(activity!!)
                    if (root.commentCount != null && root.commentCount.size > 0) {
                        v.recycler_friends.adapter = RequestAdapter(activity!!, root.commentCount, service, retroFit, gson)
                    }
                    else{
                        v.recycler_friends.visibility = View.GONE
                    }
                } else {
                    Common.showToast(activity!!, root.message)
                    v.recycler_friends.visibility = View.GONE

                    //  v.recycler_feed.adapter = FeedsAdapter(activity!!,t.posts)

                }
            }
        },

                Response.ErrorListener { pd.dismiss()
                    v.recycler_friends.visibility = View.VISIBLE}) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(activity).userId
                Log.e("map request", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)
    }
    //======= Get all Friends API ====
    fun getAllFriendsAPI() {
        var url = GlobalConstants.API_URL+"show_all_friend"
        val pd = ProgressDialog.show(activity, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            v.recycler_friends.visibility = View.VISIBLE

            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
             root = gson.fromJson<AllFriendRoot>(reader, AllFriendRoot::class.java)

            if (root != null) {
                if (root.status.equals("true")) {
                    v.recycler_friends.layoutManager = LinearLayoutManager(activity!!)
                    if(root.listFriends!= null && root.listFriends.size > 0) {
                        v.recycler_friends.adapter = AdapterFriends(activity!!, root.listFriends, service, retroFit, gson)
                    }
                    else{
                        v.recycler_friends.visibility = View.GONE

                    }
                } else {
                    Common.showToast(activity!!, "No Friend Found...")
                    v.recycler_friends.visibility = View.GONE

                    //  v.recycler_feed.adapter = FeedsAdapter(activity!!,t.posts)

                }
            }
        },

                Response.ErrorListener { pd.dismiss()
                    v.recycler_friends.visibility = View.VISIBLE
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(activity).userId
                Log.e("map all friend", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)
    }


    //******* Implementing filter method to search text in list
    fun filter(text:String) {
        // text = text.toLowerCase(Locale.getDefault())
        listFriends = ArrayList()
        listRequest = ArrayList()
        listAccept= ArrayList()
        if (text.length == 0)
        {
            listFriends = ArrayList()
            listRequest = ArrayList()
            listAccept = ArrayList()
            if(fragType.equals("all")) {
                if(root.listAll !=null) {
                    listFriends.addAll(root.listAll)
                }
            }
            else if(fragType.equals("request")) {
                if(root.commentCount !=null) {

                    listRequest.addAll(root.commentCount)
                }
            }
            else{
                if(root.listFriends != null) {
                    listAccept.addAll(root.listFriends)
                }
            }
        }
        else
        {
            listFriends = ArrayList()
            listRequest = ArrayList()
            listAccept = ArrayList()
            if(fragType.equals("all")) {
                if(root.listAll != null && root.listAll.size > 0) {
                    for (i in 0 until root.listAll.size) {
                        if (root.listAll[i].displayName.toLowerCase(Locale.getDefault()).contains(text)) {
                            var m: ListAll
                            m = root.listAll[i]
                            listFriends.add(m)
                        }
                    }
                }
            }
            else if(fragType.equals("request")) {
                if(root.commentCount != null && root.commentCount.size > 0) {
                    for (i in 0 until root.commentCount.size) {
                        if (root.commentCount[i].displayName.toLowerCase(Locale.getDefault()).contains(text)) {
                            var m: CommentCount
                            m = root.commentCount[i]
                            listRequest.add(m)
                        }
                    }

                }
            }
            else{
                if(root.listFriends != null && root.listFriends.size > 0) {
                    for (i in 0 until root.listFriends.size) {
                        if (root.listFriends[i].displayName.toLowerCase(Locale.getDefault()).contains(text)) {
                            var m: ListFriend
                            m = root.listFriends[i]
                            listAccept.add(m)
                        }
                    }
                }
            }

        }
if(fragType.equals("all")) {
    adap = FriendsAdapter(activity!!, listFriends, service, retroFit, gson)
    v.recycler_friends.adapter = adap
}
        else if(fragType.equals("request")){
    v.recycler_friends.adapter = RequestAdapter(activity!!,listRequest, service, retroFit, gson)

}
        else{
    v.recycler_friends.adapter = AdapterFriends(activity!!,listAccept, service, retroFit, gson)

}

    }

}