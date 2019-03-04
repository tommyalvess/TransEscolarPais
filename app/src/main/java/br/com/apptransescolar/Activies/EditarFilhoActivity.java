package br.com.apptransescolar.Activies;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class EditarFilhoActivity extends AppCompatActivity {

    Spinner spinnerPeriodo, spinnerTios, spinnerEscola;
    EditText editNomeT,dtNasc,endT;
    Button btnSaveCadastro;

    String id;
    String getId;
    String nome;
    String dtNas;
    String end;
    int tio;
    int escola;
    String periodo;

    //Spinner
    List<Tio> spinnerTioData;
    List<Escolas> spinnerEscolaData;
    String name [] = {"Manhã", "Tarde"};
    String record = "";

    ArrayAdapter<String> arrayAdapter;

    private RequestQueue queue;

    SessionManager sessionManager;

    Kids kids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_filho);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        kids = (Kids) getIntent().getExtras().get("kids");

        //Dados do pai
        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Alterar Cadastro");

        spinnerPeriodo = findViewById(R.id.spinnerP);
        spinnerTios = findViewById(R.id.spinnerT);
        spinnerEscola = findViewById(R.id.spinnerE);

        editNomeT = findViewById(R.id.editNomeT);
        dtNasc = findViewById(R.id.dtNasc);
        endT = findViewById(R.id.end);
        btnSaveCadastro = findViewById(R.id.btnSaveCadastro);

        editNomeT.setText(kids.getNome());
        endT.setText(kids.getEnd_principal());
        dtNasc.setText(kids.getDt_nas());


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
                nome  = editNomeT.getText().toString().trim();
                dtNas  = dtNasc.getText().toString().trim();
                end = endT.getText().toString().trim();
                periodo = record.trim();
                escola  = (int) spinnerEscola.getSelectedItemPosition()+1;
                tio  = (int) spinnerTios.getSelectedItemPosition()+1;

                if (nome.isEmpty()){
                    editNomeT.setError("Insira o seu nome");
                    editNomeT.requestFocus();
                    return;
                }

                if (dtNas.isEmpty()){
                    dtNasc.setError("Insira seu aniversario!");
                    dtNasc.requestFocus();
                    return;
                }

                if (end.isEmpty()){
                    endT.setError("Insira seu endereço!");
                    endT.requestFocus();
                    return;
                }

                registroPadrao();

            }
        });

        spinnerPeriodo.setPrompt("Selecione um Periodo:");
        spinnerTios.setPrompt("Selecione um Tio");
        spinnerEscola.setPrompt("Selecionar uma Escola");

        //Spinner Setting
        queue = Volley.newRequestQueue(this);
        requestJsonObjectTios();
        requestJsonObjectEscolas();

    }// onCreate

    private void registroPadrao() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_FILHO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progess.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //boolean success = jsonObject.getBoolean("success");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(EditarFilhoActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(EditarFilhoActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }

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
                params.put("nome", nome);
                params.put("dt_nas", dtNas);
                params.put("end_principal", end);
                params.put("periodo", periodo);
                params.put("idTios", String.valueOf(tio));
                params.put("idEscola", String.valueOf(escola));
                params.put("idPais", getId);
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
}//class
