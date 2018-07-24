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
        listDataHeader.add("Basic + Archives($99/year)")
        listDataHeader.add("Premium($139/year)")
        listDataHeader.add("Premium For Couples($199/year)")

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
        list2.add("Tributes and condolences")
        list2.add("Light a candle")
        list2.add("Donate to your charity")
        list2.add("Personal timeline or blog")
        list2.add("Open forums")
        list2.add("Classified board")
        list2.add("Expert circle")
        list2.add("Closed forums")
        list2.add("Newsletter")

        val list3 = ArrayList<String>()
        list3.add("Archives")
        list3.add("Hand-written life diary")
        list3.add("Read,search and view")
        list3.add("Work,photos,videos")
        list3.add("Ideas,professional work")
        list3.add("Personal timeline or blog")
        list3.add("Open forums")
        list3.add("Classified board")
        list3.add("Expert circle")
        list3.add("Closed forums")
        list3.add("Newsletter")


        val list4 = ArrayList<String>()
        list4.add("Archives")
        list4.add("Hand-written life diary")
        list4.add("Read,search and view")
        list4.add("Work,photos,videos")
        list4.add("Ideas,professional work")
        list4.add("Memorial")
        list4.add("Upload their own photos")
        list4.add("Leave written memories")
        list4.add("Tributes and condolences")
        list4.add("Light a candle")
        list4.add("Donate to your charity")
        list4.add("Personal timeline or blog")
        list4.add("Open forums")
        list4.add("Classified board")
        list4.add("Expert circle")
        list4.add("Closed forums")
        list4.add("Newsletter")


        listDataChild!!.put(listDataHeader.get(0), list1) // Header, Child data
        listDataChild!!.put(listDataHeader.get(1), list2)
        listDataChild!!.put(listDataHeader.get(2), list3)
        listDataChild!!.put(listDataHeader.get(3), list4)
        listDataChild!!.put(listDataHeader.get(4), list4)
    }
}