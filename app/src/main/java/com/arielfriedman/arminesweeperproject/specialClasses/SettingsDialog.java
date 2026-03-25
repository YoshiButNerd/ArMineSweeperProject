package com.arielfriedman.arminesweeperproject.specialClasses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.widget.SwitchCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.arielfriedman.arminesweeperproject.R;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.MusicManager;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.SfxManager;

public class SettingsDialog extends AppCompatDialogFragment {
    private static final String PREFS_NAME = "settings";
    private static final String KEY_NOTIF = "notifications_enabled";
    private static final String KEY_MUSIC_VOL = "music_volume";
    private static final String KEY_SFX_VOL = "sfx_volume";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_activity, null);

        // XML elements
        Button btnSave = view.findViewById(R.id.saveBtn);
        btnSave.setSoundEffectsEnabled(false);
        addPressAnimation(btnSave);
        SeekBar musicSlider = view.findViewById(R.id.volumeMusicBar);
        SeekBar sfxSlider = view.findViewById(R.id.volumeSfxBar);
        SwitchCompat notifSwitch = view.findViewById(R.id.notifCheck);

        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);

        // Load saved values
        notifSwitch.setChecked(prefs.getBoolean(KEY_NOTIF, true));
        musicSlider.setProgress(prefs.getInt(KEY_MUSIC_VOL, 50));
        sfxSlider.setProgress(prefs.getInt(KEY_SFX_VOL, 50));

        // Save button listener
        btnSave.setOnClickListener(v -> {
            SfxManager.play(btnSave.getContext(), R.raw.sfx_clickbtn);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_NOTIF, notifSwitch.isChecked());
            editor.putInt(KEY_MUSIC_VOL, musicSlider.getProgress());
            editor.putInt(KEY_SFX_VOL, sfxSlider.getProgress());
            editor.apply();

            float volume = musicSlider.getProgress() / 100f;
            MusicManager.getInstance().setVolume(volume);

            dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    private void addPressAnimation(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
                } else if (event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                }
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.85);
            dialog.getWindow().setLayout(width, height);
        }
    }
}
