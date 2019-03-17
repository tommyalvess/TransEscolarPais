package br.com.apptransescolar.Activies;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.apptransescolar.API.ApiClient;
import br.com.apptransescolar.API.IKids;
import br.com.apptransescolar.API.ITios;
import br.com.apptransescolar.Adpter.FilhosAdapter;
import br.com.apptransescolar.Adpter.KidsAdapter;
import br.com.apptransescolar.Adpter.TiosAdapter;
import br.com.apptransescolar.Classes.FilhosDAO;
import br.com.apptransescolar.Classes.Kids;
import br.com.apptransescolar.Classes.Tios;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilhosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Kids> kids;
    private KidsAdapter kidsAdapter;
    private IKids iKids;
    private TextView textAviso;
    ProgressBar progressBar;
    SessionManager sessionManager;
    String getId;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filhos);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        //getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Filhos Cadastrados");     //Titulo para ser exibido na sua Action Bar em frente à seta

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar = findViewById(R.id.progess);
        recyclerView = findViewById(R.id.paisList);
        textAviso = findViewById(R.id.textAviso);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        textAviso.setVisibility(View.GONE);


        fetchKids();

    }

    private void fetchKids() {

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        id = Integer.parseInt(getId);
        iKids = IKids.retrofit.create(IKids.class);

        final Call<List<Kids>> call = iKids.getKids(id);

        call.enqueue(new Callback<List<Kids>>() {
            @Override
            public void onResponse(Call<List<Kids>> call, Response<List<Kids>> response) {
                if (!response.body().isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    textAviso.setVisibility(View.GONE);
                    kids = response.body();
                    kidsAdapter = new KidsAdapter(kids, FilhosActivity.this);
                    recyclerView.setAdapter(kidsAdapter);
                    kidsAdapter.notifyDataSetChanged();
                }else {
                    progressBar.setVisibility(View.GONE);
                    textAviso.setVisibility(View.VISIBLE);
                    Toast.makeText(FilhosActivity.this, "Opss! Você não tem tio vinculado!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Kids>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                textAviso.setVisibility(View.VISIBLE);
                Toast.makeText(FilhosActivity.this, "Opss! Nada foi encontrado!", Toast.LENGTH_SHORT).show();
                Log.e("Call", "carregar dados", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == R.id.action_edit ) {
            Intent it = new Intent(this, CadastroFilhoActivity.class);
            startActivity(it);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_aluno, menu);
        return true;

    };

    public void add(MenuItem item) {
        Intent it = new Intent(this, CadastroFilhoActivity.class);
        startActivity(it);
    }

}
