package utils;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener{
    //define several variables to be considered

    private final static float SHAKE_THRESHOLD_GRAVITY = 2.75f;
    private final static int SHAKE_SLOPE = 1000;
    private final static int SHAKE_COUNT_TIME = 3000;
    private OnShakeListener onShakeListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    public interface OnShakeListener
    {
        public void OnShake(int count);
    }

    public void setOnShakeListener(OnShakeListener listener)
    {
        this.onShakeListener = listener;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (onShakeListener!=null)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            float gx = x / SensorManager.GRAVITY_EARTH;
            float gy = y / SensorManager.GRAVITY_EARTH;
            float gz = z / SensorManager.GRAVITY_EARTH;

            float gforce = (float) Math.sqrt(gx*gx+gy*gy+gz*gz);
            if (gforce>SHAKE_THRESHOLD_GRAVITY)
            {
                final long now = System.currentTimeMillis();
                if (mShakeTimestamp+SHAKE_SLOPE>now)
                {
                    //ignore shakes that are too close to each other for applicability purposes
                    return;
                }

                if (mShakeTimestamp+SHAKE_COUNT_TIME<now)
                {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;
                onShakeListener.OnShake(mShakeCount);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
