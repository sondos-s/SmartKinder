package androidArmy.SmartKinder.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.InfoKid;
import androidArmy.SmartKinder.backend.MyInfoManager;
import androidArmy.SmartKinder.backend.User;
import androidArmy.SmartKinder.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

    private Button trackHours;
    private Button mealPlan;
    private Button childrenNeeds;
    private Button progress;
    private Button attendance;
    private Button payment;
    private FragmentHomeBinding binding;
    private TitleChangeListener titleChangeListener;

    private MyInfoManager infoManager;

    String type;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the fragment's layout using the provided inflater
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Retrieve the MyInfoManager instance from MainActivity
        infoManager = MyInfoManager.getInstance(requireContext());



        if (infoManager.getUser() != null) {
            String userName = infoManager.getUser().getUserName();
            if (userName != null) {
                if (userName.equals("Admin")) {
                    type = "admin";
                } else if (userName.equals("Parent")) {
                    type = "parent";
                } else {
                    type = "teacher";
                }
            } else {
                // Handle the case when userName is null
            }
        } else {
            // Handle the case when user is null
        }


        titleChangeListener.onTitleChanged("Home");


        // Find the buttons in the inflated layout
        trackHours = binding.getRoot().findViewById(R.id.trackHours_btn);
        mealPlan =binding.getRoot().findViewById(R.id.mealPlan_btn);
        childrenNeeds = binding.getRoot().findViewById(R.id.needs_btn);
        progress = binding.getRoot().findViewById(R.id.progress_btn);
        attendance = binding.getRoot().findViewById(R.id.attendance_btn);
        payment = binding.getRoot().findViewById(R.id.payment_btn);

        // Set OnClickListener for all the buttons
        trackHours.setOnClickListener(this::trackHoursOnClick);
        mealPlan.setOnClickListener(this::mealPlanOnClick);
        childrenNeeds.setOnClickListener(this::childrenNeedsOnClick);
        progress.setOnClickListener(this::progressOnClick);
        attendance.setOnClickListener(this::attendanceOnClick);
        payment.setOnClickListener(this::paymentOnClick);

        return root;
    }

    // Handle the payment button
    private void paymentOnClick(View view) {
        // Set the title for the fragment
        titleChangeListener.onTitleChanged("Payment");

        FragmentManager fm = getParentFragmentManager();
        Fragment payment = null;
// For parent
        if (type != null && type.equals("parent")) {
            payment = new PaymentFragment();
        }
// For admin
        else if (type != null && type.equals("admin")) {
            payment = new AdminUpdatePayments();
        } else {
            Toast.makeText(requireContext(), "No access to the teachers to the payment", Toast.LENGTH_SHORT).show();
        }

        if (payment != null) {
            FragmentTransaction t = fm.beginTransaction();
            t.replace(R.id.nav_host_fragment_activity_main, payment);
            t.commit();
        } else {
            // Handle the case when the payment fragment is null
            // Show an error message or perform appropriate actions
        }



    }

    // Handle the attendance button
    private void attendanceOnClick(View view) {
        // Set the title for the fragment
        titleChangeListener.onTitleChanged("Attendance");

        FragmentManager fm = getParentFragmentManager();
        Fragment attendance = null;

// For admin OR teacher
        if (type != null && (type.equals("admin") || type.equals("teacher"))) {
            attendance = new AttendanceFragment();
        } else if (type != null && type.equals("parent")) { // For parent
            attendance = new ChildAttendanceFragment();
        }

        if (attendance != null) {
            FragmentTransaction t = fm.beginTransaction();
            t.replace(R.id.nav_host_fragment_activity_main, attendance);
            t.commit();
        }

    }

    // Handle the progress button
    private void progressOnClick(View view) {
        // Set the title for the fragment
        titleChangeListener.onTitleChanged("Progress");

        FragmentManager fm = getParentFragmentManager();
        Fragment progress = null;

// For parent
        if (type != null && type.equals("parent")) {
            progress = new ViewProgressFragment();
        }

// For teacher OR admin
        else if (type != null && (type.equals("admin") || type.equals("teacher"))) {
            progress = PersonSelectionFragment.newInstance("Progress");
        }

        if (progress != null) {
            FragmentTransaction t = fm.beginTransaction();
            t.replace(R.id.nav_host_fragment_activity_main, progress);
            t.commit();
        }

    }

    // Handle the mealPlan button
    private void mealPlanOnClick(View view) {
        // Set the title for the fragment
        titleChangeListener.onTitleChanged("Meal Plan");

        FragmentManager fm = getParentFragmentManager();
        Fragment mealPlan = null;

// For parent
        if (type != null && type.equals("parent")) {
            mealPlan = new MealPlanFragment();
        }
// For teacher OR admin
        else if (type != null && (type.equals("admin") || type.equals("teacher"))) {
            mealPlan = new ViewMealsFragment();
        }

        if (mealPlan != null) {
            FragmentTransaction t = fm.beginTransaction();
            t.replace(R.id.nav_host_fragment_activity_main, mealPlan);
            t.commit();
        }

    }

    // Handle the needs button
    private void childrenNeedsOnClick(View view) {
        // Set the title for the fragment
        titleChangeListener.onTitleChanged("Children Needs");

        FragmentManager fm = getParentFragmentManager();
        Fragment fragment = null;

// For parent
        if (type != null && type.equals("parent")) {
            fragment = new ChildrenNeedsFragment();
        }
// For teacher OR admin
        else if (type != null && (type.equals("admin") || type.equals("teacher"))) {
            fragment = new ViewAllergiesFragment();
        }

        if (fragment != null) {
            FragmentTransaction t = fm.beginTransaction();
            t.replace(R.id.nav_host_fragment_activity_main, fragment);
            t.commit();
        }

    }

    // Handle the track hours button
    private void trackHoursOnClick(View view) {
        // Set the title for the fragment
        titleChangeListener.onTitleChanged("Track Hours");

        FragmentManager fm = getParentFragmentManager();
        Fragment fragment = null;

// For teacher
        if (type != null && type.equals("teacher")) {
            fragment = new ViewHoursFragment();
        }
// For admin
        else if (type != null && type.equals("admin")) {
            fragment = PersonSelectionFragment.newInstance("Tracking hours");
        } else {
            Toast.makeText(requireContext(), "No access to the parents to the hours", Toast.LENGTH_SHORT).show();

        }

        if (fragment != null) {
            FragmentTransaction t = fm.beginTransaction();
            t.replace(R.id.nav_host_fragment_activity_main, fragment);
            t.commit();
        }

    }


    //  Callback interface for changing the action bar title
    public interface TitleChangeListener {
        void onTitleChanged(String newTitle);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Ensure that the activity implements the TitleChangeListener
        if (context instanceof TitleChangeListener) {
            titleChangeListener = (TitleChangeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TitleChangeListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}