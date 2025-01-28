package androidArmy.SmartKinder.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.MyInfoManager;


import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MessagingFragment extends Fragment {

    private ListView parentListView;
    private ListView teacherListView;
    private EditText messageEditText;
    private Button sendButton;
    private String teacherName;
    private String parentName;
    private String selectedTeacher;
    private String selectedParent;

    private MyInfoManager infoManager;

    public MessagingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messaging, container, false);

        // Find the views by the ID
        parentListView = view.findViewById(R.id.messaging_parent_list);
        teacherListView = view.findViewById(R.id.messaging_teacher_list);
        messageEditText = view.findViewById(R.id.messaging_message);
        sendButton = view.findViewById(R.id.messaging_send_btn);

        // Set OnClickListener for all the buttons
        sendButton.setOnClickListener(this::sendMessageOnClick);

        // Create an instance of MyInfoManager
        infoManager = MyInfoManager.getInstance(requireContext());

        // Get the teachers' names and populate the teacherListView
        List<String> teacherNames = infoManager.getTeachersNames();
        List<String> teacherNamesWithLabel = new ArrayList<>();
        //teacherNamesWithLabel.add("Select Teacher");
        teacherNamesWithLabel.addAll(teacherNames);
        ArrayAdapter<String> teacherAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, teacherNamesWithLabel);
        teacherListView.setAdapter(teacherAdapter);

        // Get the parents' names and populate the parentListView
        List<String> parentNames = infoManager.getAllKidsNames();
        List<String> parentNamesWithLabel = new ArrayList<>();
       // parentNamesWithLabel.add("Select Parent");
        for (String parentName : parentNames) {
            String parentNameWithLabel = parentName + " (Parent)";
            parentNamesWithLabel.add(parentNameWithLabel);
        }
        ArrayAdapter<String> parentAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, parentNamesWithLabel);
        parentListView.setAdapter(parentAdapter);

        // Set OnItemClickListener for parentListView
        parentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 selectedParent = parentAdapter.getItem(position);
                // Handle the selected parent
                //if (position != 0) {
                    // The user has selected a parent
                    // Extract the actual parent name without the label
                     parentName = selectedParent.replace(" (Parent)", "");
                    // Use the parentName as needed
               // }
            }
        });

        // Set OnItemClickListener for teacherListView
        teacherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 selectedTeacher = teacherAdapter.getItem(position);


                // Handle the selected teacher
              //  if (position != 0) {

                    // The user has selected a teacher
                    // Extract the actual teacher name without the label
                     teacherName = selectedTeacher;
                    // Use the teacherName as needed
               // }
            }
        });

        return view;
    }


    // Handle the send button
    private void sendMessageOnClick(View view) {
        String messageContent = messageEditText.getText().toString().trim();
        Log.d("ok",messageContent);

        if (!messageContent.isEmpty()) {
            String currentDate = getCurrentDate();

//            // Get the selected parent or teacher name
//            String selectedParent = parentListView.getSelectedItem();
//            String selectedTeacher = teacherListView.getSelectedItem().toString();
//            Log.d("ok",selectedParent);
//            Log.d("ok",selectedTeacher);
            if (selectedParent != null) {
                // The user selected a parent
                 parentName = selectedParent.replace(" (Parent)", "");
                int senderId = infoManager.getChildId(parentName);
                int receiverId = 1; // Assuming the receiver ID for the sender is always 1
                boolean messageInserted = infoManager.insertMessages(currentDate, messageContent, senderId, receiverId);

                if (messageInserted) {
                    // Show success message
                    Snackbar.make(view, "Message sent successfully to the parent", Snackbar.LENGTH_SHORT).show();
                } else {
                    // Show error message
                    Snackbar.make(view, "Failed to send message", Snackbar.LENGTH_SHORT).show();
                }
            } else if (selectedTeacher!=null) {
                // The user selected a teacher
                 teacherName = selectedTeacher;
                int senderId = infoManager.getTeacherId(teacherName);
                int receiverId = 1; // Assuming the receiver ID for the sender is always 1
                boolean messageInserted = infoManager.insertMessages(currentDate, messageContent, senderId, receiverId);

                if (messageInserted) {
                    // Show success message
                    Snackbar.make(view, "Message sent successfully to the teacher", Snackbar.LENGTH_SHORT).show();
                } else {
                    // Show error message
                    Snackbar.make(view, "Failed to send message", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                // No parent or teacher selected
                Snackbar.make(view, "Please select a parent or teacher", Snackbar.LENGTH_SHORT).show();
            }

            // Clear the message EditText
            messageEditText.setText("");
        } else {
            // Empty message content
            Snackbar.make(view, "Please enter a message", Snackbar.LENGTH_SHORT).show();
        }
    }

    // gets the current date
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
    // Handle the view button
    private void viewMessageOnClick(View view) {
        // Handle view button click
    }
}