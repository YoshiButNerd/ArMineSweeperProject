package com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {
    private static MusicManager instance;
    private MediaPlayer mediaPlayer;
    private int currentResId = -1; // track which audio is playing

    private MusicManager() {}

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void startMusic(Context context, int resId, float volume) {
        // If the same track is already playing, just update volume
        if (mediaPlayer != null && mediaPlayer.isPlaying() && currentResId == resId) {
            setVolume(volume);
            return;
        }

        // Stop any old track
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        // Create new MediaPlayer
        mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(volume, volume);
        mediaPlayer.start();

        currentResId = resId;
    }

    // Stop music completely
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            currentResId = -1;
        }
    }

    // Set music volume (0.0f to 1.0f)
    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    // Check if music is playing
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
}
