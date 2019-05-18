package br.com.apptransescolar.Activies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.apptransescolar.Conexao.NetworkChangeReceiver5;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static br.com.apptransescolar.API.URLs.URL_COUNTPAIS;
import static br.com.apptransescolar.API.URLs.URL_COUNTTIOS;
import static br.com.apptransescolar.API.URLs.URL_EDIT;
import static br.com.apptransescolar.API.URLs.URL_READ;
import static br.com.apptransescolar.API.URLs.URL_UPLOAD;

public class PerfilActivity extends AppCompatActivity {

    private static final String TAG = PerfilActivity.class.getSimpleName();
    TextView txtNomeU, txtNome, txtEmailU, txtCpfU, txtTellU, txtCountKids, txtCountTios;
    EditText periodo, editSenha, editSenhaConfirme;
    ImageView imgSetting;
    CircleImageView imgPerfilP;
    Button btnSalvar;
    String getId;
    String getCpf;
    private Bitmap bitmap;

    String nNome,nEmail,nCpf, nTell, nIMG;

    SessionManager sessionManager;

    static NestedScrollView constraintLayout;

    RequestOptions cropOptions;

    private final Handler handler = new Handler();
    static Thread thread;
    static TextView tv_check_connection;
    static ActionBar bar;
    static Window window;
    static Snackbar snackbar;
    private NetworkChangeReceiver5 mNetworkReceiver;
    static NestedScrollView perfilLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mNetworkReceiver = new NetworkChangeReceiver5();
        registerNetworkBroadcastForNougat();

        IntentFilter inF1 = new IntentFilter("data_changed");
        LocalBroadcastManager.getInstance(PerfilActivity.this).registerReceiver(dataChangeReceiver1,inF1);

        window = PerfilActivity.this.getWindow();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("");

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        constraintLayout = findViewById(R.id.perfilLay);
        txtNomeU =  findViewById(R.id.tv_name);
        txtNome = findViewById(R.id.txtNome);
        txtEmailU =  findViewById(R.id.txtEmailP);
        txtCpfU =  findViewById(R.id.txtCPFP);
        txtTellU =  findViewById(R.id.txtTellP);
        imgPerfilP = findViewById(R.id.imgPerfilP);
        periodo =  findViewById(R.id.periodoD);
        txtCountKids = findViewById(R.id.txtCountKids);
        txtCountTios = findViewById(R.id.txtCountTios);
        perfilLay = findViewById(R.id.perfilLay);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        getCpf = user.get(sessionManager.CPF);
        nNome = user.get(sessionManager.NAME);
        nEmail = user.get(sessionManager.EMAIL);
        nCpf = user.get(sessionManager.CPF);
        nTell = user.get(sessionManager.TELL);
        nIMG = user.get(sessionManager.IMG);

