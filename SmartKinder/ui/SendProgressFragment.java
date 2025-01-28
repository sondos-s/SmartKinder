package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.InfoKid;
import androidArmy.SmartKinder.backend.MyInfoManager;
import androidArmy.SmartKinder.backend.Progress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SendProgressFragment extends Fragment {

    private EditText aboutChildEditText;
    private TextView childName;
    private Button sendButton, datePickerButton;
    private Calendar calendar;
    private ImageButton backButton;
    private InfoKid selectedKid;

    private MyInfoManager infoManager;

    private String selectedDate;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_progress, container, false);

        // Retrieve the MyInfoManager instance from MainActivity
        infoManager = MyInfoManager.getInstance(requireContext());

        // Find the views by the ID
        aboutChildEditText = view.findViewById(R.id.send_progress_record);
        sendButton = view.findViewById(R.id.send_progress_btn);
        datePickerButton = view.findViewById(R.id.send_progress_date_picker);
        backButton = view.findViewById(R.id.send_progress_back_button);
        childName = view.findViewById(R.id.send_progress_child_name);

        // Retrieve the Child from the arguments
        Bundle args = getArguments();
        if (args != null) {
            selectedKid = args.getParcelable("child");
        }

        // Set the title to the child name
       childName.setText(selectedKid.getName());


        calendar = Calendar.getInstance();


        // Set OnClickListener for the buttons
        sendButton.setOnClickListener(v -> handleSendButton());
        datePickerButton.setOnClickListener(v -> showDatePickerDialog());
        backButton.setOnClickListener(v -> handleBackButton());


        return view;
    }

    // Handle the send button
    private void handleSendButton() {

        String aboutChild = aboutChildEditText.getText().toString();
        if(selectedDate != null && aboutChild != null)
        {
            // Build the alert dialog
            buildAlertDialog();
            dialog.show();
            // Set click listener for "Yes" button
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Save the changes
                    // Handle sending the information about the child
                    boolean success = infoManager.addProgress(selectedKid.getName(),1, aboutChild, selectedDate);
                    if(success){
                        Toast.makeText(requireContext(), "Child progress sent successfully" , Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(requireContext(), "Failed to send progress" , Toast.LENGTH_LONG).show();
                    }
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


    // Show the picker dialog calendar
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        updateSelectedDate();
                    }
                }, year, month, day);

        datePickerDialog.show();
    }


    // Show the selected date
    private void updateSelectedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = sdf.format(calendar.getTime());
        datePickerButton.setText(selectedDate);
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
        fm.popBackStack();
    }

}
