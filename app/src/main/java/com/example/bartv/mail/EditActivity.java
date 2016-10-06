package com.example.bartv.mail;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private Button btnUpdate, btnDelete;
    private EditText etName, etStudentNumber, etKlas, etMail;
    private Spinner spImage;
    private List<String> spinnerArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        etName = (EditText)findViewById(R.id.etName);
        etStudentNumber = (EditText)findViewById(R.id.etStudentNumber);
        etKlas = (EditText)findViewById(R.id.etKlas);
        etMail = (EditText)findViewById(R.id.etEmail);
        spImage = (Spinner)findViewById(R.id.imageSpinner);

        // Gets the extra's send by the intent
        Intent i = getIntent();

        // Get all values in variable
        final int id = i.getExtras().getInt("ID");
        final String name = i.getExtras().getString("NAME");
        final String studentnumber = i.getExtras().getString("STUDENTNUMBER");
        final String klas = i.getExtras().getString("CLASS");
        final String email = i.getExtras().getString("EMAIL");
        final String image = i.getExtras().getString("IMAGE");

        spinnerArray  =  new ArrayList(Arrays.asList(getResources().getStringArray(R.array.image)));

        fillSpinner();

        etName.setText(name);
        // Sets the typing cursor to the end of the EditText
        etName.setSelection(etName.getText().length());
        etStudentNumber.setText(studentnumber);
        etKlas.setText(klas);
        etMail.setText(email);
        spImage.setSelection(spinnerArray.indexOf(name));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().toString().trim().length() > 0|| etStudentNumber.getText().toString().trim().length() > 0 || etMail.getText().toString().trim().length() > 0 || etKlas.getText().toString().trim().length() > 0){
                    update(id, etName.getText().toString(), etStudentNumber.getText().toString(), etMail.getText().toString(),spImage.getSelectedItem().toString().toLowerCase().replace(" ", "") ,  etKlas.getText().toString());
                    retrieve();
                    returnToStudentActivity();
                }
                else {
                    Toast.makeText(EditActivity.this, "Have you left something blank?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationDialog("Delete Student", "Are you sure you want to delete this student?", "Yes", "No", id);
                retrieve();
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // Method that creates a confirmation dialog
    // ---------------------------------------------------------------------------------------------

    private void confirmationDialog(String title, String message, String posTxt, String negTxt, final int id){
        // Create the AlertDialog with Builder
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        // Set the title
        b.setTitle(title);
        // Set the message
        b.setMessage(message);
        // Enables a cancel if the user clicks outside of the dialog
        b.setCancelable(true);

        // Handles the Positive button click
        b.setPositiveButton(
                posTxt,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int d_ID) {
                        delete(id);
                        retrieve();
                        dialog.cancel();
                    }
                });

        // Handles the Negative button click
        b.setNegativeButton(
                negTxt,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int d_ID) {
                        Toast.makeText(EditActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        // Create the dialog with content and show it
        AlertDialog a = b.create();
        a.show();
    }

    // ---------------------------------------------------------------------------------------------
    // Method to get all the rows from the database
    // ---------------------------------------------------------------------------------------------

    private void retrieve() {
        ArrayList<dbProperties> properties = new ArrayList<>();
        dbAdapter db=new dbAdapter(this);

        db.openDB();

        properties.clear();

        Cursor c=db.getAllStudents();

        while (c.moveToNext())
        {
            int id=c.getInt(0);
            String name = c.getString(1);
            String studentnumber = c.getString(2);
            String email = c.getString(3);
            String image = c.getString(4);
            String klas = c.getString(5);

            dbProperties properties1 = new dbProperties(id, name, studentnumber, email, image,  klas);

            properties.add(properties1);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Method to delete a student
    // ---------------------------------------------------------------------------------------------

    private void delete(int id) {
        dbAdapter db = new dbAdapter(EditActivity.this);
        db.openDB();
        long result = db.delete(id);
        if (result > 0) {
            Toast.makeText(EditActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            Toast.makeText(EditActivity.this, "Unable to Delete", Toast.LENGTH_SHORT).show();
        }
        db.closeDB();
    }

    // ---------------------------------------------------------------------------------------------
    // Method to fill the image spinner in the add dialog.
    // ---------------------------------------------------------------------------------------------

    private void fillSpinner() {

        Collections.sort(spinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImage.setAdapter(adapter);
    }

    // ---------------------------------------------------------------------------------------------
    // Method to update a student
    // ---------------------------------------------------------------------------------------------

    private void update(int id, String name, String studentnumber, String klas, String mail, String image) {
        dbAdapter db = new dbAdapter(this);
        db.openDB();
        long result = db.update(id, name, studentnumber, klas, mail, image);

        if (result > 0) {
            etName.setText(name);
            etStudentNumber.setText(studentnumber);
            etKlas.setText(klas);
            etMail.setText(mail);
            spImage.setSelection(spinnerArray.indexOf(name));
            Toast.makeText(EditActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            Toast.makeText(EditActivity.this, "Unable to Update", Toast.LENGTH_SHORT).show();
        }
        db.closeDB();
    }

    // ---------------------------------------------------------------------------------------------
    // Method to return to the studentActivity
    // ---------------------------------------------------------------------------------------------

    public void returnToStudentActivity() {
        finish();
    }

    // ---------------------------------------------------------------------------------------------
    // Override method that returns the user to the StudentActivity when they press te back button
    // once
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnToStudentActivity();
    }
}