        cropOptions = new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).error(R.drawable.kids);

        imgPerfilP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosseFile();
            }
        });


        if (verificaConexao() == true){
            getUserDetail();
            countKids();
            countTios();
        }else {
            getUserDetailSessão();
            txtCountKids.setText("0");
            txtCountTios.setText("0");
        }

    }

    //Refresh
    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                verificaConexao();
                doTheAutoRefresh();
            }
        }, 100);
    }

    //Contar pais
    private void countTios(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COUNTTIOS,
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
                                    String nome = object.getString("nome").trim();

                                    txtCountTios.setText(nome);
                                }
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
                param.put("idPais", getId);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Contar tios
    private void countKids(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COUNTPAIS,
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
                                    String nome = object.getString("nome").trim();

                                    txtCountKids.setText(nome);
                                }
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
                param.put("idPais", getId);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Pegar inf da sessão
    private void getUserDetailSessão(){
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        getCpf = user.get(sessionManager.CPF);
        nNome = user.get(sessionManager.NAME);
        nEmail = user.get(sessionManager.EMAIL);
        nCpf = user.get(sessionManager.CPF);
        nTell = user.get(sessionManager.TELL);
        nIMG = user.get(sessionManager.IMG);

        txtNomeU.setText(nNome);
        txtNome.setText(nNome);
        txtEmailU.setText(nEmail);
        txtCpfU.setText(nCpf);
        txtTellU.setText(nTell);
        Glide.with(this).load(nIMG).apply(cropOptions).into(imgPerfilP);
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

                                    txtNomeU.setText(nome);
                                    txtNome.setText(nome);
                                    txtEmailU.setText(email);
                                    txtCpfU.setText(cpf);
                                    txtTellU.setText(tell);

                                    Glide.with(PerfilActivity.this).load(strImage).apply(cropOptions).into(imgPerfilP);
                                }
                            }else {
                                snackbar = showSnackbar(perfilLay, Snackbar.LENGTH_LONG, PerfilActivity.this);
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
                param.put("idPais", getId);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (verificaConexao() == true){
            getUserDetail();
            countKids();
            countTios();
        }else {
            getUserDetailSessão();
            txtCountKids.setText("0");
            txtCountTios.setText("0");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (verificaConexao() == true){
            getUserDetail();
            countKids();
            countTios();
        }else {
            getUserDetailSessão();
            txtCountKids.setText("0");
            txtCountTios.setText("0");
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
                imgPerfilP.setImageBitmap(bitmap);
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

                            if (success.equals("OK")){
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Erro", "Upload", e);
                            snackbar = showSnackbar(perfilLay, Snackbar.LENGTH_LONG, PerfilActivity.this);
                            snackbar.show();
                            View view = snackbar.getView();
                            TextView tv = (TextView) view.findViewById(R.id.textSnack);
                            tv.setText("Opss! Tente Novamente!");

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    //Editar Perfil
    public void EditarNome(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        final String nNome = user.get(sessionManager.NAME);
        String nId = user.get(sessionManager.ID);
        int id = Integer.parseInt(nId);

        View mView = getLayoutInflater().inflate(R.layout.dialog_nome, null);
        final EditText nomeE = (EditText) mView.findViewById(R.id.nomeD);
        Button mSim = (Button) mView.findViewById(R.id.btnSim);
        Button mNao = (Button) mView.findViewById(R.id.btnNao);

        alertDialog.setTitle("Editar Nome");

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        nomeE.setText(nNome);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> user = sessionManager.getUserDetail();

                final String id = getId;
                final String nome = nomeE.getText().toString().trim();
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

                                    if (success.equals("OK")){
                                        sessionManager.createSession(id, nome, email, cpf, tell, img);
                                        LocalBroadcastManager.getInstance(PerfilActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(perfilLay, Snackbar.LENGTH_LONG, PerfilActivity.this);
                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                        tv.setText(jsonObject.getString("message"));

                                        dialog.dismiss();
                                    }

                                } catch (JSONException e1) {
                                    Log.e("JSON", "Error parsing JSON", e1);
                                    dialog.dismiss();
                                }
                                Log.e(TAG, "response: " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("JSON", "Error parsing JSON", error);
                                dialog.dismiss();
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
        dialog.show();
    }

    public void EditarEmail(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String nNome = user.get(sessionManager.EMAIL);
        String nId = user.get(sessionManager.ID);
        int id = Integer.parseInt(nId);

        alertDialog.setTitle("Editar Email");

        View mView = getLayoutInflater().inflate(R.layout.dialog_email, null);
        final EditText emailE = (EditText) mView.findViewById(R.id.emailD);
        Button mSim = (Button) mView.findViewById(R.id.btnSim);
        Button mNao = (Button) mView.findViewById(R.id.btnNao);

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        emailE.setText(nNome);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> user = sessionManager.getUserDetail();

                final String id = getId;
                final String nome = user.get(sessionManager.NAME);
                final String email = emailE.getText().toString().trim();
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

                                    if (success.equals("OK")){
                                        sessionManager.createSession(id, nome, email, cpf, tell, img);
                                        LocalBroadcastManager.getInstance(PerfilActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(perfilLay, Snackbar.LENGTH_LONG, PerfilActivity.this);
                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                        tv.setText(jsonObject.getString("message"));
                                        dialog.dismiss();
                                    }

                                } catch (JSONException e1) {
                                    Log.e("JSON", "Error parsing JSON", e1);
                                    dialog.dismiss();
                                }
                                Log.e(TAG, "response: " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("JSON", "Error parsing JSON", error);
                                dialog.dismiss();
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
        dialog.show();
    }

    public void EditarCpf(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String nNome = user.get(sessionManager.CPF);
        String nId = user.get(sessionManager.ID);
        int id = Integer.parseInt(nId);

        alertDialog.setTitle("Editar CPF");

        View mView = getLayoutInflater().inflate(R.layout.dialog_cpf, null);
        final EditText cpfE = (EditText) mView.findViewById(R.id.cpfD);
        Button mSim = (Button) mView.findViewById(R.id.btnSim);
        Button mNao = (Button) mView.findViewById(R.id.btnNao);

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        cpfE.setText(nNome);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> user = sessionManager.getUserDetail();

                final String id = getId;
                final String nome = user.get(sessionManager.NAME);
                final String email = user.get(sessionManager.EMAIL);
                final String cpf = cpfE.getText().toString();
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

                                    if (success.equals("OK")){
                                        sessionManager.createSession(id, nome, email, cpf, tell, img);
                                        LocalBroadcastManager.getInstance(PerfilActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(perfilLay, Snackbar.LENGTH_LONG, PerfilActivity.this);
                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                        tv.setText(jsonObject.getString("message"));

                                        dialog.dismiss();
                                    }

                                } catch (JSONException e1) {
                                    Log.e("JSON", "Error parsing JSON", e1);
                                    dialog.dismiss();
                                }
                                Log.e(TAG, "response: " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("JSON", "Error parsing JSON", error);
                                dialog.dismiss();
                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("idPais", getId);
                        params.put("nm_pai", nome);
                        params.put("email", email);
                        params.put("cpf", cpf);
                        params.put("tell", tell);
                        params.put("img", img);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(PerfilActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        dialog.show();
    }

    public void EditarTell(View view) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String nNome = user.get(sessionManager.TELL);
        String nId = user.get(sessionManager.ID);
        int id = Integer.parseInt(nId);

        View mView = getLayoutInflater().inflate(R.layout.dialog_edit_tell, null);
        final EditText mTell = (EditText) mView.findViewById(R.id.periodoD);
        Button mSim = (Button) mView.findViewById(R.id.btnSim);
        Button mNao = (Button) mView.findViewById(R.id.btnNao);

        alertDialog.setTitle("Editar Telefone");

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        mTell.setText(nNome);

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> user = sessionManager.getUserDetail();

                final String id = getId;
                final String nome = user.get(sessionManager.NAME);
                final String email = user.get(sessionManager.EMAIL);
                final String cpf = user.get(sessionManager.CPF);
                final String tell = mTell.getText().toString().trim();
                final String img = user.get(sessionManager.IMG);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //boolean success = jsonObject.getBoolean("success");
                                    String success = jsonObject.getString("success");

                                    if (success.equals("OK")){
                                        sessionManager.createSession(id, nome, email, cpf, tell, img);
                                        LocalBroadcastManager.getInstance(PerfilActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(perfilLay, Snackbar.LENGTH_LONG, PerfilActivity.this);
                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                        tv.setText(jsonObject.getString("message"));
                                        dialog.dismiss();

                                    }

                                } catch (JSONException e1) {
                                    Log.e("JSON", "Error parsing JSON", e1);
                                    dialog.dismiss();

                                }
                                Log.e(TAG, "response: " + response);
                                dialog.dismiss();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("JSON", "Error parsing JSON", error);
                                dialog.dismiss();

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

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dialog.dismiss();
            }
        });
        dialog.show();
        //alertDialog.show();
    }

    public void alterarSenha(MenuItem item) {
        Intent intent = new Intent(PerfilActivity.this, AlterarSenhaActivity.class);
        startActivity(intent);
    }
    
    public static void dialogP(boolean value, final Context context){

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
            snackbar = showSnackbar(perfilLay, Snackbar.LENGTH_INDEFINITE, context);
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
            getUserDetail();
        }
    };

    private static Snackbar showSnackbar(NestedScrollView coordinatorLayout, int duration, Context context) {
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
