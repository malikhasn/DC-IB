package com.appinsnap.ewatchIBfinal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterReport extends ArrayAdapter<AlectionModel> implements View.OnClickListener{

    private ArrayList<AlectionModel> dataSet;
    Context mContext;
    DbHelper mydb;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName,date;
        TextView txtType;
        TextView txtVersion;
        ImageView info;
    }

    public CustomAdapterReport(ArrayList<AlectionModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
        mydb=new DbHelper(context);

    }

    @Override
    public void onClick(View v) {

        final int position=(Integer) v.getTag();
        Object object= getItem(position);
        AlectionModel dataModel=(AlectionModel)object;

        switch (v.getId())
        {
            case R.id.item_info:

               deleteDraft(position);
               break;
        }
    }

    private void deleteDraft(final int position) {

        final CharSequence[] items = {"Delete",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext, R.style.MyDialogTheme);
        builder.setTitle("Are you want to Delete Report ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Delete")) {
                    mydb.deleteElection(dataSet.get(position).getId());
                    dataSet.remove(position);
                    notifyDataSetChanged();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AlectionModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);
            /*if (position%2!=0) {
                convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.white));
            } else {
                convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.bgcolor));
            }*/

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getCategory());
        /*if (dataModel.isread == false)
        {       viewHolder.txtName.setTypeface(null, Typeface.BOLD);
    }*/

        viewHolder.txtType.setText(dataModel.getCreationdatetime());
        if(dataModel.getStatus().equals("draft")){
            viewHolder.info.setVisibility(View.VISIBLE);
        }else {
            viewHolder.info.setVisibility(View.INVISIBLE);
        }

//        viewHolder.txtVersion.setText(dataModel.gettype());
//        viewHolder.date.setText(dataModel.getDate());
//        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
                deleteDraft(lastPosition);


            }
        });
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
