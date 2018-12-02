package br.com.apptransescolar.Activies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.apptransescolar.API.URLs;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;

import static br.com.apptransescolar.API.URLs.URL_LOGIN;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    Button btnLogin, btncadastro;
    EditText editCpf, editSenha;

    ProgressBar loginProgress;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        editCpf = findViewById(R.id.login_main);
        editSenha = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.btn_login);
        btncadastro = findViewById(R.id.btn_cadastro);
        loginProgress = findViewById(R.id.login_progress);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cpf = editCpf.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();

                //validating inputs
                if (TextUtils.isEmpty(cpf) ) {
                    editCpf.setError("Insira seu CPF!");
                    editCpf.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(senha)) {
                    editSenha.setError("Insira sua senha!");
                    editSenha.requestFocus();
                    return;
                }

                if (verificaConexao() == false) {
                    Toast.makeText(LoginActivity.this, "Sem Conexão Com a Internet!!", Toast.LENGTH_SHORT).show();
                }

                login(cpf,senha);
            }
        });

        btncadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

    }// onCreate

    private void login(final String cpf, final String senha) {

        loginProgress.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray nameArray = json.names();
                            JSONArray valArray = json.toJSONArray( nameArray );
                            String success = json.getString("error");


                            if (success.equals("1")){

                                for ( int i = 0; i < valArray.length(); i++) {
                                    JSONObject object = valArray.getJSONObject(i);
                                    String id = object.getString("idPais").trim();
                                    String nome = object.getString("nome").trim();
                                    String email = object.getString("email").trim();
                                    String cpf = object.getString("cpf").trim();
                                    String tell = object.getString("tell").trim();
                                    String img = object.getString("img").trim();
                                    String idTio = object.getString("idTios").trim();

                                    sessionManager.createSession(id, nome, email, cpf, tell, img, idTio);

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("idPais", id);
                                    intent.putExtra("nome", nome);
                                    intent.putExtra("email", email);
                                    intent.putExtra("cpf", cpf);
                                    intent.putExtra("tell", tell);
                                    intent.putExtra("img", tell);
                                    intent.putExtra("idTios", idTio);
                                    startActivity(intent);

                                    loginProgress.setVisibility(View.GONE);
                                    btnLogin.setVisibility(View.VISIBLE);
                                    Toast.makeText(LoginActivity.this,json.getString("message"),Toast.LENGTH_LONG).show();
                                    finish();

                                }
                            }else if(success.equals("0")) {
                                Toast.makeText(LoginActivity.this,json.getString("message"),Toast.LENGTH_LONG).show();
                                loginProgress.setVisibility(View.GONE);
                                btnLogin.setVisibility(View.VISIBLE);

                            }
                        }catch ( JSONException e ) {
                            loginProgress.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            Log.e("JSON", "Error parsing JSON", e);
                        }

                        Log.e(TAG, "response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginProgress.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                        //Toast.makeText(LoginActivity.this, "Opss!! Sem Conexão a internet", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "response: " + error);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("cpf", cpf);
                param.put("senha", senha);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }// verificaConexao

}
