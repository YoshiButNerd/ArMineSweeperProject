package com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

public class SfxManager {
    private static MediaPlayer sfxPlayer;

    // Play any sound resource
    public static void play(Context context, int resId) {
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        int sfxVol = prefs.getInt("sfx_volume", 50);
        float volume = sfxVol / 100f;

        if (sfxPlayer != null) {
            sfxPlayer.release();
        }

        sfxPlayer = MediaPlayer.create(context, resId);
        sfxPlayer.setVolume(volume, volume);
        sfxPlayer.start();
        sfxPlayer.setOnCompletionListener(MediaPlayer::release);
    }
}
