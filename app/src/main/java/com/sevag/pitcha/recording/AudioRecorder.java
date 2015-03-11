package com.sevag.pitcha.recording;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.sevag.pitcha.music.NotePitchMap;
import com.sevag.pitcha.uihelper.UIHelper;

/**
 * Created by sevag on 11/25/14.
 */
public class AudioRecorder {

    static {
        System.loadLibrary("pitchalib");
    }

    public static native double get_pitch_from_short(short[] data);

    private static AudioRecord recorder;
    private static short[] data;
    private static final int SAMPLE_RATE = 48000;
    private static final int SAMPLES = 1024;
    private static boolean shouldStop = false;
    private static int N;
    private static UIHelper uiHelper;

    public AudioRecorder() {
    }

    public static void init(UIHelper paramUiHelper) {
        N = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        data = new short[SAMPLES];
        uiHelper = paramUiHelper;
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, N * 10);
        shouldStop = false;
        recorder.startRecording();
    }

    public static void run() {
        while ((shouldStop == false)) {
            try {
                recorder.read(data, 0, data.length);
                double pitch = get_pitch_from_short(data);
                NotePitchMap.displayNoteOf(pitch, uiHelper);
                recorder.stop();
            } catch (Throwable x) {
                x.printStackTrace();
                System.exit(-1);
            }
        }
        return;
    }

    public static void deinit() {
        shouldStop = true;
        recorder.stop();
        recorder.release();
    }
}