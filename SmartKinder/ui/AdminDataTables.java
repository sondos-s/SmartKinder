package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AdminDataTables extends Fragment {

    private ListView listView;
    private Button nextButton;
    private int selectedItemPosition;
    private TitleChangeListener titleChangeListener;


    private String[] items = {"Meals", "Children",  "Teachers", "Allergies"};

    public static AdminDataTables newInstance() {
        return new AdminDataTables();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_data_tables, container, false);

        listView = view.findViewById(R.id.admin_data_tables_list);
        nextButton = view.findViewById(R.id.admin_data_tables_next_btn);

        titleChangeListener.onTitleChanged("Edit Data");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view1, position, id) -> {
            String selectedItem = (String) adapterView.getItemAtPosition(position);
            Toast.makeText(requireContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
        });

        selectedItemPosition = -1;
        // Set OnItemClickListener for the ListView to handle item selection
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            handleListItemClick(position);
        });

        // Set OnClickListener for the next button
        nextButton.setOnClickListener(v -> handleNextButton());

        return view;
    }



    // Handle the next button
    private void handleNextButton() {
        if (selectedItemPosition == -1) {
            // Show a dialog box indicating that the user needs to select an item first
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Error");
            builder.setMessage("Please select the table you want to edit first.");
            builder.setPositiveButton("OK", null);
            builder.show();
        } else {

            Fragment fragment = null;
            String selectedOption = items[selectedItemPosition];
            Bundle bundle = new Bundle();
            switch (selectedOption) {
                case "Meals":
                    titleChangeListener.onTitleChanged("Meals");
                    fragment = new AdminTablesView();
                    bundle.putString("key", "Meals");
                    // Set the arguments for the destination fragment
                    fragment.setArguments(bundle);
                    break;
                case "Teachers":
                    titleChangeListener.onTitleChanged("Teachers");
                    fragment = new AdminTablesView();
                    bundle.putString("key", "Teachers");
                    // Set the arguments for the destination fragment
                    fragment.setArguments(bundle);
                    break;
                case "Children":
                    titleChangeListener.onTitleChanged("Children");
                    fragment = new AdminTablesView();
                    bundle.putString("key", "Children");
                    // Set the arguments for the destination fragment
                    fragment.setArguments(bundle);
                    break;
                case "Allergies":
                    titleChangeListener.onTitleChanged("Allergies");
                    fragment = new AdminTablesView();
                    bundle.putString("key", "Allergies");
                    // Set the arguments for the destination fragment
                    fragment.setArguments(bundle);
                    break;
                default:
                    Toast.makeText(requireContext(), "Invalid option selected", Toast.LENGTH_SHORT).show();
                    return;
            }

            if (fragment != null) {
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.nav_host_fragment_activity_main,fragment);
                t.addToBackStack(null);
                t.commit();
            }
        }
    }

    // Handle the list item (table name) click
    private void handleListItemClick(int position) {
        selectedItemPosition = position;
        nextButton.setEnabled(true);
        for (int i = 0; i < listView.getChildCount(); i++) {
            View listItem = listView.getChildAt(i);
            listItem.setBackgroundColor(i == position ? Color.BLUE: Color.TRANSPARENT);
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
        if (context instanceof AdminDataTables.TitleChangeListener) {
            titleChangeListener = (AdminDataTables.TitleChangeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TitleChangeListener");
        }
    }

}
