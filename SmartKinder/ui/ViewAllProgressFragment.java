package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.InfoKid;
import androidArmy.SmartKinder.backend.MyInfoManager;
import androidArmy.SmartKinder.backend.Progress;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewAllProgressFragment extends Fragment {

    private ImageButton backButton;
    private Button addButton , deleteButton;
    private  ListView listView;
    private int selectedItemPosition ;
    private SimpleAdapter adapter;
    private List<Map<String, String>> progressItems;
    private InfoKid selectedChild;

    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;
    private MyInfoManager infoManager;

    private List<Progress> progresses;


    public ViewAllProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_progress, container, false);

        // Retrieve the MyInfoManager instance from MainActivity
        infoManager = MyInfoManager.getInstance(requireContext());

        // Find and initialize the views
        listView = view.findViewById(R.id.list_view_all_progress);
        backButton = view.findViewById(R.id.view_all_progress_back_button);
        deleteButton = view.findViewById(R.id.view_all_progress_delete_button);
        addButton = view.findViewById(R.id.view_all_progress_add_button);

        Bundle args = getArguments();
        if (args != null) {
            selectedChild = args.getParcelable("child");
        }

        //Get kid's progresses by it's name
        progresses = infoManager.getProgressesByKid(selectedChild.getName());


        // Use the selectedChild to retrieve his progresses
        progressItems = new ArrayList<>();
        for(Progress progress : progresses){
            progressItems.add(createProgressItem(progress.getProgressDate(), progress.getKidProgress()));
        }

        // Create the adapter and set it to the ListView
        String[] from = {"date", "progress"};
        int[] to = {R.id.progress_date_text, R.id.progress_progress_text};
        adapter = new SimpleAdapter(getContext(), progressItems, R.layout.progress_item, from, to);

        listView.setAdapter(adapter);

        // Set OnClickListener for the back button
        backButton.setOnClickListener(v -> handleBackButton());

        // Set OnClickListener for the add button
        addButton.setOnClickListener(v -> handleAddButton());

        selectedItemPosition = -1;
        // Set OnItemClickListener for the ListView to handle item selection
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            handleListItemClick(position);
        });

        // Set OnClickListener for the delete button
        deleteButton.setOnClickListener(v -> handleDeleteButton());

        return view;
    }

    // Handle the add button
    private void handleAddButton() {
        Bundle args = new Bundle();
        args.putParcelable("child", selectedChild);
        FragmentManager fm = getParentFragmentManager();
        SendProgressFragment progress = new SendProgressFragment();
        progress.setArguments(args);
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.nav_host_fragment_activity_main,progress);
        t.addToBackStack(null);
        t.commit();
    }


    // Helper method to create a HashMap for each hour item
    private Map<String, String> createProgressItem(String date, String progress) {
        Map<String, String> progressItem = new HashMap<>();
        progressItem.put("date", date);
        progressItem.put("progress", progress);
        return progressItem;
    }



    // Handle the list item (child progress) click
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
        } if (selectedItemPosition != -1 && selectedItemPosition < progressItems.size()) {
            // Build the alert dialog
            buildAlertDialog();
            dialog.show();
            // Set click listener for "Yes" button
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Save the changes
                    // Delete the item selected , delete from db
                    Progress selectedProgress = progresses.get(selectedItemPosition);
                    infoManager.deleteProgress(selectedChild.getName(),1,selectedProgress.getProgressDate());
                    progressItems.remove(selectedItemPosition);
                    String[] from = {"date", "progress"};
                    int[] to = {R.id.progress_date_text, R.id.progress_progress_text};
                    adapter = new SimpleAdapter(getContext(), progressItems, R.layout.progress_item, from, to);

                    listView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                    resetListBackground(); // Reset list item background
                    selectedItemPosition = -1;
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

    // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack();
    }

}
