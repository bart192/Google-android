package com.example.bartv.mail;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bartv.mail.Database.dbAdapter;
import com.example.bartv.mail.Database.dbProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    private EditText etName, etStudentNumber, etMail, etKlas, etZipcode;
    private Button btnAdd;
    private Spinner imgSpinner;
    private RecyclerView rv;
    private MyAdapter adapter;
    private ArrayList<dbProperties> properties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the RecyclerView and set it's LayoutManager and ItemAnimator
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setNestedScrollingEnabled(false);

        // Checks if list is changed and if it is changed it retrieves all data from the database
        // and putts it in the recyclerView and sorts it on the first name
        adapter = new MyAdapter(this, properties, this);
        adapter.notifyDataSetChanged();
        retrieve();
        sortRecyclerViewOnFirstName();

        // Checks if students is empty and if its empty it adds the hardcodes students
        // to the database
        if(properties.isEmpty()) {
            addAllStudents();
            sortRecyclerViewOnFirstName();
            retrieve();
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog();
            }
        });
    }
    // ---------------------------------------------------------------------------------------------
    // Method that creates the dialog to add students
    // ---------------------------------------------------------------------------------------------

    private void addDialog() {
        final Dialog d = new Dialog(this);

        d.setContentView(R.layout.add_dialog);

        etName = (EditText) d.findViewById(R.id.etName);
        etStudentNumber = (EditText) d.findViewById(R.id.etStudentNumber);
        etMail = (EditText) d.findViewById(R.id.etEmail);
        etKlas = (EditText) d.findViewById(R.id.etKlas);
        etZipcode = (EditText) d.findViewById(R.id.etZipcode);
        imgSpinner = (Spinner) d.findViewById(R.id.imageSpinner);
        btnAdd = (Button) d.findViewById(R.id.btnAdd);

        // fills the image spinner
        fillSpinner();

        // Saves the student on click of the button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().trim().length() > 0|| etStudentNumber.getText().toString().trim().length() > 0 || etMail.getText().toString().trim().length() > 0 || etKlas.getText().toString().trim().length() > 0 || etZipcode.getText().toString().trim().length() > 0){
                    save(etName.getText().toString(), etStudentNumber.getText().toString(), etMail.getText().toString(),imgSpinner.getSelectedItem().toString().toLowerCase().replace(" ", "") ,  etKlas.getText().toString(), etZipcode.getText().toString());
                    sortRecyclerViewOnFirstName();
                    d.dismiss();
                }
                else {
                    Toast.makeText(StudentActivity.this, "Have you left something blank?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        d.show();
    }

    // ---------------------------------------------------------------------------------------------
    // Method to fill the image spinner in the add dialog.
    // ---------------------------------------------------------------------------------------------

    private void fillSpinner() {
        List<String> spinnerArray =  new ArrayList(Arrays.asList(getResources().getStringArray(R.array.image)));
        Collections.sort(spinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imgSpinner.setAdapter(adapter);
    }

    // ---------------------------------------------------------------------------------------------
    // Method to save a new item to the database.
    // ---------------------------------------------------------------------------------------------

    private void save(String name, String studentnummer,String email, String image, String klas, String zipcode) {
        dbAdapter db = new dbAdapter(this);
        db.openDB();
        long result = db.add(name, studentnummer, email, image, klas, zipcode);
        if (result > 0) {
            etName.setText("");
            etStudentNumber.setText("");
            etMail.setText("");
            etKlas.setText("");
            Toast.makeText(StudentActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(StudentActivity.this, "Failed to add...", Toast.LENGTH_SHORT).show();
        }
        db.closeDB();
        retrieve();
    }
    // ---------------------------------------------------------------------------------------------
    // Method to sort the recyclerView on the name(Its sorts on the first letter of the string, if
    // the first letter is the same then it progresses to the second item in the string.)
    // ---------------------------------------------------------------------------------------------

    public void sortRecyclerViewOnFirstName() {
        Collections.sort(properties, new Comparator<dbProperties>() {
            public int compare(dbProperties o1, dbProperties o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }
    // ---------------------------------------------------------------------------------------------
    // Method to get all the rows from the database
    // ---------------------------------------------------------------------------------------------

    private void retrieve() {
        dbAdapter db = new dbAdapter(this);
        db.openDB();
        properties.clear();
        Cursor c = db.getAllStudents();
        //Loop through the data, adding to the arraylist
        while (c.moveToNext()) {
            Log.e("Error", c.getString(1));
            Log.e("Error", c.getString(2));
            Log.e("Error", c.getString(3));
            Log.e("Error", c.getString(4));
            Log.e("Error", c.getString(5));
            Log.e("Error", c.getString(6));
            int id = c.getInt(0);
            String name = c.getString(1);
            String studentnumber = c.getString(2);
            String email = c.getString(3);
            String image = c.getString(4);
            String klas = c.getString(5);
            String zipcode = c.getString(6);
            dbProperties f = new dbProperties(id, name, studentnumber, email, image,  klas, zipcode);
            properties.add(f);
        }
        if (!(properties.size() < 1)) {
            rv.setAdapter(adapter);
        }
    }
    // ---------------------------------------------------------------------------------------------
    // Method To add all students into the Database
    // ---------------------------------------------------------------------------------------------
    public void addAllStudents() {
        dbAdapter db = new dbAdapter(this);
        db.openDB();

        db.add("Bart van Es", "0264650", "bvanes01@student.rocvantwente.nl", "bartvanes", "I4AO1", "7534 ME");
        db.add("Tim Bruntink", "0267599", "tbruntink01@student.rocvantwente.nl", "timbruntink", "I4AO1", "7556 DS");
        db.add("Kelvin Cornelissens", "0267772", "kcornelissens01@student.rocvantwente.nl", "kelvincornelissens", "I4AO1", "7622 KV");
        db.add("Martijn Dekker", "0256907", "mdekker04@student.rocvantwente.nl", "martijndekker", "I4AO1", "7471 ZJ");
        db.add("Dylan Doornbos", "0265788", "ddoornbos01@student.rocvantwente.nl", "dylandoornbos", "I4AO1", "7545 KH");
        db.add("Loek Gosen", "0267853", "lgosen01@student.rocvantwente.nl", "loekgosen", "I4AO1", "7574 ZV");
        db.add("Bas Grave", "0267617", "bgrave01@student.rocvantwente.nl", "basgrave", "I4AO1", "7557 GE");
        db.add("Dylan Hofstra", "0263413", "dhoftsra02@student.rocvantwente.nl", "dylanhofstra", "I4AO1", "7534 JX");
        db.add("Jordy Mengerink", "0265597", "jmengerink03@student.rocvantwente.nl", "jordymengerink", "I4AO1", "7559 KT");
        db.add("James Morsink", "0267422", "jmorsink09@student.rocvantwente.nl", "jamesmorsink", "I4AO1", "7557 JB");
        db.add("Robin Tatlici", "0187199", "rtatlici01@student.rocvantwente.nl", "robintatlici", "I4AO1", "7512 XM");
        db.add("Laurens Tel", "0179028", "ltel01@student.rocvantwente.nl", "laurenstel", "I4AO1", "7577 MD");
        db.add("Carlo Verver", "0269264", "cverver01@student.rocvantwente.nl", "carloverver", "I4AO1", "7205 BH");
        db.add("Firdhan Yahya", "0267433", "fyahya01@student.rocvantwente.nl", "firdhanyahya", "I4AO1", "7556 PP");
        db.add("Jake Zweers", "0257956", "jzweers05@student.rocvantwente.nl", "jakezweers", "I4AO1", "7462 BD");

        db.closeDB();
    }

    // ---------------------------------------------------------------------------------------------
    // Override method that sends back a empty result when pressing the back button
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("EMAIL", "");
        setResult(0, intent);

        super.onBackPressed();
    }

    // ---------------------------------------------------------------------------------------------
    // Override method that refreshes the recyclerView on resume of this activity
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        retrieve();
        sortRecyclerViewOnFirstName();
        super.onResume();
    }
}
