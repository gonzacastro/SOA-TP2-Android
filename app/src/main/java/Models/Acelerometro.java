package Models;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class Acelerometro {
    private SensorManager accelerometer;
            Sensor sensorA;
    private SensorEventListener sensorListener;
    public static final int SHAKE_LIMIT = 10;


    public Acelerometro(Activity actualActivity){

        accelerometer = (SensorManager) actualActivity.getSystemService(Context.SENSOR_SERVICE);
        sensorA = accelerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensorA == null){
            actualActivity.finish();
        }

        sensorListener = new SensorEventListener() {

            long ultimaAct = 0;
            float last_x;
            float last_y;
            float last_z;
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(sensorA.getType() == SensorManager.SENSOR_ACCELEROMETER){
                    long tiempoActual = System.currentTimeMillis();
                    if((tiempoActual - ultimaAct) > 100){
                        long diffTime = (tiempoActual - ultimaAct);
                        ultimaAct = tiempoActual;

                        float x = event.values[0];
                        float y = event.values[1];
                        float z = event.values[2];


                        float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                        if(speed > SHAKE_LIMIT){
                            Log.e("Speed"," " + speed);
                      last_x = x;  }
                     last_y = y;
                     last_z = z;

                    }




                }
                else{
                    actualActivity.finish();
                }




            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }


}
