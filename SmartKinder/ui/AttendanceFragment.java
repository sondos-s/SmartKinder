package androidArmy.SmartKinder.ui;


import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.Attendance;
import androidArmy.SmartKinder.backend.MyInfoManager;


public class AttendanceFragment extends Fragment {

    private ImageButton backButton;
    private ImageButton reportButton;
    private LinearLayout checkboxesLayout; // Reference to the LinearLayout that will hold the checkboxes

    private MyInfoManager infoManager;

    private List<String> kidsNames;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;

    private Date attendanceDate = new Date();

    private Button submitButton;
    private Context context = null;

    private TextView currentDateTextView;
    public AttendanceFragment() {
        // Required empty public constructor
    }

    public void setDate(Date date) {
        attendanceDate = date;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        checkboxesLayout = view.findViewById(R.id.attendance_checkboxes_layout);
        currentDateTextView = view.findViewById(R.id.attendance_current_date_textview);

        // Retrieve the MyInfoManager instance from MainActivity
        infoManager = MyInfoManager.getInstance(requireContext());

        // Fetch kids' names using MyInfoManager
        kidsNames = infoManager.getAllKidsNames();

        // Set the attendance date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(attendanceDate);
        currentDateTextView.setText(currentDate);
        currentDateTextView.setText(currentDate);

        List<Attendance> attendanceRecords =infoManager.getAllAttendance(attendanceDate);



        // Create and add checkboxes for each kid name
        for (String kidName : kidsNames) {
            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setText(kidName);
            checkboxesLayout.addView(checkBox);

            // Find the attendance record for the current kid
            Attendance attendanceRecord = infoManager.findAttendance(attendanceRecords, kidName);
            Log.d("m",""+attendanceRecord);
            if (attendanceRecord != null && attendanceRecord.isAttendance()) {
                checkBox.setChecked(true);
            }
        }

        // Find the buttons by its ID
        backButton = view.findViewById(R.id.attendance_back_button);
        reportButton = view.findViewById(R.id.attendance_report_btn);
        submitButton = view.findViewById(R.id.attendance_submit);

        // Set an onClickListener to handle button click events
        backButton.setOnClickListener(v -> handleBackButton());
        reportButton.setOnClickListener(v -> handleReportButton());
        submitButton.setOnClickListener(v -> handleSubmitButton());

        return view;
    }


    // Handle the report button
    private void handleReportButton() {
        FragmentManager fm = getParentFragmentManager();
        AttendanceReportFragment report = new AttendanceReportFragment();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.nav_host_fragment_activity_main,report);
        t.commit();
    }

    // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        HomeFragment home = new HomeFragment();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.nav_host_fragment_activity_main, home);
        t.addToBackStack(null);
        t.commit();
    }


    private List<String> fetchKidsNames() {
        // Replace with your implementation to fetch kids' names from your data source (SQLite, API, etc.)
        List<String> kidsNames = new ArrayList<>();
        for (String kidName : kidsNames) {
            kidsNames.add(kidName);
        }
        return kidsNames;
    }

    // Handle the submit button
    private void handleSubmitButton() {
        // Build the alert dialog
        buildAlertDialog();
        dialog.show();
        // Set click listener for "Yes" button
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the changes
                boolean allRecordsInserted = true;

                // Get the attendance status for each kid
                for (int i = 0; i < checkboxesLayout.getChildCount(); i++) {
                    View view = checkboxesLayout.getChildAt(i);
                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) view;
                        boolean isAttend = checkBox.isChecked();

                        // Get the kid's name associated with the checkbox (assuming the name is set as the checkbox text)
                        String kidName = checkBox.getText().toString();

                        // Insert the attendance record using the MyInfoManager
                        boolean success = infoManager.insertAttendance(attendanceDate,kidName, isAttend);

                        if (!success) {
                            // An error occurred while inserting the record
                            // Set the flag to indicate that at least one record failed
                            allRecordsInserted = false;
                            break; // Exit the loop if a record failed
                        }
                    }
                }
                if (allRecordsInserted) {
                    // All records were successfully inserted
                    // Show a success message
                    String successMessage = "Attendance recorded successfully";
                    Toast.makeText(getContext(), successMessage, Toast.LENGTH_SHORT).show();
                } else {
                    // At least one record failed to insert
                    // Show an error message indicating that the attendance is already taken
                    String errorMessage = "Attendance is already taken";
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        // Set click listener for "No" button
        noButton.setOnClickListener(v -> {dialog.dismiss();});

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

    private void initCheckBoxes(Date date){

    }



}
