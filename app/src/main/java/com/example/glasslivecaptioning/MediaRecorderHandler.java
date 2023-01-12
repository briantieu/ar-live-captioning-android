package com.example.glasslivecaptioning;

import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.IOException;

/**
 * Provides functionality of recording AUDIO using {@link MediaRecorder}.
 */
public class MediaRecorderHandler {

    private static final String TAG = MediaRecorderHandler.class.getSimpleName();

    private MediaRecorder recorder;
    private boolean isRecording = false;
    private File outputFile;
    private Surface surface;

    /**
     * Sets output file for the video recording and initializes {@link MediaRecorder}.
     */
    public void initRecorder() {
        Log.d(TAG, "Initializing audio recorder");
        outputFile = FileManager.getOutputVideoFile();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        recorder.setOutputFile(outputFile.getPath());

    }

    /**
     * Prepares {@link MediaRecorder} and sets the surface to recording.
     */
    public void prepareRecorder() {
        Log.d(TAG, "Preparing audio recorder");
        try {
            recorder.prepare();
            surface = recorder.getSurface();
        } catch (IllegalStateException | IOException e) {
            Log.e(TAG, "Preparing audio recorder failed", e);
        }
    }

    /**
     * Returns TRUE if {@link MediaRecorder} is in the middle of recording. FALSE otherwise.
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * Starts {@link MediaRecorder} and sets {@link MediaRecorderHandler#isRecording} to true.
     */
    public void startRecording() {
        if (!isRecording) {
            Log.d(TAG, "Start recording");
            recorder.start();
            isRecording = true;
            return;
        }
        Log.d(TAG, "Recording is already started");
    }

    /**
     * Stops {@link MediaRecorder} and sets {@link MediaRecorderHandler#isRecording} to false. Resets {@link
     * MediaRecorder} to prepare it for the next recording. Deletes output file if {@link Exception}
     * occurs.
     */
    public void stopRecording() {
        try {
            if (isRecording) {
                Log.d(TAG, "Stop recording");
                recorder.stop();
            } else {
                Log.d(TAG, "Recording is already stopped");
            }
            recorder.reset();
        } catch (Exception e) {
            if (outputFile.delete()) {
                Log.d(TAG, "File has been deleted");
            }
            Log.e(TAG, "Recording stop failed", e);
        }
        isRecording = false;
    }

    /**
     * Returns last recorded {@link File} object.
     */
    public File getLastRecordedFile() {
        return outputFile;
    }

    /**
     * Releases {@link MediaRecorder}.
     */
    public void releaseMediaRecorder() {
        if (recorder != null) {
            Log.d(TAG, "Releasing media recorder");
            recorder.release();
            return;
        }
        Log.d(TAG, "Media recorder is null");
    }


    /**
     * Returns recording surface.
     */
    public Surface getSurface() {
        return surface;
    }
}
