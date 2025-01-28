package  androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidArmy.SmartKinder.backend.Allergies;
import androidArmy.SmartKinder.backend.InfoKid;
import androidArmy.SmartKinder.backend.Meal;
import androidArmy.SmartKinder.backend.MyInfoManager;
import androidArmy.SmartKinder.backend.Teacher;

public class AdminTablesView extends Fragment {
    private static final int REQUEST_CODE = 1;

    private ListView listView;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private ImageButton backButton;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;
    private  String value = null;
    private int selectedItemPosition;


    // List of data for each table
    private List<Teacher> teacherList;
    private List<Meal> mealList;
    private List<InfoKid> childList;
    private List<Allergies> allergyList;

    // Adapters for each table
    private ArrayAdapter<Teacher> teacherAdapter;
    private ArrayAdapter<Meal> mealAdapter;
    private ArrayAdapter<InfoKid> childAdapter;
    private ArrayAdapter<Allergies> allergyAdapter;



    MyInfoManager myInfoManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_tables_view, container, false);
        myInfoManager = MyInfoManager.getInstance(getContext());
        // Find the views by the ID
        listView = view.findViewById(R.id.admin_tables_view_list);
        addButton = view.findViewById(R.id.admin_view_add_btn);
        editButton = view.findViewById(R.id.admin_view_edit_btn);
        deleteButton = view.findViewById(R.id.admin_view_delete_btn);
        backButton = view.findViewById(R.id.admin_tables_view_back_button);


        // Retrieve the arguments
        Bundle arguments = getArguments();
        if (arguments != null)
        {
            value = arguments.getString("key");
        }

        // Set adapters according to the argument
        switch (value)
        {
            case "Meals" :
                meal();
                break;
            case "Teachers" :
                teacher();
                break;
            case "Allergies" :
                allergy();
                break;
            case "Children" :
                children();
                break;
            default:
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        selectedItemPosition = -1;
        // Set OnItemClickListener for the ListView to handle item selection
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            handleListItemClick(position);
        });

        // Set OnClickListener for the buttons
        backButton.setOnClickListener(v -> handleBackButton());
        addButton.setOnClickListener(this::addBtnOnClick);
        editButton.setOnClickListener(this::editBtnOnClick);
        deleteButton.setOnClickListener(this::deleteBtnOnClick);


        return view;
    }

    // Handle the list item click
    private void handleListItemClick(int position) {
        selectedItemPosition = position;
        for (int i = 0; i < listView.getChildCount(); i++) {
            View listItem = listView.getChildAt(i);
            listItem.setBackgroundColor(i == position ? Color.BLUE: Color.TRANSPARENT);
        }
    }

    // Handle the edit button click
    private void editBtnOnClick(View view) {

        if (selectedItemPosition == -1) {
            // Show a dialog box indicating that the user needs to select an item first
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Error");
            builder.setMessage("Please select the record you want to update first.");
            builder.setPositiveButton("OK", null);
            builder.show();
        } else {

            if (value.equals("Teachers")) {// Handle edit for teacher item
                // Get the selected Teacher object
                Teacher selectedTeacher = teacherList.get(selectedItemPosition);
                // Pass the selected teacher to the activity
                Intent teacherIntent = new Intent(getActivity(), AdminUpdateTeachers.class);
                teacherIntent.putExtra("key", "edit");
                teacherIntent.putExtra("selectedItem", (Parcelable) selectedTeacher);
                startActivityForResult(teacherIntent, REQUEST_CODE);
            } else if (value.equals("Meals")) {// Handle edit for meal item
                // Get the selected Meal object
                Meal selectedMeal = mealList.get(selectedItemPosition);
                // Pass the selected meal to the activity
                Intent mealIntent = new Intent(getActivity(), AdminUpdateMeals.class);
                mealIntent.putExtra("key", "edit");
                mealIntent.putExtra("selectedItem", (Parcelable) selectedMeal);
                startActivityForResult(mealIntent, REQUEST_CODE);
            } else if (value.equals("Children")) {// Handle edit for child item
                // Get the selected Child object
                InfoKid selectedChild = childList.get(selectedItemPosition);
                // Pass the selected child to the activity
                Intent childIntent = new Intent(getActivity(), AdminUpdateChildren.class);
                childIntent.putExtra("key", "edit");
                childIntent.putExtra("selectedItem", (Parcelable) selectedChild);
                startActivityForResult(childIntent, REQUEST_CODE);
            } else if (value.equals("Allergies")) {// Handle edit for allergy item
                // Get the selected Allergy object
                Allergies selectedAllergy = allergyList.get(selectedItemPosition);
                // Pass the selected allergy to the activity
                Intent allergyIntent = new Intent(getActivity(), AdminUpdateAllergies.class);
                allergyIntent.putExtra("key", "allergy");
                allergyIntent.putExtra("selectedItem", (Parcelable) selectedAllergy);
                startActivityForResult(allergyIntent, REQUEST_CODE);
            }
        }
    }


    private void addBtnOnClick(View view) {
        Intent intent;
        if (value.equals("Teachers")) {// Handle add for teacher item
            intent = new Intent(getActivity(), AdminUpdateTeachers.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (value.equals("Meals")) {// Handle add for meal item
            intent = new Intent(getActivity(), AdminUpdateMeals.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (value.equals("Children")) {// Handle add for child item
            intent = new Intent(getActivity(), AdminUpdateChildren.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (value.equals("Allergies")) {// Handle add for allergy item
            intent = new Intent(getActivity(), AdminUpdateAllergies.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    // Handle the delete button click
    private void deleteBtnOnClick(View view) {
        if (selectedItemPosition == -1) {
            // Show a dialog box indicating that the user needs to select an item first
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Error");
            builder.setMessage("Please select the record you want to delete first.");
            builder.setPositiveButton("OK", null);
            builder.show();
        } else {
            // Build the alert dialog
            buildAlertDialog();
            dialog.show();
            // Set click listener for "Yes" button
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Save the changes
                    if (value.equals("Teachers")) {// Handle delete for teacher item
                        // Delete from the database
                        // Get the chosen teacher from the list
                        Teacher selectedTeacher = teacherList.get(selectedItemPosition);
                        boolean success = myInfoManager.deleteTeacher(selectedTeacher);
                        if(success){
                            // Delete the item selected
                            teacherList.remove(selectedItemPosition);
                            teacherAdapter.notifyDataSetChanged();
                            Toast.makeText(requireContext(), "Teacher was deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Deleting failed", Toast.LENGTH_SHORT).show();
                        }

                    } else if (value.equals("Meals")) {// Handle delete for meal item

                        // Delete from the database
                        // Get the chosen teacher from the list
                        Meal selectedMeal = mealList.get(selectedItemPosition);
                        boolean success = myInfoManager.deleteMeal(selectedMeal);
                        if(success) {
                            // Delete the item selected
                            mealList.remove(selectedItemPosition);
                            mealAdapter.notifyDataSetChanged();
                            Toast.makeText(requireContext(), "Meal was deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Deleting failed", Toast.LENGTH_SHORT).show();
                        }

                    } else if (value.equals("Children")) {// Handle delete for child item
                        // Delete from the database
                        InfoKid selectedKid = childList.get(selectedItemPosition);
                        boolean success = myInfoManager.deleteChild(selectedKid);
                        if(success) {
                            // Delete the item selected
                            childList.remove(selectedItemPosition);
                            childAdapter.notifyDataSetChanged();
                            Toast.makeText(requireContext(), "Child was deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(requireContext(), "Deleting failed", Toast.LENGTH_SHORT).show();
                        }

                    } else if (value.equals("Allergies")) {// Handle delete for allergy item

                        // Delete from the database
                        Allergies selectedAllergy = allergyList.get(selectedItemPosition);
                        boolean success = myInfoManager.deleteAllergy(selectedAllergy);
                        if(success) {
                            // Delete the item selected
                            allergyList.remove(selectedItemPosition);
                            allergyAdapter.notifyDataSetChanged();
                            Toast.makeText(requireContext(), "Allergy was deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(requireContext(), "Deleting failed", Toast.LENGTH_SHORT).show();
                        }

                    }
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

    // Fill the list view with meals data
    private void meal() {
        mealList = new ArrayList<>();

        mealList = myInfoManager.getAllMeals();

        mealAdapter = new ArrayAdapter<Meal>(getContext(), R.layout.list_meals_table, mealList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_meals_table, parent, false);
                }

                Meal mealItem = getItem(position);

                TextView idTextView = convertView.findViewById(R.id.admin_meal_list_id_col);
                TextView nameTextView = convertView.findViewById(R.id.admin_meal_list_name_col);
                TextView typeTextView = convertView.findViewById(R.id.admin_meal_list_type_col);

                idTextView.setText(String.valueOf(mealItem.getId()));
                nameTextView.setText(mealItem.getMealName());
                typeTextView.setText(mealItem.getType());

                return convertView;
            }
        };

        listView.setAdapter(mealAdapter);
    }



    // Fill the list view with teachers data
    private void teacher() {
        teacherList = new ArrayList<>();

        teacherList = myInfoManager.getAllTeachers();

        teacherAdapter = new ArrayAdapter<Teacher>(getContext(), R.layout.list_teachers_table, teacherList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_teachers_table, parent, false);
                }

                Teacher teacherItem = getItem(position);

                TextView idTextView = convertView.findViewById(R.id.admin_teachers_list_id_col);
                TextView fnameTextView = convertView.findViewById(R.id.admin_teachers_list_firstname_col);
                TextView lnameTextView = convertView.findViewById(R.id.admin_teachers_list_lastname_col);

                idTextView.setText(String.valueOf(teacherItem.getTeacherId()));
                fnameTextView.setText(teacherItem.getFirstName());
                lnameTextView.setText(teacherItem.getLastName());

                return convertView;
            }
        };

        listView.setAdapter(teacherAdapter);
    }



    // Fill the list view with allergies data
    private void allergy() {
        allergyList = new ArrayList<>();

       allergyList = myInfoManager.getAllAllergies();
        allergyAdapter = new ArrayAdapter<Allergies>(getContext(), R.layout.list_allergies_table, allergyList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_allergies_table, parent, false);
                }
                Allergies allergy = getItem(position);
                TextView nameTextView = convertView.findViewById(R.id.admin_allergy_list_name_col);
                nameTextView.setText(allergy.getAllergyName());
                return convertView;
            }
        };
        listView.setAdapter(allergyAdapter);
    }



    // Fill the list view with children data
    private void children() {
        childList = new ArrayList<>();

        MyInfoManager myInfoManager = MyInfoManager.getInstance(getContext());
        childList  = myInfoManager.getAllKids();
        childAdapter = new ArrayAdapter<InfoKid>(getContext(), R.layout.list_children_table, childList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_children_table, parent, false);
                }
                InfoKid kid = getItem(position);

                TextView id = convertView.findViewById(R.id.admin_children_list_id_col);
                TextView name = convertView.findViewById(R.id.admin_children_list_name_col);
                TextView mother = convertView.findViewById(R.id.admin_children_list_mother_col);
                TextView father = convertView.findViewById(R.id.admin_children_list_father_col);
                TextView birthdate = convertView.findViewById(R.id.admin_children_list_birthdate_col);
                TextView needs = convertView.findViewById(R.id.admin_children_list_needs_col);

                id.setText(String.valueOf(kid.getId()));
                name.setText(kid.getName());
                mother.setText(kid.getMotherId());
                father.setText(kid.getFatherId());
                birthdate.setText(String.valueOf(kid.getBirthdate()));
                needs.setText(kid.getNeeds());

                return convertView;
            }
        };
        listView.setAdapter(childAdapter);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {


//            // Invalidate the views in the ListView
//            listView.invalidateViews();
            // Set adapters according to the argument
            switch (value)
            {
                case "Meals" :
                    meal();
                    mealAdapter.notifyDataSetChanged();
                    break;
                case "Teachers" :
                    teacher();
                    teacherAdapter.notifyDataSetChanged();
                    break;
                case "Allergies" :
                    allergy();
                    allergyAdapter.notifyDataSetChanged();
                    break;
                case "Children" :
                    children();
                    childAdapter.notifyDataSetChanged();
                    break;
                default:
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

     // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack();
    }
}
