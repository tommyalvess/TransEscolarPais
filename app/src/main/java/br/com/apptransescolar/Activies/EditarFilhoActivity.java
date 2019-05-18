package br.com.apptransescolar.Activies;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.apptransescolar.Adpter.SpinnerEscolaAdapter;
import br.com.apptransescolar.Adpter.SpinnerTiosAdapter;
import br.com.apptransescolar.Classes.Escolas;
import br.com.apptransescolar.Classes.Kids;
import br.com.apptransescolar.Classes.Tio;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;

import static br.com.apptransescolar.API.URLs.PATH_TO_SERVER;
import static br.com.apptransescolar.API.URLs.PATH_TO_SERVER2;
import static br.com.apptransescolar.API.URLs.URL_EDIT;
import static br.com.apptransescolar.API.URLs.URL_EDIT_FILHO;
import static br.com.apptransescolar.API.URLs.URL_EDIT_FILHO_FULL;

public class EditarFilhoActivity extends AppCompatActivity {

    Spinner spinnerPeriodo, spinnerTios, spinnerEscola;
    EditText editNomeT,dtNasc,endT;
    Button btnSaveCadastro;

    String id;
    String getId, getNome, getDtNas, getEnd;
    int tio;
    int escola;
    String periodo;

    //Spinner
    List<Tio> spinnerTioData;
    List<Escolas> spinnerEscolaData;
    String[] name  = {"Manhã", "Tarde"};
    String record = "";

    ArrayAdapter<String> arrayAdapter;

    private RequestQueue queue;

    SessionManager sessionManager;
    static Snackbar snackbar;
    static ConstraintLayout  constraintLayoutEdi;

    Kids kids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_filho);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        kids = (Kids) getIntent().getExtras().get("kids");

        //Dados do pai
        sessionManager = new SessionManager(this);

        getId = String.valueOf(kids.getIdKids());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Alterar Cadastro");

        spinnerPeriodo = findViewById(R.id.spinnerP);
        spinnerTios = findViewById(R.id.spinnerT);
        spinnerEscola = findViewById(R.id.spinnerE);
        constraintLayoutEdi = findViewById(R.id.constraintLayoutEdi);

        editNomeT = findViewById(R.id.editNomeT);
        dtNasc = findViewById(R.id.dtNasc);
        endT = findViewById(R.id.end);
        btnSaveCadastro = findViewById(R.id.btnSaveCadastro);

        // Spinner periodo
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);
        spinnerPeriodo.setAdapter(arrayAdapter);

        spinnerPeriodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0:
                        record = "Manhã";
                        break;

                    case 1:
                        record = "Tarde";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSaveCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodo = record.trim();
                escola  = (int) spinnerEscola.getSelectedItemPosition()+1;
                tio  = (int) spinnerTios.getSelectedItemPosition()+1;

                registroPadrao();

            }
        });

        spinnerPeriodo.setPrompt("Selecione um Período:");
        spinnerTios.setPrompt("Selecione um Tio");
        spinnerEscola.setPrompt("Selecionar uma Escola");

        //Spinner Setting
        queue = Volley.newRequestQueue(this);
        requestJsonObjectTios();
        requestJsonObjectEscolas();

    }// onCreate

    private void registroPadrao() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_FILHO_FULL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progess.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //boolean success = jsonObject.getBoolean("success");
                            String success = jsonObject.getString("success");
                            //JSONArray success = jsonObject.getJSONArray("success");

                            if (success.equals("OK")){
                                snackbar = showSnackbar(constraintLayoutEdi, Snackbar.LENGTH_LONG, EditarFilhoActivity.this);
                                snackbar.show();
                                View view = snackbar.getView();
                                TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                tv.setText(jsonObject.getString("message"));
                            }else {
                                snackbar = showSnackbar(constraintLayoutEdi, Snackbar.LENGTH_LONG, EditarFilhoActivity.this);
                                snackbar.show();
                                View view = snackbar.getView();
                                TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                tv.setText(jsonObject.getString("message"));                            }

                        } catch (JSONException e1) {
                            Log.e("JSON", "Error parsing JSON", e1);

                        }
                        Log.e("Chamada", "response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSON", "Error parsing JSON", error);

                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("periodo", periodo);
                params.put("idTios", String.valueOf(tio));
                params.put("idEscola", String.valueOf(escola));
                params.put("idKids", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditarFilhoActivity.this);
        requestQueue.add(stringRequest);
    }

    private void requestJsonObjectTios() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PATH_TO_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spinnerTioData = Arrays.asList(mGson.fromJson(response, Tio[].class));
                //display first question to the user
                if(null != spinnerTioData){
                    //spinnerEscola = findViewById(R.id.spinnerEscola);
                    assert spinnerTioData != null;
                    //spinnerTios.setVisibility(View.VISIBLE);
                    SpinnerTiosAdapter spinnerAdapter = new SpinnerTiosAdapter(EditarFilhoActivity.this, spinnerTioData);
                    spinnerTios.setAdapter(spinnerAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    private void requestJsonObjectEscolas() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PATH_TO_SERVER2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spinnerEscolaData = Arrays.asList(mGson.fromJson(response, Escolas[].class));
                //display first question to the user
                if(null != spinnerEscolaData){
                    //spinnerEscola = findViewById(R.id.spinnerEscola);
                    assert spinnerEscolaData != null;
                    //spinnerTios.setVisibility(View.VISIBLE);
                    SpinnerEscolaAdapter spinnerAdapter = new SpinnerEscolaAdapter(EditarFilhoActivity.this, spinnerEscolaData);
                    spinnerEscola.setAdapter(spinnerAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
}//class
