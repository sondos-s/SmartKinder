package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;

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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewMealsFragment extends Fragment {

    private ImageButton backButton;

    private  ListView listView;
    private  TextView dateTextView;


    public ViewMealsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_meals, container, false);

        // Find and initialize the views
        listView = view.findViewById(R.id.list_view_meals);
        dateTextView = view.findViewById(R.id.view_meal_date);
        backButton = view.findViewById(R.id.view_meal_back_button);

        // Create a SimpleDateFormat instance with the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Get the current date
        Date currentDate = new Date();

        // Format the current date as a string
        String formattedDate = dateFormat.format(currentDate);
        // Add the date to the title
        dateTextView.setText(formattedDate);

        // Create an ArrayList to hold the meal data
        ArrayList<MealItem> mealItems = new ArrayList<>();
        mealItems.add(new MealItem("Reem", "Eggs", "Salad"));
        mealItems.add(new MealItem("Emily", "Pancakes", "Hotdog"));

        // Create the adapter and set it to the ListView
        ArrayAdapter<MealItem> adapter = new ArrayAdapter<MealItem>(getContext(), R.layout.meal_item, R.id.child_name_text, mealItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.meal_item, parent, false);
                }

                MealItem mealItem = getItem(position);

                TextView nameTextView = convertView.findViewById(R.id.child_name_text);
                TextView breakfastTextView = convertView.findViewById(R.id.breakfast_text);
                TextView lunchTextView = convertView.findViewById(R.id.lunch_text);

                nameTextView.setText(mealItem.getChildName());
                breakfastTextView.setText(mealItem.getBreakfast());
                lunchTextView.setText(mealItem.getLunch());

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


    // Inner class to represent a single meal item
    private static class MealItem {
        private String childName;
        private String breakfast;
        private String lunch;

        public MealItem(String childName, String breakfast, String lunch) {
            this.childName = childName;
            this.breakfast = breakfast;
            this.lunch = lunch;
        }

        public String getChildName() {
            return childName;
        }

        public String getBreakfast() {
            return breakfast;
        }

        public String getLunch() {
            return lunch;
        }
    }
}
