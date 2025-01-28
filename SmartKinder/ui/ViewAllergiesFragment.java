package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;

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
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class ViewAllergiesFragment extends Fragment{

    private ListView listViewAllergies;
    private ImageButton backButton;



    public ViewAllergiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_allergies, container, false);

        // Find the views by the ID
        backButton = view.findViewById(R.id.view_allergies_back_button);
        listViewAllergies = view.findViewById(R.id.view_allergies_list);

        // Create sample data
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("Kim, Allergies Peanut, Needs X");
        allergiesList.add("John, Allergies Milk, Needs Y");
        allergiesList.add("Ross, Allergies Dust, Needs Z");

        // Create ArrayAdapter and set it to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, allergiesList);
        listViewAllergies.setAdapter(adapter);

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

}



