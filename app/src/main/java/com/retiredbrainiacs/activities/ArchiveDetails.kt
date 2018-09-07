package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.util.Log
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ArchiveDetailAdapter
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.archive.ArchiveDetailsRoot
import com.retiredbrainiacs.model.archive.MainModel
import com.retiredbrainiacs.model.archive.ModelDetail
import kotlinx.android.synthetic.main.archive_details.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import java.io.StringReader

class ArchiveDetails : AppCompatActivity() {
    lateinit var root: ArchiveDetailsRoot
    var id: String = ""
    lateinit var model: ModelDetail
    lateinit var listModel: ArrayList<ModelDetail>
    lateinit var modelMain: MainModel
    lateinit var v: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.archive_details)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        v = supportActionBar!!.customView
        v.titletxt.text = "Archive Details"
        v.btn_logout.visibility = View.GONE
        v.btn_edit.visibility = View.VISIBLE
        if (intent != null && intent.extras != null && intent.extras.getString("id") != null) {
            id = intent.extras.getString("id")
        }

        recycler_archive_details.layoutManager = LinearLayoutManager(this@ArchiveDetails)

        if (CommonUtils.getConnectivityStatusString(this).equals("true")) {
            getArchiveDetails()
        } else {
            CommonUtils.openInternetDialog(this)
        }

        v.btn_edit.setOnClickListener {
            edt_archive_title_detail.isEnabled = true
            edt_archive_cat_detail.isEnabled = true
            edt_archive_desc_detail.isEnabled = true
            v.btn_edit.text = "Save"
        }
    }

    private fun getArchiveDetails() {
        var url = GlobalConstants.API_URL + "view-archive-data"
        val pd = ProgressDialog.show(this@ArchiveDetails, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ArchiveDetailsRoot>(reader, ArchiveDetailsRoot::class.java)

            if (root.status.equals("true")) {
                modelMain = MainModel()
                listModel = ArrayList()

                if (root.listArch != null && root.listArch.size > 0) {
                    edt_archive_title_detail.text = Editable.Factory.getInstance().newEditable(root.listArch[0].archiveTitle)
                    edt_archive_desc_detail.text = Editable.Factory.getInstance().newEditable(root.listArch[0].description)
                    edt_archive_cat_detail.text = Editable.Factory.getInstance().newEditable(root.listArch[0].categoryName)
                    txt_archive_date_detail.text = root.listArch[0].archiveDate
                    for (i in 0 until root.listArch.size) {
                        if (root.listArch[i].youtube != null && root.listArch[i].youtube.size > 0) {
                            for (j in 0 until root.listArch[i].youtube.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].youtube[j].id
                                model.file = root.listArch[i].youtube[j].file
                                model.file_align = root.listArch[i].youtube[j].fileAlign
                                model.file_note = root.listArch[i].youtube[j].fileNote
                                model.file_type = "youtube"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].archiveimages != null && root.listArch[i].archiveimages.size > 0) {
                            for (j in 0 until root.listArch[i].archiveimages.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].archiveimages[j].id
                                model.file = root.listArch[i].archiveimages[j].file
                                model.file_align = root.listArch[i].archiveimages[j].fileAlign
                                model.file_note = root.listArch[i].archiveimages[j].fileNote
                                model.file_type = "images"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].bmpTif != null && root.listArch[i].bmpTif.size > 0) {
                            for (j in 0 until root.listArch[i].bmpTif.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].bmpTif[j].id
                                model.file = root.listArch[i].bmpTif[j].file
                                model.file_align = root.listArch[i].bmpTif[j].fileAlign
                                model.file_note = root.listArch[i].bmpTif[j].fileNote
                                model.file_type = "bmp"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].xml != null && root.listArch[i].xml.size > 0) {
                            for (j in 0 until root.listArch[i].xml.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].xml[j].id
                                model.file = root.listArch[i].xml[j].file
                                model.file_align = root.listArch[i].xml[j].fileAlign
                                model.file_note = root.listArch[i].xml[j].fileNote
                                model.file_type = "xml"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].pdf != null && root.listArch[i].pdf.size > 0) {
                            for (j in 0 until root.listArch[i].pdf.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].pdf[j].id
                                model.file = root.listArch[i].pdf[j].file
                                model.file_align = root.listArch[i].pdf[j].fileAlign
                                model.file_note = root.listArch[i].pdf[j].fileNote
                                model.file_type = "pdf"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].xls != null && root.listArch[i].xls.size > 0) {
                            for (j in 0 until root.listArch[i].xls.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].xls[j].id
                                model.file = root.listArch[i].xls[j].file
                                model.file_align = root.listArch[i].xls[j].fileAlign
                                model.file_note = root.listArch[i].xls[j].fileNote
                                model.file_type = "xls"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].xlsx != null && root.listArch[i].xlsx.size > 0) {
                            for (j in 0 until root.listArch[i].xlsx.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].xlsx[j].id
                                model.file = root.listArch[i].xlsx[j].file
                                model.file_align = root.listArch[i].xlsx[j].fileAlign
                                model.file_note = root.listArch[i].xlsx[j].fileNote
                                model.file_type = "xlsx"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].doc != null && root.listArch[i].doc.size > 0) {
                            for (j in 0 until root.listArch[i].doc.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].doc[j].id
                                model.file = root.listArch[i].doc[j].file
                                model.file_align = root.listArch[i].doc[j].fileAlign
                                model.file_note = root.listArch[i].doc[j].fileNote
                                model.file_type = "doc"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].docx != null && root.listArch[i].docx.size > 0) {
                            for (j in 0 until root.listArch[i].docx.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].docx[j].id
                                model.file = root.listArch[i].docx[j].file
                                model.file_align = root.listArch[i].docx[j].fileAlign
                                model.file_note = root.listArch[i].docx[j].fileNote
                                model.file_type = "docx"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].mp4MovWmv != null && root.listArch[i].mp4MovWmv.size > 0) {
                            for (j in 0 until root.listArch[i].mp4MovWmv.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].mp4MovWmv[j].id
                                model.file = root.listArch[i].mp4MovWmv[j].file
                                model.file_align = root.listArch[i].mp4MovWmv[j].fileAlign
                                model.file_note = root.listArch[i].mp4MovWmv[j].fileNote
                                model.file_type = "mp4"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }
                        if (root.listArch[i].mp3 != null && root.listArch[i].mp3.size > 0) {
                            for (j in 0 until root.listArch[i].mp3.size) {
                                model = ModelDetail()
                                model.id = root.listArch[i].mp3[j].id
                                model.file = root.listArch[i].mp3[j].file
                                model.file_align = root.listArch[i].mp3[j].fileAlign
                                model.file_note = root.listArch[i].mp3[j].fileNote
                                model.file_type = "mp3"
                                listModel.add(model)
                            }
                            modelMain.model = listModel
                        }

                        if (modelMain != null && modelMain.model != null && modelMain.model.size > 0) {
                            recycler_archive_details.adapter = ArchiveDetailAdapter(this, modelMain.model)
                        }
                    }
                }

            } else {
                Common.showToast(this@ArchiveDetails, root.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@ArchiveDetails).userId
                map["id"] = id
                Log.e("map archive details", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this@ArchiveDetails)
        requestQueue.add(postRequest)

    }

}