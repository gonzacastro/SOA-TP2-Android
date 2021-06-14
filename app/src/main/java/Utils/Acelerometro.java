package Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;




//import static com.example.GUI.HomeMenuActivity.home;


public class Acelerometro {
    private SensorManager accelerometer;
    private Sensor sensorA;
    private SensorEventListener sensorListener;
    private AlertDialog alertDialog;
    public static final int SHAKE_LIMIT = 800;
    public static int contShake = 0;

    public Acelerometro(){

    }

    public boolean setShake(Context context){
        //accelerometer = (SensorManager) home.getSystemService(Context.SENSOR_SERVICE);

        sensorA = accelerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensorA == null){
            return false;
        }
        sensorListener = new SensorEventListener() {
            long ultimaAct = 0;
            float last_x;
            float last_y;
            float last_z;
            @Override
            public void onSensorChanged(SensorEvent event) {
                    long tiempoActual = System.currentTimeMillis();

                    if((tiempoActual - ultimaAct) > 100){
                        long diffTime = (tiempoActual - ultimaAct);
                        ultimaAct = tiempoActual;
                        float x = event.values[0];
                        float y = event.values[1];
                        float z = event.values[2];
                        float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;
                        if(speed > SHAKE_LIMIT){
                                System.out.println("Speed: " + speed);
                                contShake++;
                             }
                        last_x = x;
                        last_y = y;
                        last_z = z;
                        if(contShake == 2){
                            System.out.println("Salimos lpm");
                            System.exit(1);
                        }
                    }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        start();
        return true;
    }
    private void start(){
        accelerometer.registerListener(sensorListener,sensorA,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stop(){

    }

    public void setSensorManager(SensorManager accelerometer){
        this.accelerometer = accelerometer;
    }


}
