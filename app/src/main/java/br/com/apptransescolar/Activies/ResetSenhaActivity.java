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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;

import static br.com.apptransescolar.API.URLs.URL_EDIT_SENHA;

public class ResetSenhaActivity extends AppCompatActivity {

    private static final String TAG = PerfilActivity.class.getSimpleName();

    EditText editSenha, editSenhaConfirme;
    Button btnSalvar;

    String getId;
    String getCpf;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sessionManager = new SessionManager(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Alterar Senha");

        btnSalvar =  findViewById(R.id.btnSalvar);
        editSenha = findViewById(R.id.editText3);
        editSenhaConfirme = findViewById(R.id.editText4);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        getCpf = user.get(sessionManager.CPF);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String senhaNovo = editSenha.getText().toString().trim();
                String senhaConfirme = editSenhaConfirme.getText().toString().trim();

                if (senhaNovo.equals(senhaConfirme)){
                    if (senhaNovo.length() > 6) {

                        final String senha = editSenha.getText().toString().trim();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_SENHA,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //progess.setVisibility(View.GONE);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            //boolean success = jsonObject.getBoolean("success");
                                            String success = jsonObject.getString("success");

                                            if (success.equals("1")) {
                                                Toast.makeText(ResetSenhaActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                                editSenha.setText("");
                                                editSenhaConfirme.setText("");
                                                Intent intent = new Intent(ResetSenhaActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(ResetSenhaActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                            }

                                        } catch (JSONException e1) {
                                            Log.e("JSON", "Error parsing JSON", e1);
                                        }
                                        Log.e(TAG, "response: " + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("JSON", "Error parsing JSON", error);

                                    }
                                }) {

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("idPais", getId);
                                params.put("senha", senha);
                                params.put("cpf", getCpf);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(ResetSenhaActivity.this);
                        requestQueue.add(stringRequest);

                    }else {
                        Toast.makeText(ResetSenhaActivity.this, "Senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ResetSenhaActivity.this, "As senhas não são iguais!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }//on create
}//class

