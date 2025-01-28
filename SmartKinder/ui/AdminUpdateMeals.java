package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.Meal;
import androidArmy.SmartKinder.backend.MyInfoManager;

import androidArmy.SmartKinder.backend.Teacher;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminUpdateMeals extends AppCompatActivity {

    private Spinner mealTypeSpinner;
    private Button saveButton , cancelButton ;
    EditText mealNameEdit;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;

    private MyInfoManager infoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_meals);

        //Retrieve infoManager
        infoManager = MyInfoManager.getInstance(this);


        // Find the views by the ID
        mealTypeSpinner = findViewById(R.id.admin_meal_type_spinner);
        mealNameEdit = findViewById(R.id.edit_admin_meal_name);
        saveButton = findViewById(R.id.admin_update_meal_save_btn);
        cancelButton = findViewById(R.id.admin_update_meal_cancel_btn);

        // Create an ArrayList to hold the meal type options
        ArrayList<String> mealTypeOptions = new ArrayList<>();
        mealTypeOptions.add("Breakfast");
        mealTypeOptions.add("Lunch");

        // Create an ArrayAdapter to populate the spinner with the options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mealTypeOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(adapter);

        // Set a listener to handle spinner item selection
        mealTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMealType = mealTypeOptions.get(position);
                Toast.makeText(AdminUpdateMeals.this, "Selected meal type: " + selectedMealType, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set a click listener for the save button
        saveButton.setOnClickListener(v -> saveMeal());
        cancelButton.setOnClickListener(v -> handleCancelButton());

        // Retrieve the object from the Intent
        Meal selectedMeal = (Meal) getIntent().getSerializableExtra("selectedItem");
        if (selectedMeal != null) {
            // Editing existing Meal
            int position = adapter.getPosition(selectedMeal.getType());
            mealTypeSpinner.setSelection(position);
            mealNameEdit.setText(selectedMeal.getMealName());
        }
    }


    // Handle the save button
    private void saveMeal() {

        String mealName = mealNameEdit.getText().toString();

        // Retrieve the selected meal type from the spinner
        String selectedMealType = mealTypeSpinner.getSelectedItem().toString();

        // Perform the save operation
        // Validate the required fields
        if (mealName.isEmpty()) {
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
                    save(mealName,selectedMealType);
                    dialog.dismiss();
                }
            });

            // Set click listener for "No" button
            noButton.setOnClickListener(v -> {dialog.dismiss();});


        }
    }


    private void save(String mealName,String selectedMealType )
    {
        //if its an edit request
        Intent intent = getIntent();
        // Check if the intent has extras and the "selectedItem" key
        if (intent != null && intent.hasExtra("selectedItem")) {
            // Retrieve the selected meal object
            Meal selectedMeal = intent.getParcelableExtra("selectedItem");

            boolean success = infoManager.editMeal(selectedMeal, mealName, selectedMealType);
            if(success){
                Toast.makeText(this, "Meal updated:  Name = " + mealName + ", Type = " + selectedMealType, Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }else{
                Toast.makeText(this, "Meal not updated", Toast.LENGTH_SHORT).show();
            }

        } else { //if it is an add request
            boolean success = infoManager.addMeal(mealName, selectedMealType);
            if (success) {
                Toast.makeText(this, "Meal saved:  Name = " + mealName + ", Type = " + selectedMealType, Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }
            else Toast.makeText(this, "Meal not saved", Toast.LENGTH_SHORT).show();

        }
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
