package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.Allergies;
import androidArmy.SmartKinder.backend.MyInfoManager;
import androidArmy.SmartKinder.backend.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidArmy.SmartKinder.backend.MyInfoManager;


public class AdminUpdateTeachers extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, idEditText;

    private Button saveButton, cancelButton;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;
    private MyInfoManager infoManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_teachers);

        //Retrieve infomanager
        infoManager = MyInfoManager.getInstance(this);


        // Find the views by the ID
        idEditText = findViewById(R.id.edit_admin_teacher_id);
        firstNameEditText = findViewById(R.id.edit_admin_teacher_firstname);
        lastNameEditText = findViewById(R.id.edit_admin_teacher_lastname);
        cancelButton = findViewById(R.id.admin_update_teacher_cancel_btn);
        saveButton = findViewById(R.id.admin_update_teacher_save_btn);

        // Set OnClickListener for the buttons
        saveButton.setOnClickListener(v -> handleSaveButton());
        cancelButton.setOnClickListener(v -> handleCancelButton());

        // Retrieve the object from the Intent
        Teacher selectedTeacher = (Teacher) getIntent().getSerializableExtra("selectedItem");
        if (selectedTeacher != null) {
            // Editing existing teacher
            idEditText.setText(String.valueOf(selectedTeacher.getTeacherId()));
            firstNameEditText.setText(selectedTeacher.getFirstName());
            lastNameEditText.setText(selectedTeacher.getLastName());
        }
    }


    // Handle the save button
    public void handleSaveButton() {
        String teacherId = idEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        // Validate the required fields
        if (teacherId.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
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
                    save( teacherId, firstName, lastName );
                    dialog.dismiss();
                }
            });

            // Set click listener for "No" button
            noButton.setOnClickListener(v -> {dialog.dismiss();});

        }

    }


    private void save(String teacherId,String firstName,String lastName )
    {
        Teacher teacher = new Teacher(Integer.parseInt(teacherId), firstName, lastName);
        teacher.setTeacherId(Integer.parseInt(teacherId));
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);

        Intent intent = getIntent();

        // Check if the intent has extras and the "selectedItem" key
        if (intent != null && intent.hasExtra("selectedItem")) {
            Teacher selectedTeacher =   intent.getParcelableExtra("selectedItem");
            selectedTeacher.setFirstName(firstName);
            selectedTeacher.setLastName(lastName);
            boolean success = infoManager.editTeacher(selectedTeacher);
            if(success){
                Toast.makeText(this, "Teacher updated", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }else{
                Toast.makeText(this, "Teacher not updated", Toast.LENGTH_SHORT).show();
            }
        }else {
            boolean success = infoManager.addTeacher(teacher);
            if(success)
            {
                Toast.makeText(this, "Teacher saved: " + teacherId + " " + firstName + " " + lastName, Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }

            else   Toast.makeText(this, "Teacher not saved", Toast.LENGTH_SHORT).show();

        }

    }
    // Handle the cancel button
    private void handleCancelButton() {
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
}