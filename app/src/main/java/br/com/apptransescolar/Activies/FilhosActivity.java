package br.com.apptransescolar.Activies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import br.com.apptransescolar.API.ApiClient;
import br.com.apptransescolar.API.IKids;
import br.com.apptransescolar.Adpter.KidsAdapter;
import br.com.apptransescolar.Classes.Kids;
import br.com.apptransescolar.Conexao.NetworkChangeReceiver2;
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
    static RelativeLayout relativeLayoutF;
    private NetworkChangeReceiver2 mNetworkReceiver;
    static Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filhos);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        //getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Filhos");     //Titulo para ser exibido na sua Action Bar em frente à seta

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mNetworkReceiver = new NetworkChangeReceiver2();
        registerNetworkBroadcastForNougat();

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        IntentFilter inF1 = new IntentFilter("data_changed");
        LocalBroadcastManager.getInstance(FilhosActivity.this).registerReceiver(dataChangeReceiver1,inF1);


        relativeLayoutF = findViewById(R.id.relativeLayoutF);
        progressBar = findViewById(R.id.progess);
        recyclerView = findViewById(R.id.paisList);
        textAviso = findViewById(R.id.textAviso);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        textAviso.setVisibility(View.GONE);

        //fetchKids();
        fetchKid("users", "", getId);


    }

    private void fetchKid(String type, String key, String id) {
        iKids = ApiClient.getApiClient().create(IKids.class);

        Call<List<Kids>> call = iKids.getKids(type, key, id);
        call.enqueue(new Callback<List<Kids>>() {
            @Override
            public void onResponse(Call<List<Kids>> call, Response<List<Kids>> response) {
                if (!response.body().isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    kids = response.body();
                    kidsAdapter = new KidsAdapter(kids, FilhosActivity.this);
                    recyclerView.setAdapter(kidsAdapter);
                    kidsAdapter.notifyDataSetChanged();
                }else {
                    progressBar.setVisibility(View.GONE);
                    textAviso.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<List<Kids>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Chamada", "Erro", t);
            }
        });
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

                    snackbar = showSnackbar(relativeLayoutF, Snackbar.LENGTH_LONG, FilhosActivity.this);
                    snackbar.show();
                    View view = snackbar.getView();
                    TextView tv = (TextView) view.findViewById(R.id.textSnack);
                    tv.setText("Nenhum filho localizado!");
                }
            }

            @Override
            public void onFailure(Call<List<Kids>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                textAviso.setVisibility(View.VISIBLE);

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

    public static void dialogF(boolean value, final Context context){

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
            snackbar = showSnackbar(relativeLayoutF, Snackbar.LENGTH_INDEFINITE, context);
            snackbar.show();
            View view = snackbar.getView();
            TextView tv = (TextView) view.findViewById(R.id.textSnack);
            tv.setText("Sem Conexão a Internet!");

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
            fetchKid("users", "", getId);
        }
    };

    private static Snackbar showSnackbar(RelativeLayout coordinatorLayout, int duration, Context context) {
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
