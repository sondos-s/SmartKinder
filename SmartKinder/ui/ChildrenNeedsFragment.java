package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.ChildAllergy;
import androidArmy.SmartKinder.backend.MyInfoManager;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChildrenNeedsFragment extends Fragment {

    private EditText  edittextOtherNeeds;
    private GridLayout gridLayout;
    private Button updateButton;
    private ImageButton backButton;

    private MyInfoManager infoManager;

    private List<String> allergies;
    // Declare a list to hold CheckBox objects
    private List<CheckBox> checkboxes = new ArrayList<>();


    public ChildrenNeedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_childrenneeds, container, false);

        // Retrieve the MyInfoManager instance from MainActivity
        infoManager = MyInfoManager.getInstance(requireContext());

        //Fetch the allergies names using myInfoMnagaer
        allergies = infoManager.getAllAllergiesNames();
        // Find the views by the ID
        gridLayout = view.findViewById(R.id.needs_allergies);
        updateButton = view.findViewById(R.id.needs_update_btn);
        backButton = view.findViewById(R.id.needs_back_button);
        edittextOtherNeeds = view.findViewById(R.id.edittext_other_needs);

        // Set OnClickListener for the buttons
        updateButton.setOnClickListener(v -> handleUpdateButton());
        backButton.setOnClickListener(v -> handleBackButton());

        // Replace with your database retrieval logic
        ArrayList<String> checkboxDataList = new ArrayList<>();
        //Create and add checkboxes for each allergy name
        for(String allergyName : allergies){
            checkboxDataList.add(allergyName);
        }

        // Iterate over the data from the database
        for (String checkboxData : checkboxDataList)
        {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(checkboxData);
            setCheckboxPadding(checkBox);
            checkboxes.add(checkBox);
            // Add the CheckBox to the GridLayout
            gridLayout.addView(checkBox);
        }

        preFill();

        return view;
    }



    // If the parent already selected allergies, then display the details
    private void preFill() {
        // Template for prefilling the data, replace it with the child's details
        List<String> kidAllergies = infoManager.getKidAllergies(123);
        //Check the allergies check boxes
        for(String childAllergy : kidAllergies){
            for(CheckBox checkBox : checkboxes){
                if(checkBox.getText().equals(childAllergy))
                    checkBox.setChecked(true);
            }
        }
        //Get kid's needs by it's id
        String needs = infoManager.getKidNeeds(123);
        // Set the edittextOtherNeeds with "Nothing"
        edittextOtherNeeds.setText(needs);


    }


    // Method to set padding for checkboxes
    private void setCheckboxPadding (CheckBox checkBox){
        int padding = getResources().getDimensionPixelSize(R.dimen.checkbox_padding);
        checkBox.setPadding(padding, 0, padding, 0);
    }


    // Handle the Update button
    private void handleUpdateButton() {

        // Create an ArrayList to store the selected checkboxes' data
        ArrayList<String> selectedNeedsList = new ArrayList<>();
        String otherNeeds = edittextOtherNeeds.getText().toString();

        // Iterate over the checkboxes
        for (CheckBox checkbox : checkboxes) {
            // Check if the checkbox is checked
            if (checkbox.isChecked()) {
                // Add the checkbox's text to the selectedNeedsList
                selectedNeedsList.add(checkbox.getText().toString());
            }
        }

        // Display the selected checkboxes' and the otherNeeds data as a toast message
        if (!selectedNeedsList.isEmpty()) {
            // Display the selectedNeedsList in a toast message
            StringBuilder toastMessage = new StringBuilder("");
            for (String need : selectedNeedsList) {
                toastMessage.append(need).append("\n");
            }
            Toast.makeText(getContext(), "Selected Allergies: " +toastMessage.toString()+ " Needs: " +otherNeeds, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "No needs selected", Toast.LENGTH_SHORT).show();
        }
        // Save to the database
        //Update the needs of the kid
        boolean success1 = infoManager.updateKidNeed(123, otherNeeds);
        //Update the kids allergies
        if(success1){
            boolean success2 = infoManager.updateKidAllergies(123, selectedNeedsList);
            if(success2){
                Toast.makeText(getContext(), "Kids needs and allergies updated", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getContext(), "The allergies not updated", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(getContext(), "Failed to Update", Toast.LENGTH_SHORT).show();
        }


    }


    // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        int backStackCount = fm.getBackStackEntryCount();
        // Return to the previous screen (view allergies)
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