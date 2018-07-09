package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.StoreServicesAdapter
import kotlinx.android.synthetic.main.all_classified_screen.view.*


class StoreServiceFragment : Fragment(){
  lateinit  var listDataHeader: ArrayList<String>
    var listDataChild: HashMap<String, ArrayList<String>>? = null
    lateinit var v : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
v = inflater.inflate(R.layout.all_classified_screen,container,false)


        prepareListData()

        v.recycler_stores.layoutManager = LinearLayoutManager(activity!!)
        v.recycler_stores.adapter = StoreServicesAdapter(activity!!,listDataHeader,listDataChild)
        return  v
    }

    private fun prepareListData() {
        listDataHeader = ArrayList<String>()
        listDataChild = HashMap<String,ArrayList<String>>()

        // Adding child data
        listDataHeader.add("Basic Plan (Free)")
        listDataHeader.add("Basic + Memorial($69/year)")

        // Adding child data
        val list1 = ArrayList<String>()
        list1.add("Personal timeline or blog")
        list1.add("Open forums")
        list1.add("Classified board")
        list1.add("Experts circle")
        list1.add("Closed forums")
        list1.add("Newsletter")

        val list2 = ArrayList<String>()
        list2.add("Memorial")
        list2.add("Upload their own photos")
        list2.add("Leave written memories")
        list2.add("Tribuutes and condolences")
        list2.add("Light a candle")
        list2.add("Donate to your charity")
        list2.add("Personal timeline or blog")
        list2.add("Open forums")
        list2.add("Classified board")
        list2.add("Expert circle")
        list2.add("Closed forums")
        list2.add("Newsletter")


        listDataChild!!.put(listDataHeader.get(0), list1) // Header, Child data
        listDataChild!!.put(listDataHeader.get(1), list2)
    }
}