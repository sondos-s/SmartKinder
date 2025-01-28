package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.Allergies;
import androidArmy.SmartKinder.backend.MyInfoManager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AdminUpdateAllergies extends AppCompatActivity {

    private EditText allergyNameEditText;
    private Button saveButton , cancelButton;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;

    private MyInfoManager infoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_allergies);

        //Retrieve infoManager
        infoManager = MyInfoManager.getInstance(this);

        // Find the views by the ID
        allergyNameEditText = findViewById(R.id.edit_admin_allergy_name);
        saveButton = findViewById(R.id.admin_update_allergies_save_btn);
        cancelButton = findViewById(R.id.admin_update_allergies_cancel_btn);

        // Set OnClickListener for the buttons
        cancelButton.setOnClickListener(v -> handleCancelButton());
        saveButton.setOnClickListener(v -> handleSaveButton());


        // Retrieve the object from the Intent
       Allergies selectedAllergy = (Allergies) getIntent().getSerializableExtra("selectedItem");
        if (selectedAllergy != null) {
            // Editing existing allergy
            allergyNameEditText.setText(selectedAllergy.getAllergyName());
        }


    }

    // Handle the save button
    private void handleSaveButton() {
        String allergyName = allergyNameEditText.getText().toString().trim();
        // Validate the required fields
        if (allergyName.isEmpty()) {
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
                    yesSaveChanges(allergyName);
                    dialog.dismiss();
                }
            });

            // Set click listener for "No" button
            noButton.setOnClickListener(v -> {dialog.dismiss();});
        }
    }

    private void yesSaveChanges( String allergyName) {
        Intent intent = getIntent();

        // Check if the intent has extras and the "selectedItem" key
        if (intent != null && intent.hasExtra("selectedItem")) {
            Allergies allergy =  intent.getParcelableExtra("selectedItem");

            boolean success = infoManager.editAllergy(allergy, allergyName);
            if(success){
                Toast.makeText(this, "Allergy updated", Toast.LENGTH_SHORT).show();
                // Set the result and finish the activity
                setResult(Activity.RESULT_OK);
                finish();
            }else{
                Toast.makeText(this, "Allergy not updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            boolean success = infoManager.addAllergy(allergyName);
            if(success){
                Toast.makeText(this, "Allergy Name: " + allergyName + " saved to database", Toast.LENGTH_SHORT).show();
                // Set the result and finish the activity
                setResult(Activity.RESULT_OK);
                finish();
            }
            else Toast.makeText(this, "Allergy not saved", Toast.LENGTH_SHORT).show();

        }
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



    // Handle the cancel button
    private void handleCancelButton() {
        finish();
    }
}
