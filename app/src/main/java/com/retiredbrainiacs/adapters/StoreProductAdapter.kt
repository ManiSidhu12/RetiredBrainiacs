package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.store.ListProduct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.store_product_adapter.view.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


class StoreProductAdapter(var ctx: Context, var listProducts: MutableList<ListProduct>): RecyclerView.Adapter<StoreProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.store_product_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listProducts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(listProducts[position].image != null && !listProducts[position].image.equals("")){
            Picasso.with(ctx).load(listProducts[position].image).into(holder.imgProduct)
        }

        holder.productName.text = listProducts[position].name
        holder.txtDesc.text = listProducts[position].description
        holder.discountPrice.text = "$"+listProducts[position].price

        holder.layMain.setOnClickListener {
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct = itemView.img_store_product
        val productName = itemView.txt_product_name
        val txtPrice = itemView.old_price
        val discountPrice = itemView.actual_price
        val txtDesc = itemView.txt_product_desc
        val layMain = itemView.lay_products_store
    }
}