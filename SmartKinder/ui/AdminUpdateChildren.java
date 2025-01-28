package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.Allergies;
import androidArmy.SmartKinder.backend.InfoKid;
import androidArmy.SmartKinder.backend.MyInfoManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdminUpdateChildren extends AppCompatActivity {

    private EditText childIdEditText;
    private EditText childNameEditText;
    private EditText motherIdEditText;
    private EditText fatherIdEditText;
    private Button birthdateButton;
    private EditText needsEditText;
    private Button saveButton, cancelButton;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_children);

        // Find the views by the ID
        childIdEditText = findViewById(R.id.edit_admin_child_id);
        childNameEditText = findViewById(R.id.edit_admin_child_name);
        motherIdEditText = findViewById(R.id.edit_admin_child_mother_id);
        fatherIdEditText = findViewById(R.id.edit_admin_child_father_id);
        birthdateButton = findViewById(R.id.edit_admin_child_birthdate);
        needsEditText = findViewById(R.id.edit_admin_child_needs);
        saveButton = findViewById(R.id.admin_update_children_save_btn);
        cancelButton = findViewById(R.id.admin_update_children_cancel_btn);


        calendar = Calendar.getInstance();

        // Set OnClickListener for the birthdate button
        birthdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Set OnClickListener for the buttons
        saveButton.setOnClickListener(v -> handleSaveButton());
        cancelButton.setOnClickListener(v -> handleCancelButton());

        // Retrieve the object from the Intent
        InfoKid selectedKid = (InfoKid) getIntent().getSerializableExtra("selectedItem");
        if (selectedKid != null) {
            // Editing existing kid
            childIdEditText.setText(String.valueOf(selectedKid.getId()));
            childNameEditText.setText(selectedKid.getName());
            motherIdEditText.setText(selectedKid.getMotherId());
            fatherIdEditText.setText(selectedKid.getFatherId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String selectedDate = sdf.format(selectedKid.getBirthdate());
            birthdateButton.setText(selectedDate);
            needsEditText.setText(selectedKid.getNeeds());
        }

    }



    // Show the date dialog
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        updateSelectedDate();
                    }
                }, year, month, day);

        datePickerDialog.show();
    }


    // Handle the chosen date
    private void updateSelectedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String selectedDate = sdf.format(calendar.getTime());
        birthdateButton.setText(selectedDate);
    }


    // Handle the save button
    public void handleSaveButton() {
        String childId = childIdEditText.getText().toString();
        String childName = childNameEditText.getText().toString();
        String motherId = motherIdEditText.getText().toString();
        String fatherId = fatherIdEditText.getText().toString();
        String birthdate = birthdateButton.getText().toString();
        String needs = needsEditText.getText().toString();


        // Validate the required fields
        if (childId.isEmpty()||childName.isEmpty()||motherId.isEmpty()||fatherId.isEmpty()||needs.isEmpty()) {
            showRequiredFieldsError();
        } else {
            // Build the alert dialog
            buildAlertDialog();
            dialog.show();
            // Set click listener for "Yes" button
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Save the changes
                    save(childId,childName,motherId,fatherId,birthdate,needs);
                    dialog.dismiss();
                }
            });

            // Set click listener for "No" button
            noButton.setOnClickListener(v -> {dialog.dismiss();});

        }
    }

    private void save(String childId,String childName,String motherId,String fatherId,String birthdate,String needs)
    {
        // Create an InfoKid object
        androidArmy.SmartKinder.backend.InfoKid infoKid = new androidArmy.SmartKinder.backend.InfoKid();
        infoKid.setId(Integer.parseInt(childId));
        infoKid.setName(childName);
        infoKid.setMotherId(motherId);
        infoKid.setFatherId(fatherId);

        // Parse the birthdate string to Date object using SimpleDateFormat
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date birthdateDate = dateFormat.parse(birthdate);
            infoKid.setBirthdate(birthdateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        infoKid.setNeeds(needs);

        // Save the InfoKid object to the database
        MyInfoManager infoManager = MyInfoManager.getInstance(this);
        Intent intent = getIntent();

        // Check if the intent has extras and the "selectedItem" key
        if (intent != null && intent.hasExtra("selectedItem")) {
            InfoKid kid =  intent.getParcelableExtra("selectedItem");
            kid.setName(childName);
            kid.setMotherId(motherId);
            kid.setFatherId(fatherId);



            //Covert string date to Date

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date birthdateDate = dateFormat.parse(birthdate);
                kid.setBirthdate(birthdateDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            kid.setNeeds(needs);

            boolean success = infoManager.editKid(kid);
            if(success){
                Toast.makeText(this, "Kid updated", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }else{
                Toast.makeText(this, "Kid not updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            infoManager.addInfoKid(infoKid);
            Toast.makeText(this, "InfoKid added successfully", Toast.LENGTH_SHORT).show();

        }


        // Finish the activity or perform any other action
        setResult(Activity.RESULT_OK);
        finish();
    }

    // Building the alert dialog
    private void buildAlertDialog()
    {
        // Create a Dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);

        // Set the inflated view as the dialog view
        builder.setView(dialogView);

        dialog = builder.create();


        // Find the "Yes" and "No" buttons from the dialog view
        yesButton = dialogView.findViewById(R.id.dialog_btn_yes);
        noButton = dialogView.findViewById(R.id.dialog_btn_no);
    }


    // Show an alert dialog indicating that required fields are empty
    private void showRequiredFieldsError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Please fill in all required fields.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Handle the cancel button
    private void handleCancelButton() {
        finish();
    }

}
