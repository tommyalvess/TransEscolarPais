package br.com.apptransescolar.Activies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    SessionManager sessionManager;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)


        coordinatorLayout = findViewById(R.id.coordinatorLayout);


        sessionManager = new SessionManager(this);

        sessionManager.checkLogin();

        // pegar infs da session
        HashMap<String, String> user = sessionManager.getUserDetail();

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


}// HomeActivity