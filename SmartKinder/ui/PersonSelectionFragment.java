package androidArmy.SmartKinder.ui;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.InfoKid;
import androidArmy.SmartKinder.backend.Teacher;
import androidArmy.SmartKinder.backend.MyInfoManager;


public class PersonSelectionFragment extends Fragment {
    private ListView listView;
    private ImageButton backButton;
    private Button nextButton;
    private TextView title;
    private int selectedItemPosition;
    private InfoKid selectedChild;
    private Teacher selectedTeacher;


    ArrayList<InfoKid> kidList;
    ArrayList<Teacher> teacherList;

    private String prevState;
    AlertDialog.Builder builder;

    private MyInfoManager infoManager;


    public PersonSelectionFragment() {
        // Required empty public constructor
    }

    public static PersonSelectionFragment newInstance( String state) {
        PersonSelectionFragment fragment = new PersonSelectionFragment();

        // Pass the data to the fragment
        Bundle args = new Bundle();
        args.putString("prevState",state);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_selection, container, false);

        // Retrieve the MyInfoManager instance from MainActivity
        infoManager = MyInfoManager.getInstance(requireContext());

        // Find the views by the ID
        title = view.findViewById(R.id.select_person_title);
        backButton = view.findViewById(R.id.select_person_back_button);
        listView = view.findViewById(R.id.select_person_list);
        nextButton = view.findViewById(R.id.select_person_next_button);


        // Get the data passed from the previous fragment
        prevState = getArguments().getString("prevState");

        // Set title for the selection fragment
        setTitle();

        // Set adapters according to the prevState
        if(prevState == "Progress")
        {
            childAdapter();
        }
        if(prevState == "Tracking hours")
        {
            teacherAdapter();
        }


        // Set OnClickListener for the back button
        backButton.setOnClickListener(v -> handleBackButton());
        // Set OnClickListener for the next button
        nextButton.setOnClickListener(v -> handleNextButton());

        selectedItemPosition = -1;
        // Set OnItemClickListener for the ListView to handle item selection
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            handleListItemClick(position);
        });


        return view;
    }


    // Create teacher ArrayAdapter and set it to the ListView
    private void teacherAdapter() {
        // Get the teachers from db
        teacherList = new ArrayList<>(infoManager.getAllTeachers());

        // Create ArrayAdapter and set it to the ListView
        ArrayAdapter<Teacher> teacherAdapter = new ArrayAdapter<Teacher>(requireContext(), android.R.layout.simple_list_item_1, teacherList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                Teacher teacher = getItem(position);

                TextView TeacherNameTextView = convertView.findViewById(android.R.id.text1);
                TeacherNameTextView.setText(teacher != null ? teacher.getFirstName()+teacher.getLastName() : "");

                return convertView;
            }
        };
        listView.setAdapter(teacherAdapter);
    }


    // Create children ArrayAdapter and set it to the ListView
    private void childAdapter() {
        /// Get the children from db
        kidList = new ArrayList<>(infoManager.getAllKids());

        // Create ArrayAdapter and set it to the ListView
        ArrayAdapter<InfoKid> kidAdapter = new ArrayAdapter<InfoKid>(requireContext(), android.R.layout.simple_list_item_1, kidList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                InfoKid kid = getItem(position);

                TextView kidNameTextView = convertView.findViewById(android.R.id.text1);
                kidNameTextView.setText(kid != null ? kid.getName() : "");

                return convertView;
            }
        };
        listView.setAdapter(kidAdapter);
    }


    // Set title for the selection fragment
    private void setTitle()
    {
        if(prevState == "Progress")
        {
            title.setText(R.string.title_child_selection);
        }
        if(prevState == "Tracking hours")
        {
            title.setText(R.string.title_teacher_selection);
        }
    }


    // Handle the list item click
    private void handleListItemClick(int position) {
        selectedItemPosition = position;
        nextButton.setEnabled(true);
        for (int i = 0; i < listView.getChildCount(); i++) {
            View listItem = listView.getChildAt(i);
            listItem.setBackgroundColor(i == position ? Color.BLUE: Color.TRANSPARENT);
        }
    }


    // Handle the next button click
    private void handleNextButton() {
        if (selectedItemPosition == -1) {
            // Show a dialog box indicating that the user needs to select an item first
            builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Error");
            if(prevState == "Tracking hours") {
                builder.setMessage("Please select a teacher from the list to view their working hours.");
            }
            if(prevState == "Progress") {
                builder.setMessage("Please select a child from the list to view their progress.");
            }

            builder.setPositiveButton("OK", null);
            builder.show();
        } else {

            Bundle args = new Bundle();
            Fragment fragment;
            switch (prevState)
            {
                case "Tracking hours":
                    // Open the tracking hours screen
                    selectedTeacher = teacherList.get(selectedItemPosition);
                    fragment = new ViewHoursFragment();
                    args.putParcelable("teacher", selectedTeacher);
                    fragment.setArguments(args);
                    break;
                case "Progress":
                    // Open the progress screen
                    selectedChild = kidList.get(selectedItemPosition);
                    fragment = new ViewAllProgressFragment();
                    args.putParcelable("child", selectedChild);
                    fragment.setArguments(args);
                    break;
                default:
                    return;
            }

            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction t = fm.beginTransaction();
            t.replace(R.id.nav_host_fragment_activity_main,fragment);
            t.addToBackStack(null);
            t.commit();
        }
    }


    // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        HomeFragment home = new HomeFragment();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.nav_host_fragment_activity_main, home);
        t.commit();
    }

}
