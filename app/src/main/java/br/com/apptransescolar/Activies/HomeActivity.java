package br.com.apptransescolar.Activies;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.Window;
import android.view.WindowManager;
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
    CircleImageView imgFilhos,imgEscola,imgUsuario,imgTios;

    private final Handler handler = new Handler();

    boolean conectado;

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

        imgFilhos =  findViewById(R.id.passageiros);
        imgEscola = findViewById(R.id.escolas);
        imgUsuario = findViewById(R.id.user);
        imgTios = findViewById(R.id.tios);

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

        //doTheAutoRefresh();

    }//onCreate

    //Refresh
    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                verificaConexao();
                if (verificaConexao() == true) {
                    sessionManager.checkLogin();

                    Window window = HomeActivity.this.getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this,R.color.AzulTema));

                    ActionBar bar = getSupportActionBar();
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#007fff")));

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
                }else {

                    Window window = HomeActivity.this.getWindow();

                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                    window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this,R.color.red));

                    ActionBar bar = getSupportActionBar();
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff0000")));

                    imgFilhos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }); //Fim do imgPass

                    imgEscola.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });// Fim imgEscola

                    imgUsuario.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }); // Fim do imgUsuario

                    imgTios.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }); // Fim do imgPais
                }
                doTheAutoRefresh();
            }
        }, 1);
    }

    public  boolean verificaConexao() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            conectado = true;
        }
        else
            conectado = false;
        return conectado;
    }// verificaConexao


    @Override
    protected void onResume() {
        super.onResume();

        //doTheAutoRefresh();

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