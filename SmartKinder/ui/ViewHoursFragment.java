package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.MyInfoManager;
import androidArmy.SmartKinder.backend.Teacher;
import androidArmy.SmartKinder.backend.TrackHour;
import androidArmy.SmartKinder.databinding.FragmentViewHoursBinding;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewHoursFragment extends Fragment {

    private static final int UPDATE_HOURS_REQUEST_CODE = 1;

    private Button updateHoursButton;
    private Button deleteButton;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;
    private ImageButton backButton;
    private ListView listView;
    private FragmentViewHoursBinding binding;
    private int selectedItemPosition;

    private Teacher selectedTeacher;

    private SimpleAdapter adapter;
    private List<Map<String, String>> hoursItems;

    // Create SimpleDateFormat objects with the desired formats
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    private MyInfoManager infoManager;
    private List<TrackHour> tracks;

    private Bundle args;


    //private ArrayList<ViewHoursFragment.HourItem> hoursItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_hours, container, false);

        // Retrieve the MyInfoManager instance from MainActivity
        infoManager = MyInfoManager.getInstance(requireContext());

        // Inflate the fragment's layout using the provided inflater
        binding = FragmentViewHoursBinding.inflate(inflater, container, false);

        // Get them from the person selection
        args = getArguments();

        // Get the trackHours of the teacher
        getTracks();

        // Retrieve data from the db
        setTeacherHours();

        // Create the adapter and set it to the ListView
        String[] from = {"date", "start", "end"};
        int[] to = {R.id.date_text, R.id.start_time_text, R.id.end_time_text};
        adapter = new SimpleAdapter(getContext(), hoursItems, R.layout.hour_item, from, to);

        selectedItemPosition = -1;

        // Find the button by its ID
        updateHoursButton = view.findViewById(R.id.button_update_hours);
        backButton = view.findViewById(R.id.track_back_button);
        deleteButton = view.findViewById(R.id.button_delete_hours);
        listView = view.findViewById(R.id.list_view_hours);

        // Set an onClickListeners to handle button click events
        updateHoursButton.setOnClickListener(v -> handleUpdateHoursButton());
        backButton.setOnClickListener(v -> handleBackButton());
        deleteButton.setOnClickListener(v -> handleDeleteButton());

        // Set OnItemClickListener for the ListView to handle item selection
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            handleListItemClick(position);
        });

        listView.setAdapter(adapter);

        return view;
    }


    /** Retrieve the data from the dataBase using uesrId if the user is
     teacher and selectedTeacher if the user is admin **/
    private void getTracks() {
        if (args != null) { //if the admin access
            selectedTeacher = args.getParcelable("teacher");
            //Get the tracks by the selected teacher id
            tracks = infoManager.getTrackHoursByTeacherId(selectedTeacher.getTeacherId());
        } else { //if the user is teacher
            tracks = infoManager.getTrackHoursByTeacherId(1);
        }
    }

    // Insert the trackHours in the list
    private void setTeacherHours() {

        hoursItems = new ArrayList<>();

        // For each trackHour do the below
        // For each teacher do the below
        for(TrackHour t : tracks) {
            // Track hour date
            Date date = t.getDate();

            // Format the Date object as a string
            String formattedDate = dateFormat.format(date);

            // Start hour time (db)
            Time startTime = t.getStartTime();

            // End hour time (db)
            Time endTime = t.getEndTime();

            // Format the Time object as a string
            String formattedStartTime = timeFormat.format(startTime);
            String formattedEndTime = timeFormat.format(endTime);

            hoursItems.add(createHourItem(formattedDate, formattedStartTime, formattedEndTime));
        }
    }



    // Handle the list item click
    private void handleListItemClick(int position) {
        selectedItemPosition = position;
        deleteButton.setEnabled(true);
        for (int i = 0; i < listView.getChildCount(); i++) {
            View listItem = listView.getChildAt(i);
            listItem.setBackgroundColor(i == position ? Color.BLUE: Color.TRANSPARENT);
        }
    }


    // Handle the delete button click
    private void handleDeleteButton() {
        if (selectedItemPosition == -1) {
            // Show a dialog box indicating that the user needs to select an item first
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Error");
            builder.setMessage("Please select the record you want to delete first.");
            builder.setPositiveButton("OK", null);
            builder.show();
        } if (selectedItemPosition != -1 && selectedItemPosition < hoursItems.size()) {
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
            noButton.setOnClickListener(v -> {
                dialog.dismiss();
                selectedItemPosition = -1;
                // Reset selectedItemPosition
                resetListBackground();
            });

    }
    }


    // Reset the list background
    private void resetListBackground() {
        for (int i = 0; i < listView.getChildCount(); i++) {
            View listItem = listView.getChildAt(i);
            listItem.setBackgroundColor( Color.TRANSPARENT);
        }
    }

    // Handle the yes save button of the alert dialog
    private void yesSaveChanges()
    {
        Map<String, String> selectedHourItem = hoursItems.get(selectedItemPosition);
        String dateString = selectedHourItem.get("date");
        String startTimeString = selectedHourItem.get("start");
        String endTimeString = selectedHourItem.get("end");

        try {
            // Parse the strings to obtain Date and Time objects
            Date date = dateFormat.parse(dateString);
            Time startTime = new Time(timeFormat.parse(startTimeString).getTime());
            Time endTime = new Time(timeFormat.parse(endTimeString).getTime());

            TrackHour selectedTrackHour;
            // Create the TrackHour object
            if(args != null){ //if the admin is deleting
                selectedTrackHour = new TrackHour(selectedTeacher.getTeacherId(), date, startTime, endTime);
            } else { //if the teacher is deleting
                selectedTrackHour = new TrackHour(1, date, startTime, endTime);
            }

            // Delete the selected item from the db
            boolean success = infoManager.deleteTrackHour(selectedTrackHour);

            if (success) {
                // Remove the item from the list
                hoursItems.remove(selectedItemPosition);
                Toast.makeText(requireContext(), "Deleted successfully", Toast.LENGTH_LONG).show();
                resetListBackground(); // Reset list item background
                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            } else {
                Toast.makeText(requireContext(), "Failed to delete", Toast.LENGTH_LONG).show();

            }
           selectedItemPosition = -1;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    // Helper method to create a HashMap for each hour item
    private Map<String, String> createHourItem(String date, String start, String end) {
        Map<String, String> hourItem = new HashMap<>();
        hourItem.put("date", date);
        hourItem.put("start", start);
        hourItem.put("end", end);
        return hourItem;
    }


    // Handle the update hours button
    private void handleUpdateHoursButton() {
        TrackHour trackHour = null;
        if (selectedItemPosition != -1) {
            Map<String, String> selectedHourItem = hoursItems.get(selectedItemPosition);
            String dateString = selectedHourItem.get("date");
            String startTimeString = selectedHourItem.get("start");
            String endTimeString = selectedHourItem.get("end");
            try {
                // Parse the strings to obtain Date and Time objects
                Date date = dateFormat.parse(dateString);
                Time startTime = new Time(timeFormat.parse(startTimeString).getTime());
                Time endTime = new Time(timeFormat.parse(endTimeString).getTime());

                if(args != null){
                    trackHour = new TrackHour(selectedTeacher.getTeacherId(),date,startTime,endTime);
                } else {
                    trackHour = new TrackHour(1,date,startTime,endTime);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //  Open for Edit
            Intent intent = new Intent(getActivity(), UpdateHoursActivity.class);

            // Admin
            if(selectedTeacher != null)
            {
                intent.putExtra("teacher",(Parcelable) selectedTeacher);
            }
            // The user is teacher
            intent.putExtra("selectedItem", trackHour);
            startActivityForResult(intent, UPDATE_HOURS_REQUEST_CODE);
        }
        else {
            //  Open for Add
            Intent intent = new Intent(getActivity(), UpdateHoursActivity.class);
            // Admin
            if(selectedTeacher != null)
            {
                intent.putExtra("teacher",(Parcelable) selectedTeacher);
            }
            // Open the UpdateHoursActivity with startActivityForResult
            startActivityForResult(intent, UPDATE_HOURS_REQUEST_CODE);

        }
        // Reset the background color
        if (selectedItemPosition != -1) {
            View listItem = listView.getChildAt(selectedItemPosition);
            if (listItem != null) {
                listItem.setBackgroundColor(Color.TRANSPARENT);
            }
        }

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

        // Find the tittle and the message from the dialog view
        TextView message = dialogView.findViewById(R.id.dialog_message);
        TextView title = dialogView.findViewById(R.id.dialog_title);

        message.setText("Are you sure you want to delete this item?");
        title.setText("Confirm Delete");

        // Find the "Yes" and "No" buttons from the dialog view
        yesButton = dialogView.findViewById(R.id.dialog_btn_yes);
        noButton = dialogView.findViewById(R.id.dialog_btn_no);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_HOURS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            // Refresh the data from the database
            getTracks();

            // Insert the hours to the list
            setTeacherHours();

            // Create the adapter and set it to the ListView
            String[] from = {"date", "start", "end"};
            int[] to = {R.id.date_text, R.id.start_time_text, R.id.end_time_text};
            adapter = new SimpleAdapter(getContext(), hoursItems, R.layout.hour_item, from, to);
            listView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
            // Invalidate the views in the ListView
            listView.invalidateViews();
        }
    }


    // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        int backStackCount = fm.getBackStackEntryCount();
        // Return to the previous screen (choose teacher)
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
