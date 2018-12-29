package br.com.apptransescolar.Activies;

import android.app.SearchManager;
import android.content.Context;
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
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import br.com.apptransescolar.API.ApiClient;
import br.com.apptransescolar.API.IKids;
import br.com.apptransescolar.Adpter.KidsAdapter;
import br.com.apptransescolar.Classes.Kids;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilhosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Kids> kids;
    private KidsAdapter kidAdapter;
    private IKids apiInterface;

    private ListView lstViewAddPassageiros;
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
        //getSupportActionBar().setTitle("Alunos Cadastrados");     //Titulo para ser exibido na sua Action Bar em frente à seta

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar = findViewById(R.id.progess);
        recyclerView = findViewById(R.id.kidsList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fetchAllKids("users", "");
    }

    private void fetchAllKids(String type, String key) {

        apiInterface = ApiClient.getApiClient().create(IKids.class);

        Call<List<Kids>> call = apiInterface.getAllKids(type, key);
        call.enqueue(new Callback<List<Kids>>() {
            @Override
            public void onResponse(Call<List<Kids>> call, Response<List<Kids>> response) {
                if (response == null){
                    Toast.makeText(FilhosActivity.this, "Nenhum passageiro localizado!", Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setVisibility(View.GONE);
                    kids = response.body();
                    kidAdapter = new KidsAdapter(kids, FilhosActivity.this);
                    recyclerView.setAdapter(kidAdapter);
                    kidAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Kids>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FilhosActivity.this, "Error\n"+t.toString(), Toast.LENGTH_LONG).show();
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
                fetchAllKids("users", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchAllKids("users", newText);
                return false;
            }
        });
        return true;
    }

}
