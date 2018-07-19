package com.retiredbrainiacs.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.github.captain_miao.recyclerviewutils.listener.OnRecyclerItemClickListener;
import com.retiredbrainiacs.R;
import com.retiredbrainiacs.common.Common;
import com.retiredbrainiacs.model.SimpleChild;
import com.retiredbrainiacs.model.SimpleParentItem;

import java.util.List;


public class SimpleExpandableAdapter extends ExpandableRecyclerAdapter<SimpleExpandableAdapter.SimpleParentViewHolder, SimpleExpandableAdapter.SimpleChildViewHolder> implements OnRecyclerItemClickListener {

    private LayoutInflater mInflater;

    Context c;
    public SimpleExpandableAdapter(Context context, List<ParentListItem> itemList) {
        super(itemList);
        mInflater = LayoutInflater.from(context);
        c = context;
    }

    @Override
    public SimpleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.group_view_holder, viewGroup, false);
        return new SimpleParentViewHolder(view);
    }

    @Override
    public SimpleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.sub_cat_viewholder, viewGroup, false);
        SimpleChildViewHolder viewHolder =  new SimpleChildViewHolder(view);
        viewHolder.setOnRecyclerItemClickListener(this);
        viewHolder.addOnItemViewClickListener();
        //viewHolder.addOnViewClickListener(viewHolder.mCheckBox);
        return viewHolder;
    }

    @Override
    public void onBindParentViewHolder(SimpleParentViewHolder parentViewHolder, final int position, ParentListItem parentListItem) {
        SimpleParentItem simpleParentItem = (SimpleParentItem) parentListItem;
      //  Common.set(c,parentViewHolder.mTvTitle);

        parentViewHolder.mTvTitle.setText(simpleParentItem.getTitle());
        if(simpleParentItem.getChildItemList() != null){
            parentViewHolder.mParentDropDownArrow.setVisibility(View.VISIBLE);
        }
        else{
            parentViewHolder.mParentDropDownArrow.setVisibility(View.GONE);

        }
        parentViewHolder.mParentDropDownArrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBindChildViewHolder(SimpleChildViewHolder simpleChildViewHolder, int position, Object childListItem) {
        final SimpleChild simpleChild = (SimpleChild) childListItem;
       // Common.setRegular(c,simpleChildViewHolder.mTvContent);
        Log.e("title","aman"+simpleChild.getTitle());
        simpleChildViewHolder.mTvContent.setText(simpleChild.getTitle());
      //  simpleChildViewHolder.mCheckBox.setChecked(simpleChild.isSolved());

        if(simpleChild.getTitle().equalsIgnoreCase("Other (Please Specify)")){
            simpleChildViewHolder.edt_add.setVisibility(View.VISIBLE);
            simpleChildViewHolder.img_nxt.setVisibility(View.GONE);
            simpleChildViewHolder.img_chk.setImageDrawable(c.getResources().getDrawable(R.drawable.sel));

        }
        else{
            simpleChildViewHolder.edt_add.setVisibility(View.GONE);
            simpleChildViewHolder.img_chk.setVisibility(View.VISIBLE);

        }
        if(simpleChild.getChked().equalsIgnoreCase("1")) {

            simpleChildViewHolder.img_chk.setImageDrawable(c.getResources().getDrawable(R.drawable.search_whitebg));
        }
        else{
            simpleChildViewHolder.img_chk.setImageDrawable(c.getResources().getDrawable(R.drawable.sel));

        }
            simpleChildViewHolder.img_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simpleChild.getChked().equalsIgnoreCase("1")){
                    simpleChild.setChked("0");
                }
                else{
                    simpleChild.setChked("1");
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v, int position) {
        int id = v.getId();
        Object item = getItem(position);

                    int itemPosition = (int) getItemId(position);
                    int parentPosition = getParentPosition(itemPosition);
                    int parentIndex = getParentWrapperIndex(parentPosition);
                    if (item instanceof SimpleChild) {

                    }
    }

    public class SimpleChildViewHolder extends ChildViewHolder {

        public TextView mTvContent;
        ImageView img_chk,img_nxt;
        EditText edt_add;

        public SimpleChildViewHolder(View itemView) {
            super(itemView);

            mTvContent =  itemView.findViewById(R.id.txt_subcat_name);
            img_chk = itemView.findViewById(R.id.img_chk);
            img_nxt = itemView.findViewById(R.id.next);
            edt_add = itemView.findViewById(R.id.edt_add);
        }
    }

    public class SimpleParentViewHolder extends ParentViewHolder {
        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180f;
        private final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

        public TextView mTvTitle;
        public ImageButton mParentDropDownArrow;
        public ImageButton mParentDropDownArrow1;


        public SimpleParentViewHolder(View itemView) {
            super(itemView);

            mTvTitle =  itemView.findViewById(R.id.txt_cat_name);
            mParentDropDownArrow = itemView.findViewById(R.id.collapseButton);
            mParentDropDownArrow1 = itemView.findViewById(R.id.parent_list_item_expand_arrow1);

            mParentDropDownArrow.setClickable(false);
        }

        @SuppressLint("NewApi")
        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (!HONEYCOMB_AND_ABOVE) {
                return;
            }

            if (expanded) {
                mParentDropDownArrow.setRotation(ROTATED_POSITION);
            } else {
                mParentDropDownArrow.setRotation(INITIAL_POSITION);
            }
        }
    }
}
