package br.com.apptransescolar.Activies;

import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.apptransescolar.API.ApiClient;
import br.com.apptransescolar.API.IEscolas;
import br.com.apptransescolar.API.ITios;
import br.com.apptransescolar.Adpter.AddTiosAdapter;
import br.com.apptransescolar.Adpter.EscolaAdapter;
import br.com.apptransescolar.Adpter.TiosAdapter;
import br.com.apptransescolar.Classes.Escolas;
import br.com.apptransescolar.Classes.Tio;
import br.com.apptransescolar.Classes.Tios;
import br.com.apptransescolar.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTiosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Tio> tios;
    private AddTiosAdapter tiosAdapter;
    private ITios iTios;
    ProgressBar progressBar;
    ConstraintLayout constraintLayoutAdd;
    static Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tios);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Tios Cadastrados");     //Titulo para ser exibido na sua Action Bar em frente à seta

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar = findViewById(R.id.progess);
        recyclerView = findViewById(R.id.escolaList);
        constraintLayoutAdd = findViewById(R.id.constraintLayoutAdd);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fetchTios("users", "");
    }

    private void fetchTios(String type, String key) {

        iTios = ApiClient.getApiClient().create(ITios.class);

        Call<List<Tio>> call = iTios.getAllTios(type, key);

        call.enqueue(new Callback<List<Tio>>() {
            @Override
            public void onResponse(Call<List<Tio>> call, Response<List<Tio>> response) {
                if (!response.body().isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    tios = response.body();
                    tiosAdapter = new AddTiosAdapter(tios, AddTiosActivity.this);
                    recyclerView.setAdapter(tiosAdapter);
                    tiosAdapter.notifyDataSetChanged();
                }else {
                    progressBar.setVisibility(View.GONE);

                    snackbar = showSnackbar(constraintLayoutAdd, Snackbar.LENGTH_LONG, AddTiosActivity.this);
                    snackbar.show();
                    View view = snackbar.getView();
                    TextView tv = (TextView) view.findViewById(R.id.textSnack);
                    tv.setText("Nenhum tio localizado!");
                }
            }

            @Override
            public void onFailure(Call<List<Tio>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
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
                fetchTios("users", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchTios("users", newText);
                return false;
            }
        });
        return true;
    }

    private static Snackbar showSnackbar(ConstraintLayout coordinatorLayout, int duration, Context context) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "", duration);
        // 15 is margin from all the sides for snackbar
        int marginFromSides = 15;

        float height = 100;

        //inflate view
        LayoutInflater inflater = (LayoutInflater)context.getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View snackView = inflater.inflate(R.layout.snackbar_layout, null);

        // White background
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        // for rounded edges
//        snackbar.getView().setBackground(getResources().getDrawable(R.drawable.shape_oval));

        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();
        parentParams.setMargins(marginFromSides, 0, marginFromSides, marginFromSides);
        parentParams.height = (int) height;
        parentParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        snackBarView.setLayoutParams(parentParams);

        snackBarView.addView(snackView, 0);
        return snackbar;
    }
}
