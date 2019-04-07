package br.com.apptransescolar.Activies;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

import java.util.HashMap;

import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    SessionManager sessionManager;
    CoordinatorLayout coordinatorLayout;

    static String LoggedIn_User_Email;
    String getCpf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        checkLocationPermission();

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)


        coordinatorLayout = findViewById(R.id.coordinatorLayout);


        sessionManager = new SessionManager(this);

        sessionManager.checkLogin();

        //OneSignal
        OneSignal.startInit(this).init();

        // pegar infs da session
        HashMap<String, String> user = sessionManager.getUserDetail();
        getCpf = user.get(sessionManager.CPF);

        LoggedIn_User_Email = getCpf;

        OneSignal.sendTag("User_ID", LoggedIn_User_Email);

        CircleImageView imgFilhos =  findViewById(R.id.passageiros);
        CircleImageView imgEscola = findViewById(R.id.escolas);
        CircleImageView imgUsuario = findViewById(R.id.user);
        CircleImageView imgMap = findViewById(R.id.maps);
        CircleImageView imgTios = findViewById(R.id.tios);

        imgFilhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick","FilhosActivity");
                Intent intent = new Intent(HomeActivity.this, FilhosActivity.class);
                startActivity(intent);
            }
        }); //Fim do imgPass

        imgEscola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick","EscolasActivity");
                Intent intent = new Intent(HomeActivity.this, EscolaActivity.class);
                startActivity(intent);

            }
        });// Fim imgEscola


        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick","MapActivity");
                Intent intent = new Intent(HomeActivity.this, PaisMapsActivity.class);
                startActivity(intent);
            }
        }); // FIm do imgMap

        imgUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick","UsuarioActivity");
                Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        }); // Fim do imgUsuario

        imgTios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick","TiosActivity");
                Intent intent = new Intent(HomeActivity.this, TiosActivity.class);
                startActivity(intent);
            }
        }); // Fim do imgPais

    }//onCreate

    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }// verificaConexao


    @Override
    protected void onResume() {
        super.onResume();

        if (verificaConexao() == true) {
            sessionManager.checkLogin();
        } else {
            Toast.makeText(this, "Sem conexão!!!", Toast.LENGTH_SHORT).show();
        }
    }// onResume

    private void checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("De permissão!")
                        .setMessage("O app precisa da permissão para usar o localização!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            }
            else{
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

}// HomeActivity