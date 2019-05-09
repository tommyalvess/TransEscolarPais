package br.com.apptransescolar.Activies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import br.com.apptransescolar.API.ITios;
import br.com.apptransescolar.Adpter.TiosAdapter;
import br.com.apptransescolar.Classes.Tios;
import br.com.apptransescolar.Conexao.NetworkChangeReceiver4;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Tios> tios;
    private TiosAdapter tiosAdapter;
    private ITios iPai;
    ProgressBar progressBar;
    SessionManager sessionManager;
    String getId;
    private int id;

    static TextView tv_check_connection;
    private NetworkChangeReceiver4 mNetworkReceiver;
    static Window window;
    static RelativeLayout relativeLayout;
    static Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tios2);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Tios");     //Titulo para ser exibido na sua Action Bar em frente à seta

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        relativeLayout = findViewById(R.id.tioView);
        progressBar = findViewById(R.id.progess);
        recyclerView = findViewById(R.id.escolaList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mNetworkReceiver = new NetworkChangeReceiver4();
        registerNetworkBroadcastForNougat();
        window = TiosActivity.this.getWindow();


        IntentFilter inF1 = new IntentFilter("data_changed");
        LocalBroadcastManager.getInstance(TiosActivity.this).registerReceiver(dataChangeReceiver1,inF1);

        fetchTios();

    }

    private void fetchTios() {

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        id = Integer.parseInt(getId);
        iPai = ITios.retrofit.create(ITios.class);

        final Call<List<Tios>> call = iPai.getTios(id);

        call.enqueue(new Callback<List<Tios>>() {
            @Override
            public void onResponse(Call<List<Tios>> call, Response<List<Tios>> response) {
                if (!response.body().isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    tios = response.body();
                    tiosAdapter = new TiosAdapter(tios, TiosActivity.this);
                    recyclerView.setAdapter(tiosAdapter);
                    tiosAdapter.notifyDataSetChanged();
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(TiosActivity.this, "Opss! Você não tem tio vinculado!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tios>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Call", "carregar dados", t);
            }
        });
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_tio, menu);
        return true;

    };

    public void add(MenuItem item) {
        Intent intent = new Intent(TiosActivity.this, AddTiosActivity.class);
        startActivity(intent);
    }

    public static void dialogG(boolean value, Context context){

        if(value){
            snackbar.dismiss();
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
                    .make(relativeLayout, "Sem Conexão a Internet!", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
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

    private BroadcastReceiver dataChangeReceiver1= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // update your listview
            fetchTios();
        }
    };

}
