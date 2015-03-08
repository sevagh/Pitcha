package com.sevag.pitcha;

import android.app.Activity;
import android.os.*;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;

import com.sevag.pitcha.recording.AudioRecorder;
import com.sevag.pitcha.gauge.NeedleGauge;
import com.sevag.pitcha.uihelper.UIHelper;

public class MainActivity extends Activity implements UIHelper {

    private NeedleGauge needleGauge;
    private Thread audioThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        needleGauge = (NeedleGauge) findViewById(R.id.needlegauge);
    }

    private void launchPitcha() {
        audioThread = new Thread(new Runnable() {
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                AudioRecorder.run();
            }
        });

        audioThread.start();
    }

    @Override
    public void display(final String note, final double err) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                needleGauge.setHandTarget(note, err);
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
        AudioRecorder.init(this);
        launchPitcha();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
