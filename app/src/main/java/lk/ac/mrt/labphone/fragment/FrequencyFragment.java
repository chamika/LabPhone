package lk.ac.mrt.labphone.fragment;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import lk.ac.mrt.labphone.R;

import static android.R.attr.duration;
import static android.R.attr.id;
import static android.R.id.toggle;
import static android.widget.CompoundButton.*;
import static lk.ac.mrt.labphone.R.string.frequency;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrequencyFragment extends Fragment {


    public FrequencyFragment() {
        // Required empty public constructor
    }

    AudioTrack mAudioTrack;
    boolean isPlaying = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frequency, container, false);

        ToggleButton toggle = (ToggleButton) v.findViewById(R.id.toggleButtonFrequency);
        toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    playSound(220);
                } else {
                    // The toggle is disabled
                    stopSound(mAudioTrack);
                }
            }
        });
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playSound(double frequency) {

        // initialize audio
        // AudioTrack definition
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        // Sine wave
        final double[] mSound = new double[44100];
        final short[] mBuffer = new short[duration];
        for (int i = 0; i < mSound.length; i++) {
            mSound[i] = Math.sin((2.0 * Math.PI * i / (44100 / frequency)));
            mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
        }

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        mAudioTrack.setVolume(1.0f);

        mAudioTrack.play();

        isPlaying = true;

        mAudioTrack.write(mBuffer, 0, mSound.length);
    }

    private void stopSound(AudioTrack mAudioTrack) {
        isPlaying = false;
        if (mAudioTrack != null) {
            mAudioTrack.stop();
            mAudioTrack.release();
        }
    }
}
