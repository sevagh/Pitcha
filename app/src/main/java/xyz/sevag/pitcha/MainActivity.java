package xyz.sevag.pitcha;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.*;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.Manifest;

import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
import xyz.sevag.pitcha.recording.AudioRecorder;
import xyz.sevag.pitcha.uihelper.UIHelper;

public class MainActivity extends Activity implements UIHelper, ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView noteTextView;
    private Thread audioThread;
    private final int REQUEST_AUDIO_RECORD = 0;
    private boolean recordPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteTextView = (TextView) findViewById(R.id.noteOutputTextView);
        ToggleButton toggle = (ToggleButton) findViewById(R.id.ndkToggleBtn);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AudioRecorder.enableNdk();
                } else {
                    AudioRecorder.disableNdk();
                }
            }
        });
    }

    @Override
    public void display(final String note, final double err) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!note.isEmpty()) {
                    if ((99.5 < err) && (err < 100.5)) {
                        noteTextView.setText(note);
                        noteTextView.setTextColor(Color.GREEN);
                    } else {
                        noteTextView.setText(note);
                        noteTextView.setTextColor(Color.RED);
                    }
                } else {
                    noteTextView.setText("");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        endHook();
        super.onPause();
    }

    @Override
    protected void onResume() {
        startHook();
        super.onResume();
    }

    private void endHook() {
        AudioRecorder.deinit();
        try {
            audioThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    private void startHook() {
        handlePermissions();
        AudioRecorder.init(this);
        launchPitcha();
    }

    private void handlePermissions() {
        switch (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
            case PackageManager.PERMISSION_DENIED:
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_AUDIO_RECORD);
                break;
            case PackageManager.PERMISSION_GRANTED:
                recordPermission = true;
                break;
        }
    }

    private void launchPitcha() {
        if (recordPermission) {
            audioThread = new Thread(new Runnable() {
                public void run() {
                    android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                    AudioRecorder.run();
                }
            });
            audioThread.start();
        } else {
            System.exit(-1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_RECORD: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recordPermission = true;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

