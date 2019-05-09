package br.com.apptransescolar.Activies;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.apptransescolar.API.ApiClient;
import br.com.apptransescolar.API.IEscolas;
import br.com.apptransescolar.Adpter.EscolaAdapter;
import br.com.apptransescolar.Classes.Escolas;
import br.com.apptransescolar.Conexao.NetworkChangeReceiver;
import br.com.apptransescolar.Conexao.NetworkChangeReceiver3;
import br.com.apptransescolar.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.apptransescolar.Activies.FilhosActivity.relativeLayoutF;

public class EscolaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Escolas> contacts;
    private EscolaAdapter adapter;
    private IEscolas apiInterface;
    ProgressBar progressBar;
    TextView textAviso;
    String[] item;
    static Snackbar snackbar;
    private NetworkChangeReceiver3 mNetworkReceiver;
    static CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escola);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        progressBar = findViewById(R.id.progess);
        recyclerView = findViewById(R.id.escolaList);
        textAviso = findViewById(R.id.textAviso);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        //layoutManager = new LinearLayoutManager(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        fetchEscolas("users", "");

        textAviso.setVisibility(View.GONE);

        mNetworkReceiver = new NetworkChangeReceiver3();
        registerNetworkBroadcastForNougat();

    }

    private void fetchEscolas(String type, String key) {

        apiInterface = ApiClient.getApiClient().create(IEscolas.class);

        Call<List<Escolas>> call = apiInterface.getEscolas(type, key);
        call.enqueue(new Callback<List<Escolas>>() {
            @Override
            public void onResponse(Call<List<Escolas>> call, Response<List<Escolas>> response) {
                if (!response.body().isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    textAviso.setVisibility(View.GONE);
                    contacts = response.body();
                    adapter = new EscolaAdapter(contacts, EscolaActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    progressBar.setVisibility(View.GONE);
                    textAviso.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<Escolas>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                textAviso.setVisibility(View.VISIBLE);
                Toast.makeText(EscolaActivity.this, "Error\n"+t.toString(), Toast.LENGTH_LONG).show();
                Log.e("Chamada", "Erro", t);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_escola, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchEscolas("users", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchEscolas("users", newText);
                return false;
            }
        });
        return true;
    }

    public static void dialogE(boolean value, final Context context){

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
                    .make(coordinatorLayout, "Sem Conexão a Internet!", Snackbar.LENGTH_INDEFINITE);
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

}
