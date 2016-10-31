package lk.ac.mrt.labphone.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Arrays;

public class AngleSensorListener implements SensorEventListener {

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private SensorManager sensorManager;
    private OnAngleValueChangedListener listener;
    private SensorEventListener eventListener;

    public AngleSensorListener(SensorManager sensorManager, OnAngleValueChangedListener listener) {
        this.sensorManager = sensorManager;
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }

        // Update rotation matrix, which is needed to update orientation angles.
        sensorManager.getRotationMatrix(mRotationMatrix, null,
                mAccelerometerReading, mMagnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        sensorManager.getOrientation(mRotationMatrix, mOrientationAngles);

        // "mOrientationAngles" now has up-to-date information.
//        System.out.println(Arrays.toString(mOrientationAngles));

        double x = -mOrientationAngles[2] ;
        double y = mOrientationAngles[1] ;
        double z = mOrientationAngles[0] ;

        if (listener != null) {
            listener.onValueChanged(x, y, z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // do nothing
    }

    public void registerSensorListener() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterSensorListener() {
        sensorManager.unregisterListener(this);
    }

    public interface OnAngleValueChangedListener {
        void onValueChanged(double x, double y, double z);

    }

}