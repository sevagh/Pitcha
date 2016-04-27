package com.sevag.pitcha.recording;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.sevag.pitcha.dsp.MPM;
import com.sevag.pitcha.music.NotePitchMap;
import com.sevag.pitcha.uihelper.UIHelper;

/**
 * Created by sevag on 11/25/14.
 */
public class AudioRecorder {

    static {
        System.loadLibrary("mpm");
    }

    public static native double get_pitch_from_short(short[] data);

    private static AudioRecord recorder;
    private static short[] data;
    private static final int SAMPLES = 1024;
    private static boolean shouldStop = false;
    private static UIHelper uiHelper;
    private static MPM mpm;

    public AudioRecorder() {
    }

    private static int getMaxValidSampleRate() {
        int maxRate = 0;
        for (int rate : new int[] {8000, 11025, 16000, 22050, 44100, 48000}) {
            int bufferSize = AudioRecord.getMinBufferSize(rate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            if (bufferSize > 0) {
                maxRate = rate;
            }
        }
        return maxRate;
    }

    public static void init(UIHelper paramUiHelper) {
        int sampleRate = getMaxValidSampleRate();
        mpm = new MPM(sampleRate, SAMPLES, 0.93);
        int N = AudioRecord.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        data = new short[SAMPLES];
        uiHelper = paramUiHelper;
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                N * 10);
        shouldStop = false;
        recorder.startRecording();
    }

    public static void run() {
        while ((!shouldStop)) {
            try {
                recorder.read(data, 0, data.length);
                double pitch1 = mpm.getPitchFromShort(data);
                double pitch2 = get_pitch_from_short(data);
                System.out.println("Pitch1: " + pitch1 + ", pitch2: " + pitch2);
                NotePitchMap.displayNoteOf(pitch1, uiHelper);
            } catch (Throwable x) {
                x.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public static void deinit() {
        shouldStop = true;
        recorder.stop();
        recorder.release();
    }
}