package br.com.apptransescolar.Activies;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
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
    static Snackbar snackbar;
    ConstraintLayout constraintLayoutRec;


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
        constraintLayoutRec = findViewById(R.id.constraintLayoutRec);

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
                            if (!json.equals("falhou")){
                                for (int i = 0; i < valArray.length(); i++) {
                                    JSONObject object = valArray.getJSONObject(i);
                                    String id = object.getString("idPais").trim();
                                    String nome = object.getString("nm_pai").trim();
                                    String email = object.getString("email").trim();
                                    String cpf = object.getString("cpf").trim();
                                    String tell = object.getString("tell").trim();
                                    String img = object.getString("img").trim();

                                    sessionManager.createSessionSenha(id, nome, email, cpf, tell, img);

                                    Intent intent = new Intent(RecuperarSenhaActivity.this, ResetSenhaActivity.class);
                                    intent.putExtra("idPais", id);
                                    intent.putExtra("nm_pai", nome);
                                    intent.putExtra("email", email);
                                    intent.putExtra("cpf", cpf);
                                    intent.putExtra("tell", tell);
                                    startActivity(intent);

                                }
                            }else {
                                snackbar = showSnackbar(constraintLayoutRec, Snackbar.LENGTH_LONG, RecuperarSenhaActivity.this);
                                snackbar.show();
                                View view = snackbar.getView();
                                TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                tv.setText(json.getString("message"));
                            }
                        }catch ( JSONException e ) {
                            Log.e("JSON", "Error parsing JSON", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
}//Class
