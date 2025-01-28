package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.MyInfoManager;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceReportFragment extends Fragment {

    private Button datePickerButton;
    private Button editButton;
    private ImageButton backButton;
    private ListView listAbsent;
    private ListView listPresent;
    private Calendar calendar;

    private LinearLayout presentLayout;

    private LinearLayout absentLayout;

    TextView dateTitleTextView ;

    // Create an adapter for the list views (replace with your own adapter implementation)
    ArrayAdapter<String> presentAdapter;
    ArrayAdapter<String> absentAdapter;

    private MyInfoManager infoManager;

    private  Date attendanceDate;



    public AttendanceReportFragment() {
        // Required empty public constructor
    }


    public static AttendanceReportFragment newInstance() {
        return new AttendanceReportFragment();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_report, container, false);

        presentLayout = view.findViewById(R.id.attendance_report_list_present);
        absentLayout = view.findViewById(R.id.attendance_report_list_absent);


        // Initialize views
        datePickerButton = view.findViewById(R.id.attendance_report_date_picker);
        //listAbsent = view.findViewById(R.id.attendance_report_list_absent);
        //listPresent = view.findViewById(R.id.attendance_report_list_present);
        backButton = view.findViewById(R.id.attendance_report_back_button);
        editButton = view.findViewById(R.id.attendance_report_edit_btn);

        //presentAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item_child, new ArrayList<>());
        //absentAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item_child, new ArrayList<>());

        //listPresent.setAdapter(presentAdapter);
        //listAbsent.setAdapter(absentAdapter);

        calendar = Calendar.getInstance();
        datePickerButton.setOnClickListener(v -> showDatePickerDialog());

        dateTitleTextView = view.findViewById(R.id.attendance_report_date_title);



        // Retrieve the MyInfoManager instance from MainActivity
        infoManager = MyInfoManager.getInstance(requireContext());

//        // Set up DatePicker listener
//        datePickerButton.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {
//            // TODO: Update the lists based on the selected date
//            updateAttendanceLists(year, monthOfYear, dayOfMonth);
//        });


        // Set an onClickListener to handle button click events
        backButton.setOnClickListener(v -> handleBackButton());
        editButton.setOnClickListener(v -> handleEditButton());


        return view;
    }

    // Handle the edit button
    private void handleEditButton() {
        // Back to the attendance file with the selected day
        FragmentManager fm = getParentFragmentManager();
        AttendanceFragment attendance = new AttendanceFragment();
        attendance.setDate(attendanceDate);
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.nav_host_fragment_activity_main, attendance);
        t.commit();

    }


    // Show the picker dialog calendar
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Handle the selected date
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        dateTitleTextView.setText(getString(R.string.title_progress_date) + ": " + selectedDate);
                        //Convert the selected date to DATE
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date date;
                        try {
                            date = dateFormat.parse(selectedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return; // handle the parse exception as needed
                        }

                        // Query the database to retrieve attendance data for the selected date
                        ArrayList<String> presentChildren = queryPresentChildren(date);
                        ArrayList<String> absentChildren = queryAbsentChildren(date);

                        // Clear the existing views in presentLayout and absentLayout
                        presentLayout.removeAllViews();
                        absentLayout.removeAllViews();

                        // Add the new TextViews for the present and absent children
                        for (String kidName : presentChildren) {
                            TextView textView = new TextView(requireContext());
                            textView.setText(kidName);
                            presentLayout.addView(textView);
                        }

                        for (String kidName : absentChildren) {
                            TextView textView = new TextView(requireContext());
                            textView.setText(kidName);
                            absentLayout.addView(textView);
                        }

                        attendanceDate = date;
                    }
                }, year, month, day);

        datePickerDialog.show();
    }



    // Show the selected date
    private void updateSelectedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String selectedDate = sdf.format(calendar.getTime());
        updateAttendanceLists();
        datePickerButton.setText(selectedDate);
    }

    private void updateAttendanceLists() {
        // TODO: Retrieve the attendance data for the selected date

        // Dummy data for demonstration
        List<String> absentChildren = new ArrayList<>();
        absentChildren.add("Child 1");
        absentChildren.add("Child 2");
        absentChildren.add("Child 3");

        List<String> presentChildren = new ArrayList<>();
        presentChildren.add("Child 4");
        presentChildren.add("Child 5");
        presentChildren.add("Child 6");

        // Update the lists with the retrieved data
        ArrayAdapter<String> absentAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, absentChildren);
        listAbsent.setAdapter(absentAdapter);

        ArrayAdapter<String> presentAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, presentChildren);
        listPresent.setAdapter(presentAdapter);
    }


    // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        AttendanceFragment home = new AttendanceFragment();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.nav_host_fragment_activity_main, home);
        t.commit();
    }

    private ArrayList<String> queryPresentChildren(Date date) {
        ArrayList<String> presentChildren = infoManager.getPresentKidsNames(date);
        Log.d("TAG", "Present Children: " + presentChildren);
        return presentChildren;
    }

    private ArrayList<String> queryAbsentChildren(Date date) {
        ArrayList<String> absentChildren = infoManager.getAbsentKidsNames(date);
        Log.d("TAG", "Absent Children: " + absentChildren);
        return absentChildren;
    }




}