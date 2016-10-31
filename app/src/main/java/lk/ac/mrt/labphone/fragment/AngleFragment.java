package lk.ac.mrt.labphone.fragment;


import android.app.Dialog;
import android.content.Context;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import lk.ac.mrt.labphone.R;
import lk.ac.mrt.labphone.listener.AngleSensorListener;

public class AngleFragment extends Fragment {

    private static final String TAG = AngleFragment.class.getSimpleName();

    private AngleSensorListener angleSensorListener;

    private int selectedAngle = 0;
    private int workingAngle = 0;

    private TextView textCurrent;
    private Button angleButton;
    private Button helpButton;
    private ImageButton soundButton;

    private int soundIds[];
    private SoundPool soundPool;
    private boolean soundEnabled = true;
    private SoundThread soundThread;

    private int threshold = 2;

    public AngleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_angle, container, false);

        textCurrent = (TextView) view.findViewById(R.id.text_current);
        angleButton = (Button) view.findViewById(R.id.button_angle);
        helpButton = (Button) view.findViewById(R.id.button_help);
        soundButton = (ImageButton) view.findViewById(R.id.button_sound);

        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        angleSensorListener = new AngleSensorListener(sensorManager, new AngleSensorListener.OnAngleValueChangedListener() {
            @Override
            public void onValueChanged(double x, double y, double z) {
                double current = y * 180 / Math.PI;

                workingAngle = Math.abs(Math.round((int) Math.round(current)));
                textCurrent.setText(workingAngle + getString(R.string.degree));

                Log.d(TAG, "WorkingAngle,selectedAngle:" + workingAngle + "," + selectedAngle);
                updateSoundRate(workingAngle, selectedAngle);
                if (Math.abs(workingAngle - selectedAngle) <= threshold) {
                    textCurrent.setTextColor(ContextCompat.getColor(getContext(), R.color.success));
                }else{
                    textCurrent.setTextColor(ContextCompat.getColor(getContext(), R.color.fail));
                }
            }
        });

        soundPool = initSoundPool();
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundIds = new int[1];
        soundIds[0] = soundPool.load(getActivity(), R.raw.beep, 1);

        soundThread = new SoundThread();
        soundThread.setLevel(100);

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEnabled = !soundEnabled;
                soundThread.setMute(!soundEnabled);
                updateSoundButtonView();
            }
        });

        angleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnglePickerDialog();
            }
        });
        updateSoundButtonView();

        showAnglePickerDialog();

        return view;
    }

    private void updateSoundButtonView() {
        if(soundEnabled){
            soundButton.setImageResource(R.drawable.ic_volume_up_black_24dp);
        }else{
            soundButton.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
    }

    private SoundPool initSoundPool() {
        SoundPool soundPool = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(attrs)
                    .build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        return soundPool;
    }

    private void playBeep() {
        soundPool.play(soundIds[0], 1, 1, 1, 0, 1.0F);
    }


    private void updateSoundRate(int workingAngle, int selectedAngle) {
        int level = 0;
        int diff = Math.abs(workingAngle - selectedAngle);

        if (diff <= threshold) {
            //TODO play success sound
//            level = 10;
            soundThread.pausePlay();
        } else {
            soundThread.resumePlay();
        }
        level = log(diff, 2);


        soundThread.setLevel(level);
    }

    static int log(int x, int base) {
        return (int) (Math.log(x) / Math.log(base));
    }

    @Override
    public void onResume() {
        super.onResume();
        angleSensorListener.registerSensorListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        angleSensorListener.unregisterSensorListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }

    private void showStart() {
        if(!soundThread.isAlive()) {
            soundThread.start();
        }
    }

    private void showAnglePickerDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Set Angle");
        dialog.setContentView(R.layout.dialog_angle_picker);
        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.picker_angle);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(90);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                Log.d(TAG, "Picker value changed:" + newValue);
            }
        });
        dialog.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAngle = numberPicker.getValue();
                angleButton.setText(selectedAngle + getString(R.string.degree));
                showStart();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private class SoundThread extends Thread {

        private int level = 10;
        private boolean stopped;
        private boolean enable = true;
        private boolean mute = !soundEnabled;

        public void setLevel(int level) {
            this.level = level;
        }

        public void pausePlay() {
            enable = false;
        }

        public void resumePlay() {
            enable = true;
        }

        public void setMute(boolean mute) {
            this.mute = mute;
        }

        @Override
        public void run() {
            super.run();
            while (!stopped) {
                try {
                    Thread.sleep(level * 100);
                    if(enable && !mute) {
                        playBeep();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
