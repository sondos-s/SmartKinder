package androidArmy.SmartKinder.ui;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidArmy.SmartKinder.R;

public class ChildAttendanceFragment extends Fragment{
    private ImageButton backButton;
    private ListView listView;

    public ChildAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_attendance, container, false);

        // Find and initialize the views
        listView = view.findViewById(R.id.list_view_child_attendance);
        backButton = view.findViewById(R.id.child_attendance_back_button);



        // Create an ArrayList to hold the attendance data
        ArrayList<Attendance> attendances = new ArrayList<>();
        attendances.add(new Attendance("01/06/2023", true));
        attendances.add(new Attendance("31/05/2023", false));

        // Create the adapter and set it to the ListView
        ArrayAdapter<Attendance> adapter = new ArrayAdapter<Attendance>(getContext(), R.layout.attendance_item,  attendances) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.attendance_item, parent, false);
                }

                Attendance attendance = getItem(position);

                TextView dateTextView = convertView.findViewById(R.id.child_attendance_date_text);
                CheckBox present = convertView.findViewById(R.id.child_attendance_checkbox);

                dateTextView.setText(attendance.getDate());
                present.setChecked(attendance.getPresent());

                return convertView;
            }
        };

        listView.setAdapter(adapter);

        // Set OnClickListener for the back button
        backButton.setOnClickListener(v -> handleBackButton());

        return view;
    }



    // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        HomeFragment home = new HomeFragment();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.nav_host_fragment_activity_main, home);
        t.commit();
    }


    // Inner class to represent a single attendance item
    private static class Attendance {
        private String date;
        private boolean present;
        public Attendance(String date, boolean present) {
            this.date = date;
            this.present = present;
        }

        public String getDate() {
            return date;
        }

        public boolean getPresent() {
            return present;
        }

    }
}
