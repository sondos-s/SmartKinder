package androidArmy.SmartKinder.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.databinding.FragmentNotificationsBinding;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}