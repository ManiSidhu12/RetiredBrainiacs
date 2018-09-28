package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.FeedsAdapter
import com.retiredbrainiacs.apis.UploadImage
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.Imageutils
import com.retiredbrainiacs.model.feeds.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.fullimage_sccreen.*
import uk.co.senab.photoview.PhotoViewAttacher
import java.io.File
import java.io.FileInputStream

class FullImage : AppCompatActivity(),Imageutils.ImageAttachmentListener{
lateinit var post : Post
    lateinit var v1 : View
    lateinit var attacher : PhotoViewAttacher
    lateinit var imageUtils : Imageutils
    var filetype : String = ""
    var filename : String = ""
    var file_path : String = ""
    lateinit var f : File

    lateinit var pd : ProgressDialog
    lateinit var adapt : FeedsAdapter
   companion object {
  lateinit var  adapter : FeedsAdapter
    fun setData(adap : FeedsAdapter){
        adapter = adap
    }
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fullimage_sccreen)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v1 = supportActionBar!!.customView
        v1.titletxt.text = "Image"
        imageUtils = Imageutils(this@FullImage)
adapt = FullImage.adapter
        if(intent != null && intent.extras != null){
            if( intent.extras.getParcelable<Post>("post") != null){
                post =  intent.extras.getParcelable<Post>("post")
      //Log.e("post",post.image)
                if( post.image != null && !post.image.isEmpty()){
                    Picasso.with(this@FullImage).load(post.image).into(image_zoom)

                    attacher = PhotoViewAttacher(image_zoom)
                    attacher.update()
                }
            }
        }
        work()
    }
    fun work(){
        btn_edit_img.setOnClickListener {
            imageUtils.imagepicker(1)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
        Log.e("result",""+resultCode+","+resultCode+","+data.toString())
       imageUtils.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        imageUtils.request_permission_result(requestCode, permissions, grantResults)
    }

   override fun image_attachment(from: Int, filename: String, file: Bitmap, uri: Uri) {
       file_path = filename
       //   v.attachlay.visibility = View.VISIBLE
       image_zoom.setImageBitmap(file)
       attacher = PhotoViewAttacher(image_zoom)
       attacher.update()


       f = File(file_path)

        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "RetiredBrainiacs" + File.separator
       imageUtils.createImage(file, filename, path, false)

       if(f != null) {
           uploadImage(f!!.absolutePath)
       }

    }
    private fun uploadImage(absolutePath: String) {
        if (absolutePath.endsWith(".jpg")) {
            filetype = "jpg"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        } else if (absolutePath.endsWith(".png")) {
            filetype = "png"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        } else if (absolutePath.endsWith(".jpeg")) {
            filetype = "jpeg"
            filename = "Image" + System.currentTimeMillis() + "." + filetype

        }
        pd = ProgressDialog.show(this@FullImage, "", "Uploading")
        Thread(null, uploadimage, "").start()
        //signUp();

    }


    // ****** Implementing thread to upload image****
    private val uploadimage = Runnable {
        var res = ""
        try {
            val fis = FileInputStream(f)
                val edit = UploadImage(this@FullImage,post.usersWallPostId,  filetype, filename)
                res = edit.doStart(fis)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val msg = Message()
        msg.obj = res
        imageHandler.sendMessage(msg)
    }

    //********** Implementing handler for upload image thread*****
    var imageHandler: Handler = object : Handler()
    {
        override fun handleMessage(msg: Message) {
            var res = ""
            try {
                res = msg.obj.toString()
                val status = res.split(",")[0]
Log.e("res",res.toString())
                if (status.equals("true")) {
                    // Toast.makeText(this@ContactInfo, "Successful", Toast.LENGTH_SHORT).show()
                    Common.showToast(this@FullImage,res.split(",")[1])
                    post.image = res.split(",")[2]
                    Log.e("post img",post.image)
                   /* if(adapt != null) {
                        Log.e("in","in")
                        adapt.notifyDataSetChanged()
                    }*/
                this@FullImage.finish()


                } else {
                    Common.showToast(this@FullImage,res.split(",")[1])
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            pd.dismiss()


        }
    }
}