package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.Allergies;
import androidArmy.SmartKinder.backend.MyInfoManager;
import androidArmy.SmartKinder.backend.Teacher;
import androidArmy.SmartKinder.backend.TrackHour;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateHoursActivity extends AppCompatActivity {

    private EditText startText;
    private EditText endText;
    private CalendarView calendarView;
    private TimePicker startPicker;
    private TimePicker endPicker;

    private String selectedDate;
    private String selectedStartTime;
    private String selectedEndTime;
    private Button cancel;
    private Button update;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;

    private TrackHour selectedTrack = null;
    private Teacher selectedTeacher = null;

    private MyInfoManager infoManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hours);

        // Retrieve the MyInfoManager instance from MainActivity
        infoManager = MyInfoManager.getInstance(this);

        // Find the views by the IDs
        startText = findViewById(R.id.track_start_label);
        endText = findViewById(R.id.track_end_label);
        calendarView = findViewById(R.id.track_calendar_view);
        startPicker = findViewById(R.id.track_start_picker);
        endPicker = findViewById(R.id.track_end_picker);
        cancel = findViewById(R.id.track_cancel_btn);
        update = findViewById(R.id.track_update_btn);

        // Retrieve the initial selected date from the CalendarView
        initialDate();
        resetTimeSelection();


        startText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startText, startPicker);
            }
        });

        endText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endText, endPicker);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                resetTimeSelection();
            }
        });


        // Retrieve the object TrackHour from the Intent
        selectedTrack = (TrackHour) getIntent().getSerializableExtra("selectedItem");
        if (selectedTrack != null) {
            // Set the fields
            setCurrentTrackHour();
        }

        // Retrieve the object Teacher from the Intent
        selectedTeacher = (Teacher) getIntent().getSerializableExtra("teacher");
        if (selectedTeacher != null) {
            // Ensuring that the teacher object has been passed
            //Toast.makeText(this, "-Teacher: "+selectedTeacher.getFirstName(), Toast.LENGTH_SHORT).show();
        }

        // Set onClickListener for buttons
        cancel.setOnClickListener(this::handleCancelButton);
        update.setOnClickListener(this::handleUpdateButton);



    }

    // Retrieve the initial selected date from the CalendarView
    private void initialDate() {
        long selectedTimeInMillis = calendarView.getDate();
        Calendar selectedDateCalendar = Calendar.getInstance();
        selectedDateCalendar.setTimeInMillis(selectedTimeInMillis);
        int year = selectedDateCalendar.get(Calendar.YEAR);
        int month = selectedDateCalendar.get(Calendar.MONTH);
        int dayOfMonth = selectedDateCalendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

        // Set the initial selected date in the CalendarView
        calendarView.setDate(selectedTimeInMillis);
    }


    // Handle the update button
    private void handleUpdateButton(View view) {
        // Retrieve the selected values from the EditText fields
        selectedStartTime = startText.getText().toString();
        selectedEndTime = endText.getText().toString();




        if ( selectedDate!=null && !selectedStartTime.equals("") && !selectedEndTime.equals(""))
        {
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
        {   // Required fields
            showRequiredFieldsError();
        }

    }


    // A helper function that saves the changes
    private void yesSaveChanges() {
        // Convert the selected date and times to Date and Time objects
        Date date = parseDate(selectedDate);


        Time startTime = parseTime(selectedStartTime);
        Time endTime = parseTime(selectedEndTime);

        if (date != null && startTime != null && endTime != null)
        {
            // In case we are editing
            if(selectedTrack != null)
            {
                // Update the selected track hour with the new values
                selectedTrack.setDate(date);
                selectedTrack.setStartTime(startTime);
                selectedTrack.setEndTime(endTime);
                // Save the updated track hour to db (selectedTrack)
                boolean success = infoManager.editTrackHour(selectedTrack);
                if(success){
                    Toast.makeText(this, "Track hour updated successfully. ", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Failed to update", Toast.LENGTH_LONG).show();
                }
            }
            // In case we are adding
            else
            {

                boolean success;
                // The id is of the user
                if(selectedTeacher != null){
                    success = infoManager.insertTrackHour(date, selectedTeacher.getTeacherId(), startTime, endTime);
                } else {
                    success = infoManager.insertTrackHour(date, 1, startTime, endTime);
                }
                if(success){
                    Toast.makeText(this, "Track hour added successfully", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Failed to add", Toast.LENGTH_LONG).show();

                }

            }
            // Set the result and finish the activity
            setResult(Activity.RESULT_OK);
            finish();
        }
        else
        {
            Toast.makeText(this, "Invalid date or time format", Toast.LENGTH_LONG).show();
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


    // Helper method to parse the date string and return a Date object
    private Date parseDate(String dateStr) {
        // Implement the parsing logic according to your requirements
        // Here's an example assuming the date format is "yyyy-MM-dd":
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to parse the time string and return a Time object
    private Time parseTime(String timeStr) {
        // Implement the parsing logic according to your requirements
        // Here's an example assuming the time format is "HH:mm":
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        try {
            Date time = timeFormat.parse(timeStr);
            return new Time(time.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    // This function fills the fields with the selected track hour from the previous fragment
    private void setCurrentTrackHour() {
        // Editing existing TrackHour

        Date date = selectedTrack.getDate();
        Time startTime = selectedTrack.getStartTime();
        Time endTime = selectedTrack.getEndTime();

        // Set the selected date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calendarView.setDate(calendar.getTimeInMillis());
        selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

        // Set the selected start time
        calendar.setTime(startTime);
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startMinute = calendar.get(Calendar.MINUTE);
        selectedStartTime = String.format("%02d:%02d", startHour, startMinute);
        startText.setText(selectedStartTime);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            startPicker.setHour(startHour);
            startPicker.setMinute(startMinute);
        } else {
            startPicker.setCurrentHour(startHour);
            startPicker.setCurrentMinute(startMinute);
        }

        // Set the selected end time
        calendar.setTime(endTime);
        int endHour = calendar.get(Calendar.HOUR_OF_DAY);
        int endMinute = calendar.get(Calendar.MINUTE);
        selectedEndTime = String.format("%02d:%02d", endHour, endMinute);
        endText.setText(selectedEndTime);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            endPicker.setHour(endHour);
            endPicker.setMinute(endMinute);
        } else {
            endPicker.setCurrentHour(endHour);
            endPicker.setCurrentMinute(endMinute);
        }
    }


    // Handle the cancel button
    private void handleCancelButton(View view) {
        finish();
    }


    private void showTimePickerDialog(final EditText editText, final TimePicker timePicker) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog and set the initial time
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    // Handle the selected time
                    String selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                    editText.setText(selectedTime);
                    int editTextId = editText.getId();
                    if (editTextId == R.id.track_start_label) {
                        selectedStartTime = selectedTime;
                        Log.d("TimePicker", "Selected start time: " + selectedStartTime);
                    } else if (editTextId == R.id.track_end_label) {
                        selectedEndTime = selectedTime;
                        Log.d("TimePicker", "Selected end time: " + selectedEndTime);
                    }
                }, hour, minute, false);

        // Show the dialog
        timePickerDialog.show();
    }

    private void resetTimeSelection() {
        startText.setText("");
        endText.setText("");
        selectedStartTime = null;
        selectedEndTime = null;
    }




}