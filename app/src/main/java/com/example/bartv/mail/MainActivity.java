package com.example.bartv.mail;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnSend, btnStudent;
    EditText etEmail, etMessage, etSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the buttons from layout
        btnSend = (Button) findViewById(R.id.btnSend);
        btnStudent = (Button) findViewById(R.id.btnStudent);

        // Set the EditTexts from layout
        etEmail = (EditText) findViewById(R.id.etEmail);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etMessage = (EditText) findViewById(R.id.etMessage);

        // Set the EditText not focusable but clickable
        etEmail.setFocusable(false);
        etEmail.setClickable(true);
        final int resultCode = 1;

        // The click listeners for the buttons
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                startActivityForResult(intent, resultCode);
            }
        });
        // On long click opens a dialog to ask if you want to empty this EditText.
        etEmail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                confirmEmptyDialog();
                return true;
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checks if their is a mail address in the EditText
                if(etEmail.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Please select a mail!!", Toast.LENGTH_SHORT).show();
                }
                // Checks if the Subject and Message are empty and opens a dialog with alert
                else if(etSubject.getText().toString().trim().length() == 0 && etMessage.getText().toString().trim().length() == 0) {
                    confirmationDialog("Alert", "The subject and messagebox are empty are you sure you want to proceed", "Yes", "No");
                }
                // Checks if the Subject is empty and opens a dialog with alert
                else if(etSubject.getText().toString().trim().length() == 0) {
                    confirmationDialog("Alert", "The subject is empty are you sure you want to proceed", "Yes", "No");
                }
                // Checks if the Message is empty and opens a dialog with alert
                else if(etMessage.getText().toString().trim().length() == 0) {
                    confirmationDialog("Alert", "The message is empty are you sure you want to proceed", "Yes", "No");
                }
                // If everthing is filled in then it is opening the intent without a alert dialog.
                else {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent.setType("vnd.android.cursor.item/email");
                    Uri data = Uri.parse("mailto:" + etEmail.getText() + "?subject=" + etSubject.getText() + "&body=" + etMessage.getText());
                    emailIntent.setData(data);
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                }
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // Method that opens a Confirmation dialog to empty the mail editText
    // ---------------------------------------------------------------------------------------------

    private void confirmEmptyDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Alert");
        b.setMessage("Are u sure you want to empty this field?");
        b.setCancelable(true);

        b.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int d_ID) {
                        etEmail.setText("");
                        dialog.cancel();
                    }
                });

        b.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int d_ID) {
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        AlertDialog a = b.create();
        a.show();
    }

    // ---------------------------------------------------------------------------------------------
    // Method that creates a confirmation dialog
    // ---------------------------------------------------------------------------------------------

    private void confirmationDialog(String title, String message, String posTxt, String negTxt){
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
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        emailIntent.setType("vnd.android.cursor.item/email");
                        Uri data = Uri.parse("mailto:" + etEmail.getText() + "?subject=" + etSubject.getText() + "&body=" + etMessage.getText());
                        emailIntent.setData(data);
                        startActivity(Intent.createChooser(emailIntent, "Send Email"));
                        dialog.cancel();
                    }
                });

        // Handles the Negative button click
        b.setNegativeButton(
                negTxt,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int d_ID) {
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        // Create the dialog with content and show it
        AlertDialog a = b.create();
        a.show();
    }

    // ---------------------------------------------------------------------------------------------
    // Override method that handles the incoming result of the Intent
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // checks if the requestCode equals 1
        if(requestCode==1) {
            if (resultCode == RESULT_OK)
            {
                // Checks if the selected mail is already added
                if (etEmail.getText().toString().contains(data.getStringExtra("EMAIL"))) {
                    Toast.makeText(this, "Email already added!", Toast.LENGTH_SHORT).show();
                }
                // Check for multiple emails and add email
                else {
                    if (etEmail.getText().toString().trim().equals("")) {
                        etEmail.setText(data.getStringExtra("EMAIL"));
                    } else {
                        etEmail.setText(etEmail.getText().toString() + ", \n" + data.getStringExtra("EMAIL"));
                    }
                }
            }
            else {

            }
        }
    }
}
