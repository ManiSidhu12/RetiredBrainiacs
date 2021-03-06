package com.retiredbrainiacs.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.retiredbrainiacs.R;
import com.retiredbrainiacs.model.login.ChildModel;
import com.retiredbrainiacs.model.login.MainModel;

import java.util.ArrayList;

public class SampleAdapter extends BaseExpandableListAdapter {

    private final Context c;
    private final LayoutInflater mLayoutInflater;
    ArrayList<MainModel> listMain;
  public boolean bool = false;

    public SampleAdapter(Context context, ArrayList<MainModel> listMain) {
        c = context;
        this.listMain = listMain;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return listMain.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listMain.get(groupPosition).getHeading();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.group_view_holder, parent, false);
        }


        final TextView text = (TextView) convertView.findViewById(R.id.txt_cat_name);
        String title = "";
        if (listMain.get(groupPosition).getHeading().contains("_")) {
            title = listMain.get(groupPosition).getHeading().replace("_", " ");
        } else {
            title = listMain.get(groupPosition).getHeading();
        }
        String title1, title2;
        if (title.contains(" ")) {
            title1 = title.split("\\s")[0].substring(0, 1).toUpperCase() + title.split("\\s")[0].substring(1).toLowerCase();
            title2 = title.split("\\s")[1].substring(0, 1).toUpperCase() + title.split("\\s")[1].substring(1).toLowerCase();
            text.setText(title1 + " " + title2);

        } else {
            title1 = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();

            text.setText(title1);
        }


        final ImageView expandedImage = convertView.findViewById(R.id.collapseButton);
        final int resId = isExpanded ? R.drawable.ic_expand_less_black_24dp : R.drawable.ic_expand_more_black_24dp;
        expandedImage.setImageResource(resId);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listMain.get(groupPosition).getListChild().size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listMain.get(groupPosition).getListChild().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.sub_cat_viewholder, parent, false);
        }

        final TextView text = convertView.findViewById(R.id.txt_subcat_name);

        ImageView img_chk = convertView.findViewById(R.id.img_chk);
        ImageView img_nxt = convertView.findViewById(R.id.next);
        final EditText edt_add = convertView.findViewById(R.id.edt_add);
        RelativeLayout lay_end = convertView.findViewById(R.id.lay_end);
        RelativeLayout lay_main = convertView.findViewById(R.id.lay_name);
        final ChildModel model = listMain.get(groupPosition).getListChild().get(childPosition);

        text.setText(model.getTitle());
        Log.e("pos", groupPosition + "," + childPosition);
        int p = groupPosition;
	/*	edt_add.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				model.setOther(s.toString());
			}
		});
*/

        if (model.getTitle().equalsIgnoreCase("Other (Please Specify)")) {
            //	Log.e("group",groupPosition+","+childPosition);
            edt_add.setVisibility(View.VISIBLE);
            img_nxt.setVisibility(View.GONE);
            img_chk.setVisibility(View.GONE);
            lay_end.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            Log.e("bool",""+bool);
            edt_add.setTag(groupPosition);

            if (model.getOther() != null && !model.getOther().isEmpty()) {
                Log.e("model", model.getOther()+groupPosition+childPosition);

                if(edt_add.getTag().equals(groupPosition)) {
                    edt_add.setText(model.getOther());
                }
            }
            else{
                Log.e("modelelse", "am"+model.getOther()+groupPosition+childPosition);
                if(edt_add.getTag().equals(groupPosition)) {
                    edt_add.setText("");
                }
            }
            edt_add.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.e("positions", groupPosition + "," + childPosition + "");
                    Log.e("text2====positions", s + ",,,," + model.getOther() + ",,,," + groupPosition + "njjjj"+edt_add.getTag().toString());

                    if (!s.toString().equalsIgnoreCase(model.getOther()) && edt_add.getTag().equals(groupPosition)) {
                        bool = true;
                        Log.e("iiiiiii", "hhhhh"+groupPosition + "," + childPosition);
                        model.setOther(s.toString());
                     //   edt_add.setText(model.getOther());
                        //notifyDataSetChanged();
                    }
                }
            });
        } else {
            edt_add.setVisibility(View.GONE);
            img_chk.setVisibility(View.VISIBLE);
            img_nxt.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            lay_end.setVisibility(View.GONE);
            if (model.getChkStatus().equalsIgnoreCase("1")) {
                img_chk.setImageDrawable(c.getResources().getDrawable(R.drawable.sel));
            } else {
                img_chk.setImageDrawable(c.getResources().getDrawable(R.drawable.unslct));
            }
        }

        img_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getChkStatus().equalsIgnoreCase("1")) {
                    model.setChkStatus("0");
                    notifyDataSetChanged();
                } else {
                    model.setChkStatus("1");
                    notifyDataSetChanged();
                }

            }
        });
        lay_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getChkStatus().equalsIgnoreCase("1")) {
                    model.setChkStatus("0");
                    notifyDataSetChanged();
                } else {
                    model.setChkStatus("1");
                    notifyDataSetChanged();
                }

            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
