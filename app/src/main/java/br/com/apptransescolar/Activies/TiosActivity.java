package br.com.apptransescolar.Activies;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import br.com.apptransescolar.API.ApiClient;
import br.com.apptransescolar.API.ITios;
import br.com.apptransescolar.Adpter.TiosAdapter;
import br.com.apptransescolar.Classes.Tios;
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

        progressBar = findViewById(R.id.progess);
        recyclerView = findViewById(R.id.escolaList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

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
                Toast.makeText(TiosActivity.this, "Opss! Nada foi encontrado!", Toast.LENGTH_SHORT).show();
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

}
