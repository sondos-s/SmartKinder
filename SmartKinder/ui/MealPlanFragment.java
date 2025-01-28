package androidArmy.SmartKinder.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.Meal;
import androidArmy.SmartKinder.backend.MyInfoManager;
import androidArmy.SmartKinder.databinding.FragmentMealplanBinding;

public class MealPlanFragment extends Fragment {

    private DatePicker datePicker;
    private Spinner breakfastSpinner;
    private Spinner lunchSpinner;
    private Button updateButton;
    private ImageButton backButton;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;

    private FragmentMealplanBinding binding;
    private int breakfastId;
    private int lunchId;
    private String breakfast;
    private  String lunch;
    private Date selectedDate;
    private List<String> breakfastOptions ;
    private List<String> lunchOptions ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan, container, false);

        // Inflate the fragment's layout using the provided inflater
        binding = FragmentMealplanBinding.inflate(inflater, container, false);

        // Get the views by their IDs
        datePicker = view.findViewById(R.id.meal_date_picker);
        breakfastSpinner = view.findViewById(R.id.breakfast_spinner);
        lunchSpinner = view.findViewById(R.id.lunch_spinner);
        updateButton = view.findViewById(R.id.meal_update_btn);
        backButton = view.findViewById(R.id.meal_back_button);
        // Set up the date picker

        // Initialize breakfast and lunch options lists
         breakfastOptions = new ArrayList<>();
         lunchOptions = new ArrayList<>();

        // Populate the breakfast options list with meal names
        for (Meal meal : MyInfoManager.getInstance(getContext()).getBreakfastMeals()) {
            breakfastOptions.add(meal.getMealName());
        }

        // Populate the lunch options list with meal names
        for (Meal meal : MyInfoManager.getInstance(getContext()).getLunchMeals()) {
            lunchOptions.add(meal.getMealName());
        }

        // Create breakfast adapter and set it to the breakfast spinner
        ArrayAdapter<String> breakfastAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, breakfastOptions);
        breakfastAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breakfastSpinner.setAdapter(breakfastAdapter);

        // Create lunch adapter and set it to the lunch spinner
        ArrayAdapter<String> lunchAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, lunchOptions);
        lunchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lunchSpinner.setAdapter(lunchAdapter);


        preFill();

        // Set OnClickListener for the update button
        updateButton.setOnClickListener(v -> handleUpdateButton());

        // Set OnClickListener for the back button
        backButton.setOnClickListener(v -> handleBackButton());

        // Handle errors if breakfast meal list is empty
        if (breakfastOptions.isEmpty()) {
            // For example, display an error message
            Toast.makeText(requireContext(), "No breakfast meals available", Toast.LENGTH_SHORT).show();
        }

        // Handle errors if lunch meal list is empty
        if (lunchOptions.isEmpty()) {
            // For example, display an error message
            Toast.makeText(requireContext(), "No lunch meals available", Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    // If the parent already chose meals, then display the details
    private void preFill() {
        // Template for prefilling the data, replace it with the child's details


        // Pre-fill breakfast spinner with "Eggs"
        int breakfastIndex = getIndexFromSpinner(breakfastSpinner, "Eggs");
        breakfastSpinner.setSelection(breakfastIndex);

        // Pre-fill lunch spinner with "Salad"
        int lunchIndex = getIndexFromSpinner(lunchSpinner, "Salad");
        lunchSpinner.setSelection(lunchIndex);

        // Pre-fill date picker with the current date
        Date currentDate = new Date();
        datePicker.init(currentDate.getYear() + 1900, currentDate.getMonth(), currentDate.getDate(), null);
    }


    // Method to get the index of a specific item in a spinner
    private int getIndexFromSpinner(Spinner spinner, String item) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        return adapter.getPosition(item);
    }


    // Handle the update button
    private void handleUpdateButton() {
        // Get the selected date from the date picker
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        // Create a Calendar instance and set the selected date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        // Convert Calendar to Date
        selectedDate = calendar.getTime();


        if(!lunchOptions.isEmpty() && ! breakfastOptions.isEmpty())
        {
            // Get the selected breakfast from the spinner
            breakfast = breakfastSpinner.getSelectedItem().toString();

            // Get the selected lunch from the spinner
            lunch = lunchSpinner.getSelectedItem().toString();

            // Build the alert dialog
            buildAlertDialog();
            dialog.show();
            // Set click listener for "Yes" button
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Save the changes
                    yesSaveChanges();
                    dialog.dismiss();
                }
            });

            // Set click listener for "No" button
            noButton.setOnClickListener(v -> {dialog.dismiss();});
        }
        else
        {   // If there is no breakfast meal or lunch meal selected
            showRequiredFieldsError();
        }

    }


    // A helper function that saves the changes
    private void yesSaveChanges() {

        MyInfoManager.getInstance(getContext()).addChildMeal("Omar", breakfastId, selectedDate.toString());
        MyInfoManager.getInstance(getContext()).addChildMeal("Omar", lunchId, selectedDate.toString());

        // Display a success message
        Toast.makeText(getContext(), "Meal plan updated.", Toast.LENGTH_LONG).show();
    }


    // Helper method to retrieve the mealId from the meal name
    private int getMealIdFromName(String mealName, List<Meal> mealList) {
        for (Meal meal : mealList) {
            if (meal.getMealName().equals(mealName)) {
                return meal.getId();
            }
        }
        return -1; // Return a default value or handle error case
    }


    // Building the alert dialog
    private void buildAlertDialog()
    {
        // Create a Dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        int backStackCount = fm.getBackStackEntryCount();
        // Return to the previous screen (view meals)
        if (backStackCount > 0)
        {
            fm.popBackStack();
        }
        // Return to the home screen
        else
        {
            HomeFragment home = new HomeFragment();
            FragmentTransaction t = fm.beginTransaction();
            t.replace(R.id.nav_host_fragment_activity_main, home);
            t.commit();
        }
    }
}