package br.com.apptransescolar.Activies;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onesignal.OneSignal;

import java.util.HashMap;

import br.com.apptransescolar.Conexao.NetworkChangeReceiver;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    SessionManager sessionManager;
    static CoordinatorLayout coordinatorLayout;

    static String LoggedIn_User_Email;
    String getCpf;
    static ImageView imgHome;
    static TextView tv_check_connection;
    static CircleImageView imgFilhos;
    static CircleImageView imgEscola;
    static CircleImageView imgUsuario;
    static CircleImageView imgTios;

    private final Handler handler = new Handler();

    public static String conectado;
    private BroadcastReceiver mNetworkReceiver;
    static Thread thread;
    static Animation animation;
    static CoordinatorLayout coordinatorLayoutt;
    static Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();

        animation = AnimationUtils.loadAnimation(HomeActivity.this,R.anim.blink);

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

        tv_check_connection = findViewById(R.id.tv_check_connection);
        imgFilhos = findViewById(R.id.passageiros);
        imgEscola = findViewById(R.id.escolas);
        imgUsuario = findViewById(R.id.user);
        imgTios = findViewById(R.id.tios);

        imgFilhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick", "FilhosActivity");
                Intent intent = new Intent(HomeActivity.this, FilhosActivity.class);
                startActivity(intent);
            }
        }); //Fim do imgPass

        imgEscola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick", "EscolasActivity");
                Intent intent = new Intent(HomeActivity.this, EscolaActivity.class);
                startActivity(intent);

            }
        });// Fim imgEscola

        imgUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick", "UsuarioActivity");
                Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        }); // Fim do imgUsuario

        imgTios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick", "TiosActivity");
                Intent intent = new Intent(HomeActivity.this, TiosActivity.class);
                startActivity(intent);
            }
        }); // Fim do imgPais


    }//onCreate


    public static void dialog(boolean value, final Context context){

        if(value){
            snackbar.dismiss();
            imgFilhos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OnClick", "FilhosActivity");
                    Intent intent = new Intent(context, FilhosActivity.class);
                    context.startActivity(intent);
                }
            }); //Fim do imgPass

            imgEscola.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OnClick", "EscolasActivity");
                    Intent intent = new Intent(context, EscolaActivity.class);
                    context.startActivity(intent);

                }
            });// Fim imgEscola

            imgUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OnClick", "UsuarioActivity");
                    Intent intent = new Intent(context, PerfilActivity.class);
                    context.startActivity(intent);
                }
            }); // Fim do imgUsuario

            imgTios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OnClick", "TiosActivity");
                    Intent intent = new Intent(context, TiosActivity.class);
                    context.startActivity(intent);
                }
            }); // Fim do imgPais
            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    snackbar.dismiss();
                }
            };
            handler.postDelayed(delayrunnable, 300);
        }else {
            snackbar = Snackbar
                    .make(coordinatorLayout, "Sem Conexão a Internet!", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

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
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    public  static void callAnimation()
    {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tv_check_connection.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv_check_connection.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tv_check_connection.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }// onResume

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }




}// HomeActivity