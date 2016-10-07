package com.example.bartv.mail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bartv.mail.Database.ItemLongClickListener;
import com.example.bartv.mail.Database.dbAdapter;
import com.example.bartv.mail.Database.dbProperties;
import com.example.bartv.mail.Database.ItemClickListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by bartv on 24-9-2016.
 */

public class MyAdapter extends RecyclerView.Adapter<Holder> {

    Context c;
    ArrayList<dbProperties> dbProperties;
    Activity activity;

    // ---------------------------------------------------------------------------------------------
    // Constructor of the MyAdapter class
    // ---------------------------------------------------------------------------------------------

    public MyAdapter(Context ctx, ArrayList<dbProperties> properties, Activity a) {
        this.c = ctx;
        this.dbProperties = properties;
        this.activity = a;
    }

    // ---------------------------------------------------------------------------------------------
    // Override method that sets the layout of the recyclerView to the Card.xml layout
    // ---------------------------------------------------------------------------------------------

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, null);
        Holder holder = new Holder(view);
        return holder;
    }

    // ---------------------------------------------------------------------------------------------
    // Override method that fills each card in the recyclerView with data from the databases
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        // Set the image using drawable
        String uri = "@drawable/" + dbProperties.get(position).getImage();
        int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
        Drawable res = c.getResources().getDrawable(imageResource);

        // Set all values
        holder.tvName.setText(dbProperties.get(position).getName());
        holder.tvStudentNumber.setText(dbProperties.get(position).getStudentnumber());
        holder.tvClass.setText(dbProperties.get(position).getKlas());
        holder.imgView.setImageDrawable(res);

        // Handle the normal click on a item in the recyclerView
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent= new Intent();
                intent.putExtra("EMAIL", dbProperties.get(position).getEmail());
                activity.setResult(RESULT_OK, intent);
                activity.finish();
            }
        });

        // Handle the long click on a item in the recyclerView
        holder.setItemLongClickListener(new ItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {
                chooseDialog(pos,"Choose");

            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // Override method that returns the total amount of Students in the database
    // ---------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return dbProperties.size();
    }

    public void chooseDialog(final int pos, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(title)
                .setItems(R.array.chooseDialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            Intent i= new Intent(c, EditActivity.class);

                            i.putExtra("ID", dbProperties.get(pos).getId());
                            i.putExtra("NAME", dbProperties.get(pos).getName());
                            i.putExtra("STUDENTNUMBER", dbProperties.get(pos).getStudentnumber());
                            i.putExtra("CLASS", dbProperties.get(pos).getKlas());
                            i.putExtra("EMAIL", dbProperties.get(pos).getEmail());
                            i.putExtra("IMAGE", dbProperties.get(pos).getImage());

                            c.startActivity(i);
                        }
                        else if(which == 1) {
                            Intent i= new Intent(c, StudentMapsActivity.class);

                            c.startActivity(i);
                        }
                    }
                });
        AlertDialog a = builder.create();
        a.show();
    }

}
