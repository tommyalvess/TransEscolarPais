package br.com.apptransescolar.Activies;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;

import static br.com.apptransescolar.API.URLs.URL_READ;
import static br.com.apptransescolar.API.URLs.URL_READ_FORGOT;

public class RecuperarSenhaActivity extends AppCompatActivity {

    EditText editTextCPF;
    Button btnOK;

    SessionManager sessionManager;

    String cpf1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sessionManager = new SessionManager(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Esqueci a Senha");

        editTextCPF = findViewById(R.id.editTextCPF);
        btnOK = findViewById(R.id.btnOK);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpf1 = editTextCPF.getText().toString();
                if(cpf1.isEmpty()){
                    Toast.makeText(RecuperarSenhaActivity.this, "Digite o CPF!", Toast.LENGTH_SHORT).show();
                }else {
                    getUserDetail();
                }
            }
        });

    }//Ocreate

    //Pegar as infs do BD
    private void getUserDetail(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ_FORGOT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Mensagem GetUserDetail", response.toString());
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray nameArray = json.names();
                            JSONArray valArray = json.toJSONArray( nameArray );
                            if (!json.equals("0")){
                                for (int i = 0; i < valArray.length(); i++) {
                                    JSONObject object = valArray.getJSONObject(i);
                                    String id = object.getString("idPais").trim();
                                    String nome = object.getString("nm_pai").trim();
                                    String email = object.getString("email").trim();
                                    String cpf = object.getString("cpf").trim();
                                    String tell = object.getString("tell").trim();
                                    String img = object.getString("img").trim();

                                    sessionManager.createSession(id, nome, email, cpf, tell, img);

                                    Intent intent = new Intent(RecuperarSenhaActivity.this, ResetSenhaActivity.class);
                                    intent.putExtra("idPais", id);
                                    intent.putExtra("nm_pai", nome);
                                    intent.putExtra("email", email);
                                    intent.putExtra("cpf", cpf);
                                    intent.putExtra("tell", tell);
                                    startActivity(intent);

                                    Toast.makeText(RecuperarSenhaActivity.this, "Localizado!!!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(RecuperarSenhaActivity.this,json.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }catch ( JSONException e ) {
                            Toast.makeText(RecuperarSenhaActivity.this, "Opss!!! Não localizado!", Toast.LENGTH_SHORT).show();
                            Log.e("JSON", "Error parsing JSON", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RecuperarSenhaActivity.this, "Opss!!! Algo deu errado!", Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", "Error", error);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("cpf", cpf1);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}//Class
