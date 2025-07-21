package edu.psu.sweng888.practiceiv.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.psu.sweng888.practiceiv.R;

public class SettingsFragment extends Fragment {

    private Switch languageSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        languageSwitch = view.findViewById(R.id.switchLanguage);

        // Example setting: switch between English and another language
        languageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Language switched: Spanish", Toast.LENGTH_SHORT).show();
                // TODO: implement actual language change
            } else {
                Toast.makeText(getContext(), "Language switched: English", Toast.LENGTH_SHORT).show();
                // TODO: implement actual language change
            }
        });

        return view;
    }
}
