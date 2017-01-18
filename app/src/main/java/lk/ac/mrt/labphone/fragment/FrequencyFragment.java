package lk.ac.mrt.labphone.fragment;

import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import in.excogitation.zentone.library.ToneStoppedListener;
import in.excogitation.zentone.library.ZenTone;
import lk.ac.mrt.labphone.MainActivity;
import lk.ac.mrt.labphone.R;
import lk.ac.mrt.labphone.view.RoundKnobButton;

import static android.R.attr.duration;
import static android.R.attr.id;
import static android.R.attr.x;
import static android.R.id.toggle;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.widget.CompoundButton.*;
import static lk.ac.mrt.labphone.R.string.frequency;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrequencyFragment extends Fragment {
    private int freq = 5000;
    private int duration = 1;
    private boolean isPlaying = false;
    ToggleButton toggle;

    public FrequencyFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_frequency, container, false);

        // inputs
        final SeekBar frequencySeek = (SeekBar) v.findViewById(R.id.seekBarFrequency);
        frequencySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView freqLabel = (TextView) v.findViewById(R.id.textViewFrequencyNo);
                freqLabel.setText(String.valueOf(progress));
                freq = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ZenTone.getInstance().stop();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar frequencyDuration = (SeekBar) v.findViewById(R.id.seekBarDuration);
        frequencyDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView durLabel = (TextView) v.findViewById(R.id.textViewDurationNo);
                durLabel.setText(String.valueOf(progress));
                duration = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ZenTone.getInstance().stop();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // sound generator
        toggle = (ToggleButton) v.findViewById(R.id.toggleButtonFrequency);
        toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // isPlaying = isChecked;
                    // The toggle is enabled
                    // playSound(1500);
                    handleTonePlay(freq, duration);
                } else {
                    // The toggle is disabled
                    // stopSound(mAudioTrack);
                    ZenTone.getInstance().stop();
                }
            }
        });
        RelativeLayout.LayoutParams lp;
        RelativeLayout panel = (RelativeLayout) v.findViewById(R.id.rotator_knob); // new RelativeLayout(this);
        final RoundKnobButton rv = new RoundKnobButton(v.getContext(), R.drawable.stator, R.drawable.rotoron, R.drawable.rotoroff, (int) getResources().getDimension(R.dimen.knob_size), (int) getResources().getDimension(R.dimen.knob_size));
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        panel.addView(rv, lp);

        rv.setRotorPercentage(0);

        rv.SetListener(new RoundKnobButton.RoundKnobButtonListener() {
            public void onStateChange(boolean newstate) {
                // Toast.makeText(MainActivity.this, "New state:" + newstate, Toast.LENGTH_SHORT).show();
            }

            public void onRotate(final int percentage) {
                frequencySeek.post(new Runnable() {
                    public void run() {
                        frequencySeek.setProgress(1500 * percentage / 100);
                    }
                });
            }
        });

        rv.post(new Runnable() {
            @Override
            public void run() {
                rv.setRotorPercentage(0);
            }
        });

        return v;
    }

    private void handleTonePlay(int freq, int duration) {
        if (!isPlaying) {
            // Play Tone
            ZenTone.getInstance().generate(freq, duration, 0.5f, new ToneStoppedListener() {
                @Override
                public void onToneStopped() {
                    toggle.setChecked(false);
                    isPlaying = false;
                }
            });

            isPlaying = true;
        } else {
            // Stop Tone
            ZenTone.getInstance().stop();
            isPlaying = false;
        }
    }
// legacy method
//    @TargetApi(Build.VERSION_CODES.CUPCAKE)
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void playSound(double frequency) {
//        while (isPlaying) {
//            // initialize audio
//            // AudioTrack definition
//            int mBufferSize = AudioTrack.getMinBufferSize(44100,
//                    AudioFormat.CHANNEL_OUT_MONO,
//                    AudioFormat.ENCODING_PCM_8BIT);
//
//            // Sine wave
//            final double[] mSound = new double[44100];
//            final short[] mBuffer = new short[duration];
//            for (int i = 0; i < mSound.length; i++) {
//                mSound[i] = Math.sin((2.0 * Math.PI * i / (44100 / frequency)));
//                mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
//
//            }
//            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
//                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
//                    mBufferSize, AudioTrack.MODE_STREAM);
//            mAudioTrack.play();
//
//            mAudioTrack.write(mBuffer, 0, mSound.length);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                mAudioTrack.setVolume(1.0f);
//            }
//        }
//    }
//
//    private void stopSound(AudioTrack mAudioTrack) {
//        isPlaying = false;
//        if (mAudioTrack != null) {
//            mAudioTrack.stop();
//            mAudioTrack.release();
//        }
//    }
}
