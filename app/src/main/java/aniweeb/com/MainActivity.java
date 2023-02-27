package aniweeb.com;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import aniweeb.com.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private TextView txt_contactus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_slideshow, R.id.nav_gallery, R.id.nav_home, R.id.nav_random)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        /* Inside the activity */
// Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        TextView txtFecha = headerView.findViewById(R.id.txtFecha);
        // Obtienes la fecha actual
        Month mes = null;
        DayOfWeek dia = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int year = LocalDate.now().getYear();
            mes = LocalDate.now().getMonth();
            dia = LocalDate.now().getDayOfWeek();
            // Obtienes el nombre del mes
            String month = mes.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            String day = dia.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            txtFecha.setText(ucFirst(dia.toString())+ ", " + LocalDate.now().getDayOfMonth() + " " + ucFirst(mes.toString()) + " of " + year);
        }

        txt_contactus = findViewById(R.id.txt_contactus);
        txt_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarEmail();
            }
        });

// Get access to the custom title view
    }

    public void  enviarEmail(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
       //intent.putExtra(Intent.EXTRA_SUBJECT, "Asunto de prieba");
       // intent.putExtra(Intent.EXTRA_TEXT, "Probando el envio");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"aniweeb.contact@gmail.com"});
        startActivity(intent);
    }

    public static String ucFirst(String str) { //devuelve la primera letra en mayuscula
        if (str == null || str.isEmpty()) {
            return "";
        } else {
            return  Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()).toLowerCase();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}