package com.example.bartv.mail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartv.mail.Database.ItemClickListener;
import com.example.bartv.mail.Database.ItemLongClickListener;
import com.example.bartv.mail.Database.dbAdapter;
import com.example.bartv.mail.Database.dbProperties;

/**
 * Created by bartv on 24-9-2016.
 */

public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    TextView tvName, tvStudentNumber, tvClass;
    ImageView imgView;
    ItemClickListener itemClickListener;
    ItemLongClickListener itemLongClickListener;

    // ---------------------------------------------------------------------------------------------
    // Constructor for the Holder Class
    // ---------------------------------------------------------------------------------------------

    public Holder(View itemView) {
        super(itemView);

        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvStudentNumber = (TextView) itemView.findViewById(R.id.tvStudentNumber);
        tvClass = (TextView) itemView.findViewById(R.id.tvClass);
        imgView = (ImageView) itemView.findViewById(R.id.imageView);

        itemView.setOnClickListener(this);

        itemView.setOnLongClickListener(this);
    }

    // ---------------------------------------------------------------------------------------------
    // Override method that binds my itemClickListener to the ItemClick action
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view, getLayoutPosition());

    }

    @Override
    public boolean onLongClick(View view) {
        this.itemLongClickListener.onItemLongClick(view, getLayoutPosition());
        return true;
    }

    // ---------------------------------------------------------------------------------------------
    // Method that handles the setItemClickListener action
    // ---------------------------------------------------------------------------------------------

    public void setItemClickListener(ItemClickListener ic) {
        this.itemClickListener = ic;
    }

    // ---------------------------------------------------------------------------------------------
    // Method that handles the setItemLongClickListener action
    // ---------------------------------------------------------------------------------------------

    public void setItemLongClickListener(ItemLongClickListener ic) {
        this.itemLongClickListener = ic;
    }


}
