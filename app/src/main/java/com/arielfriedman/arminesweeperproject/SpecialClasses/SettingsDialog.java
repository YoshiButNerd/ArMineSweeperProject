package com.arielfriedman.arminesweeperproject.SpecialClasses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.arielfriedman.arminesweeperproject.R;

public class SettingsDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_activity, null);
        Button btnSave = view.findViewById(R.id.saveBtn);
        SeekBar musicSlider = view.findViewById(R.id.volumeMusicBar);
        SeekBar sfxSlider = view.findViewById(R.id.volumeSfxBar);
        Switch notifSwitch = view.findViewById(R.id.notifCheck);
        btnSave.setOnClickListener(v -> {
            boolean notify = notifSwitch.isChecked();
            dismiss();
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get the dialog window
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.90); // 85% of screen width
            int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.85); // 85% of screen width
            dialog.getWindow().setLayout(width, height);
        }
    }
}
