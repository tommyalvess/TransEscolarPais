package br.com.apptransescolar.Activies;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static br.com.apptransescolar.API.URLs.URL_EDIT;
import static br.com.apptransescolar.API.URLs.URL_EDIT_SENHA;
import static br.com.apptransescolar.API.URLs.URL_READ;
import static br.com.apptransescolar.API.URLs.URL_UPLOAD;

public class PerfilActivity extends AppCompatActivity {

    private static final String TAG = PerfilActivity.class.getSimpleName();
    TextView textNomeU, textEmailU, textCpfU, textTellU;
    EditText editSenha, editSenhaConfirme;
    CircleImageView imgPerfilT;
    Button btnSalvar;
    //private static String URL_READ = "http://apptransescolar.com.br/apiapptransescolar/read_pais.php?apicall=findAll";
    //private static String URL_UPLOAD = "http://apptransescolar.com.br/apiapptransescolar/pai/uploadpais.php";
    String getId;
    String getCpf;
    private Bitmap bitmap;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        textNomeU =  findViewById(R.id.textNomeU);
        textEmailU =  findViewById(R.id.textEmailU);
        textCpfU =  findViewById(R.id.textCpfU);
        textTellU =  findViewById(R.id.textTellU);
        imgPerfilT = findViewById(R.id.imgPerfilT);


        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        getCpf = user.get(sessionManager.CPF);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogExit();
            }
        });

        imgPerfilT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosseFile();
            }
        });

    }

    //Pegar inf da sessão
    private void getUserDetailSessão(){
        HashMap<String, String> user = sessionManager.getUserDetail();
        String nNome = user.get(sessionManager.NAME);
        String nEmail = user.get(sessionManager.EMAIL);
        String nCpf = user.get(sessionManager.CPF);
        String nTell = user.get(sessionManager.TELL);
        String nIMG = user.get(sessionManager.IMG);

        getSupportActionBar().setTitle(nNome);
        textNomeU.setText(nNome);
        textEmailU.setText(nEmail);
        textCpfU.setText(nCpf);
        textTellU.setText(nTell);
        Picasso.get().load(nIMG).into(imgPerfilT);

    }

    //Pegar as infs do BD
    private void getUserDetail(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
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
                                    String strImage = object.getString("img").trim();

                                    textNomeU.setText(nome);
                                    textEmailU.setText(email);
                                    textCpfU.setText(cpf);
                                    textTellU.setText(tell);
                                    Picasso.get().load(strImage).into(imgPerfilT);
                                    getSupportActionBar().setTitle(nome);

                                }
                            }else {
                                Toast.makeText(PerfilActivity.this,json.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }catch ( JSONException e ) {
                            Log.e("JSON", "Error parsing JSON", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PerfilActivity.this, "Opss!!! Sem Conexão a internet.", Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", "Error", error);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("idPais", getId);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (verificaConexao() == true){
            getUserDetail();
        }else {
            getUserDetailSessão();
            Toast.makeText(this, "Você está sem internet!", Toast.LENGTH_SHORT).show();
        }

    }

    private void chosseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione a imagem"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgPerfilT.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Erro", "Upload", e);
            }

            UploadPicture(getId, getCpf, getStringImage(bitmap));

        }
    }

    //Fazer o Upload da foto
    private void UploadPicture(final String id, final String cpf, final String photo) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Mensagem Upload", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(PerfilActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Erro", "Upload", e);
                            Toast.makeText(PerfilActivity.this,"Opss! Tente Novamente!",Toast.LENGTH_LONG).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PerfilActivity.this,"Opss! Algo deu errado!",Toast.LENGTH_LONG).show();


                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idPais", id);
                params.put("cpf", cpf);
                params.put("img", photo);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }


    //Dialo para sair da tele
    private void dialogExit(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
        builder.setMessage("VOCÊ DESEJA SAIR?")
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sessionManager.logout();
                        finish();
                        Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil, menu);
        return true;
    }

    //Editar Perfil
    public void EditarNome(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String nNome = user.get(sessionManager.NAME);
        String nId = user.get(sessionManager.ID);
        int id = Integer.parseInt(nId);

        alertDialog.setTitle("Editar Nome");

        final EditText input = new EditText(PerfilActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(nNome);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, String> user = sessionManager.getUserDetail();

                        final String id = getId;
                        final String nome = input.getText().toString().trim();
                        final String email = user.get(sessionManager.EMAIL);
                        final String cpf = user.get(sessionManager.CPF);
                        final String tell = user.get(sessionManager.TELL);
                        final String img = user.get(sessionManager.IMG);
                        //final String senha = user.get(sessionManager.SENHA);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //progess.setVisibility(View.GONE);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            //boolean success = jsonObject.getBoolean("success");
                                            String success = jsonObject.getString("success");

                                            if (success.equals("1")){
                                                sessionManager.createSession(id, nome, email, cpf, tell, img);
                                                Toast.makeText(PerfilActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(PerfilActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
                                }){

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("idPais", getId);
                                params.put("nm_pai", nome);
                                params.put("email", email);
                                params.put("cpf", getCpf);
                                params.put("tell", tell);
                                params.put("img", img);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(PerfilActivity.this);
                        requestQueue.add(stringRequest);
                    }
                });
        alertDialog.setNegativeButton("Não", null);
        alertDialog.show();
    }

    public void EditarEmail(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String nNome = user.get(sessionManager.EMAIL);
        String nId = user.get(sessionManager.ID);
        int id = Integer.parseInt(nId);

        alertDialog.setTitle("Editar Email");

        final EditText input = new EditText(PerfilActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(nNome);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, String> user = sessionManager.getUserDetail();

                        final String id = getId;
                        final String nome = user.get(sessionManager.NAME);
                        final String email = input.getText().toString().trim();
                        final String cpf = user.get(sessionManager.CPF);
                        final String tell = user.get(sessionManager.TELL);
                        final String img = user.get(sessionManager.IMG);
                        //final String senha = user.get(sessionManager.SENHA);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //progess.setVisibility(View.GONE);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            //boolean success = jsonObject.getBoolean("success");
                                            String success = jsonObject.getString("success");

                                            if (success.equals("1")){
                                                sessionManager.createSession(id, nome, email, cpf, tell, img);
                                                Toast.makeText(PerfilActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(PerfilActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
                                }){

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("idPais", getId);
                                params.put("nm_pai", nome);
                                params.put("email", email);
                                params.put("cpf", getCpf);
                                params.put("tell", tell);
                                params.put("img", img);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(PerfilActivity.this);
                        requestQueue.add(stringRequest);
                    }
                });
        alertDialog.setNegativeButton("Não", null);
        alertDialog.show();
    }

    public void EditarCpf(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String nNome = user.get(sessionManager.CPF);
        String nId = user.get(sessionManager.ID);
        int id = Integer.parseInt(nId);

        alertDialog.setTitle("Editar CPF");

        final EditText input = new EditText(PerfilActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(nNome);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, String> user = sessionManager.getUserDetail();

                        final String id = getId;
                        final String nome = user.get(sessionManager.NAME);
                        final String email = user.get(sessionManager.EMAIL);
                        final String cpf = input.getText().toString().trim();
                        final String tell = user.get(sessionManager.TELL);
                        final String img = user.get(sessionManager.IMG);
                        //final String senha = user.get(sessionManager.SENHA);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //progess.setVisibility(View.GONE);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            //boolean success = jsonObject.getBoolean("success");
                                            String success = jsonObject.getString("success");

                                            if (success.equals("1")){
                                                sessionManager.createSession(id, nome, email, cpf, tell, img);
                                                Toast.makeText(PerfilActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(PerfilActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
                                }){

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("idPais", getId);
                                params.put("nm_pai", nome);
                                params.put("email", email);
                                params.put("cpf", getCpf);
                                params.put("tell", tell);
                                params.put("img", img);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(PerfilActivity.this);
                        requestQueue.add(stringRequest);
                    }
                });
        alertDialog.setNegativeButton("Não", null);
        alertDialog.show();
    }

    public void EditarTell(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String nNome = user.get(sessionManager.TELL);
        String nId = user.get(sessionManager.ID);
        int id = Integer.parseInt(nId);

        alertDialog.setTitle("Editar Telefone");

        final EditText input = new EditText(PerfilActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(nNome);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, String> user = sessionManager.getUserDetail();

                        final String id = getId;
                        final String nome = user.get(sessionManager.NAME);
                        final String email = user.get(sessionManager.EMAIL);
                        final String cpf = user.get(sessionManager.CPF);
                        final String tell = input.getText().toString().trim();
                        final String img = user.get(sessionManager.IMG);
                        //final String senha = user.get(sessionManager.SENHA);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //progess.setVisibility(View.GONE);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            //boolean success = jsonObject.getBoolean("success");
                                            String success = jsonObject.getString("success");

                                            if (success.equals("1")){
                                                sessionManager.createSession(id, nome, email, cpf, tell, img);
                                                Toast.makeText(PerfilActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(PerfilActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
                                }){

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("idPais", getId);
                                params.put("nm_pai", nome);
                                params.put("email", email);
                                params.put("cpf", getCpf);
                                params.put("tell", tell);
                                params.put("img", img);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(PerfilActivity.this);
                        requestQueue.add(stringRequest);
                    }
                });
        alertDialog.setNegativeButton("Não", null);
        alertDialog.show();
    }

    public void alterarSenha(MenuItem item) {
        Intent intent = new Intent(PerfilActivity.this, AlterarSenhaActivity.class);
        startActivity(intent);
    }

}
