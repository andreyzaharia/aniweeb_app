package aniweeb.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import aniweeb.com.MainActivity;
import aniweeb.com.R;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 21/02/2023.
 */
public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIEMPO = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new Handler().postDelayed(new Runnable() {
            /*
             * Mostramos la pantalla de bienvenida con un temporizador.
             * De esta forma se puede mostrar el logo de la app o
             * compañia durante unos segundos.
             */
            @Override
            public void run() {
                //Este método se ejecuta cuando se consume el tiempo del temporizador.
                //Se pasa a la activity principal
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                // Cerramos esta activity (error pq ya la cierro abajo)
                //finish();
            }
        }, SPLASH_TIEMPO);
    }
}
