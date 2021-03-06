package br.com.apptransescolar.Activies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import br.com.apptransescolar.Classes.Kids;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static br.com.apptransescolar.API.URLs.URL_DELETE_KIDS;
import static br.com.apptransescolar.API.URLs.URL_EDIT;
import static br.com.apptransescolar.API.URLs.URL_EDIT_FILHO;
import static br.com.apptransescolar.API.URLs.URL_EDIT_READ;
import static br.com.apptransescolar.API.URLs.URL_READ;
import static br.com.apptransescolar.API.URLs.URL_UPLOAD;
import static br.com.apptransescolar.API.URLs.URL_UPLOAD_KIDS;


public class InfFilhosActivity extends AppCompatActivity {

    TextView nomeK, escolaK, endK, dt_nasc, txtApelido, txtTios, txtStatus, txtPeriodo,txtEmbarque, txtDesembarque;
    String id, getNome, getEnd, getEscola, getDtnas, getTios, getStatus, getPeriodo, getEmbarque, getDesembarque;
    CircleImageView imgInfKids;
    private static final String TAG = PerfilActivity.class.getSimpleName();
    private Bitmap bitmap;
    Kids kids;

    String idKids, userID;

    RequestOptions cropOptions;

    static Snackbar snackbar;
    static ConstraintLayout coordinatorLayoutinfF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_filhos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        kids = (Kids) getIntent().getExtras().get("kids");
        nomeK = findViewById(R.id.txtNomeK);
        txtApelido = findViewById(R.id.txtApelidoK);
        escolaK = findViewById(R.id.txtEscolaK);
        endK = findViewById(R.id.txtEnderecoK);
        dt_nasc = findViewById(R.id.txtData);
        imgInfKids = findViewById(R.id.imgPerfilK);
        txtEmbarque = findViewById(R.id.txtEmbarque);
        txtDesembarque = findViewById(R.id.txtDesembarque);
        txtTios = findViewById(R.id.txtTios);
        txtStatus = findViewById(R.id.txtStatus);
        txtPeriodo = findViewById(R.id.txtPeriodo);
        coordinatorLayoutinfF = findViewById(R.id.coordinatorLayoutinfF);

        getUserDetail();

//        txtApelido.setText(kids.getNome());
//        nomeK.setText(kids.getNome());
//        escolaK.setText(kids.getNm_escola());
//        txtPeriodo.setText(kids.getPeriodo());
//        endK.setText(kids.getEnd_principal());
//        dt_nasc.setText(kids.getDt_nas());
//        txtTios.setText(kids.getTio());
//        txtStatus.setText(kids.getStatus());
//        txtEmbarque.setText(kids.getEmbarque());
//        txtDesembarque.setText(kids.getDesembarque());

        //Obj kids
//        getNome = kids.getNome();
//        getEscola = kids.getNm_escola();
//        getPeriodo = kids.getPeriodo();
//        getEnd = kids.getEnd_principal();
//        getDtnas = kids.getDt_nas();
//        getTios = kids.getTio();
//        getEmbarque = kids.getEmbarque();
//        getDesembarque = kids.getDesembarque();

        idKids = String.valueOf(kids.getIdKids());

        getNome = nomeK.getText().toString().trim();
        getEscola = escolaK.getText().toString().trim();
        getPeriodo = txtPeriodo.getText().toString().trim();
        getEnd = endK.getText().toString().trim();
        getDtnas = dt_nasc.getText().toString().trim();
        getTios = txtTios.getText().toString().trim();
        getEmbarque = txtEmbarque.getText().toString().trim();
        getDesembarque = txtDesembarque.getText().toString().trim();
        getStatus = txtStatus.getText().toString().trim();

        cropOptions = new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).placeholder(R.drawable.kids).error(R.drawable.kids);
        Glide.with(this).load(kids.getImg()).apply(cropOptions).into(imgInfKids);

        imgInfKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosseFile();

            }
        });

        IntentFilter inF1 = new IntentFilter("data_changed");
        LocalBroadcastManager.getInstance(InfFilhosActivity.this).registerReceiver(dataChangeReceiver1,inF1);

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
                imgInfKids.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Erro", "Upload", e);
            }

            UploadPicture(idKids, getStringImage(bitmap));

        }
    }

    //Fazer o Upload da foto
    private void UploadPicture(final String idKids, final String photo) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_KIDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Mensagem Upload", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("OK")){

                            }else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Erro", "Upload", e);
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
                params.put("idKids", idKids);
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
        getMenuInflater().inflate(R.menu.menu_inf_filhos, menu);
        return true;
    }

    public void alterarKids(MenuItem item) {
        Intent intent = new Intent(InfFilhosActivity.this, EditarFilhoActivity.class);
        intent.putExtra("kids", kids);
        InfFilhosActivity.this.startActivity(intent);
    }

    private void deletarKidsDialog() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_KIDS,
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
                                Intent intent = new Intent(InfFilhosActivity.this, FilhosActivity.class);
                                InfFilhosActivity.this.startActivity(intent);
                            }else {
                                snackbar = showSnackbar(coordinatorLayoutinfF, Snackbar.LENGTH_LONG, InfFilhosActivity.this);
                                snackbar.show();
                                View view = snackbar.getView();
                                TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                tv.setText(jsonObject.getString("message"));
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
                        Log.e("JSON", "Error Response", error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idKids", idKids);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InfFilhosActivity.this);
        requestQueue.add(stringRequest);
    }

    public void deletarKids(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.dialog_text, null);
        final TextView nomeE = mView.findViewById(R.id.nomeD);
        Button mSim = mView.findViewById(R.id.btnSim);
        Button mNao = mView.findViewById(R.id.btnNao);

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        nomeE.setText("Você deseja realmente deletar?");
        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletarKidsDialog();
                dialog.dismiss();
                finish();
            }
        });
        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void EditarNomeK(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfFilhosActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_nome, null);
        final EditText nomeE = mView.findViewById(R.id.nomeD);
        Button mSim =  mView.findViewById(R.id.btnSim);
        Button mNao =  mView.findViewById(R.id.btnNao);

        nomeE.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        alertDialog.setTitle("Editar Nome");

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        nomeE.setText(getNome);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nome = nomeE.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_FILHO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //progess.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //boolean success = jsonObject.getBoolean("success");
                                    String success = jsonObject.getString("success");

                                    if (success.equals("OK")){
                                        LocalBroadcastManager.getInstance(InfFilhosActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(coordinatorLayoutinfF, Snackbar.LENGTH_LONG, InfFilhosActivity.this);
                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                        tv.setText(jsonObject.getString("message"));                                        dialog.dismiss();
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
                        params.put("nome", nome);
                        params.put("dt_nas", getDtnas);
                        params.put("end_principal", getEnd);
                        params.put("embarque", getEmbarque);
                        params.put("desembarque", getDesembarque);
                        params.put("idKids", idKids);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(InfFilhosActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        dialog.show();
    }

    public void EditarEscola(View view) {
//        Intent intent = new Intent(InfFilhosActivity.this, EditarFilhoActivity.class);
//        intent.putExtra("kids", kids);
//        InfFilhosActivity.this.startActivity(intent);
    }

    public void EditarEnd(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfFilhosActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_end, null);
        final EditText nomeE = mView.findViewById(R.id.nomeD);
        Button mSim =  mView.findViewById(R.id.btnSim);
        Button mNao =  mView.findViewById(R.id.btnNao);

        nomeE.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        alertDialog.setTitle("Editar Endereço");

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        nomeE.setText(getEnd);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String end = nomeE.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_FILHO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //progess.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //boolean success = jsonObject.getBoolean("success");
                                    String success = jsonObject.getString("success");

                                    if (success.equals("OK")){
                                        LocalBroadcastManager.getInstance(InfFilhosActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(coordinatorLayoutinfF, Snackbar.LENGTH_LONG, InfFilhosActivity.this);
                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                        tv.setText(jsonObject.getString("message"));                                        dialog.dismiss();
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
                        params.put("nome", getNome);
                        params.put("dt_nas", getDtnas);
                        params.put("end_principal", end);
                        params.put("embarque", getEmbarque);
                        params.put("desembarque", getDesembarque);
                        params.put("idKids", idKids);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(InfFilhosActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        dialog.show();
    }

    public void EditarBday(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfFilhosActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_bday, null);
        final EditText nomeE = mView.findViewById(R.id.nomeD);
        Button mSim =  mView.findViewById(R.id.btnSim);
        Button mNao =  mView.findViewById(R.id.btnNao);

        alertDialog.setTitle("Editar Aniversário");

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        nomeE.setText(getDtnas);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String bday = nomeE.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_FILHO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //progess.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //boolean success = jsonObject.getBoolean("success");
                                    String success = jsonObject.getString("success");

                                    if (success.equals("OK")){
                                        LocalBroadcastManager.getInstance(InfFilhosActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(coordinatorLayoutinfF, Snackbar.LENGTH_LONG, InfFilhosActivity.this);
                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                        tv.setText(jsonObject.getString("message"));                                        dialog.dismiss();
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
                        params.put("nome", getNome);
                        params.put("dt_nas", bday);
                        params.put("end_principal", getEnd);
                        params.put("embarque", getEmbarque);
                        params.put("desembarque", getDesembarque);
                        params.put("idKids", idKids);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(InfFilhosActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        dialog.show();
    }

    public void EditNome(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfFilhosActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_nome, null);
        final EditText nomeE = mView.findViewById(R.id.nomeD);
        Button mSim =  mView.findViewById(R.id.btnSim);
        Button mNao =  mView.findViewById(R.id.btnNao);

        nomeE.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        alertDialog.setTitle("Editar Nome");

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        nomeE.setText(getNome);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nome = nomeE.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_FILHO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //progess.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //boolean success = jsonObject.getBoolean("success");
                                    String success = jsonObject.getString("success");

                                    if (success.equals("OK")){
                                        LocalBroadcastManager.getInstance(InfFilhosActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(coordinatorLayoutinfF, Snackbar.LENGTH_LONG, InfFilhosActivity.this);
                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                        tv.setText(jsonObject.getString("message"));                                        dialog.dismiss();
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
                        params.put("nome", nome);
                        params.put("dt_nas", getDtnas);
                        params.put("end_principal", getEnd);
                        params.put("embarque", getEmbarque);
                        params.put("desembarque", getDesembarque);
                        params.put("idKids", idKids);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(InfFilhosActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        dialog.show();
    }

    public void EditEnd(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfFilhosActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_end, null);
        final EditText nomeE = mView.findViewById(R.id.nomeD);
        Button mSim =  mView.findViewById(R.id.btnSim);
        Button mNao =  mView.findViewById(R.id.btnNao);

        nomeE.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        alertDialog.setTitle("Editar Endereço");

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        nomeE.setText(getEnd);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String end = nomeE.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_FILHO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //progess.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //boolean success = jsonObject.getBoolean("success");
                                    String success = jsonObject.getString("success");

                                    if (success.equals("OK")){
                                        LocalBroadcastManager.getInstance(InfFilhosActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(coordinatorLayoutinfF, Snackbar.LENGTH_LONG, InfFilhosActivity.this);
                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                        tv.setText(jsonObject.getString("message"));                                        dialog.dismiss();
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
                        params.put("nome", getNome);
                        params.put("dt_nas", getDtnas);
                        params.put("end_principal", end);
                        params.put("embarque", getEmbarque);
                        params.put("desembarque", getDesembarque);
                        params.put("idKids", idKids);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(InfFilhosActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        dialog.show();
    }

    public void EditDat(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfFilhosActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_bday, null);
        final EditText nomeE = mView.findViewById(R.id.nomeD);
        Button mSim =  mView.findViewById(R.id.btnSim);
        Button mNao =  mView.findViewById(R.id.btnNao);

        alertDialog.setTitle("Editar Aniversário");

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        nomeE.setText(getDtnas);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String bday = nomeE.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_FILHO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //progess.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //boolean success = jsonObject.getBoolean("success");
                                    String success = jsonObject.getString("success");

                                    if (success.equals("OK")){
                                        LocalBroadcastManager.getInstance(InfFilhosActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();

                                    }else {
                                        snackbar = showSnackbar(coordinatorLayoutinfF, Snackbar.LENGTH_LONG, InfFilhosActivity.this);
                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView tv = (TextView) view.findViewById(R.id.textSnack);
                                        tv.setText(jsonObject.getString("message"));                                        dialog.dismiss();
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
                        params.put("nome", getNome);
                        params.put("dt_nas", bday);
                        params.put("end_principal", getEnd);
                        params.put("embarque", getEmbarque);
                        params.put("desembarque", getDesembarque);
                        params.put("idKids", idKids);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(InfFilhosActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        dialog.show();
    }

    public void editarEmbarque(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfFilhosActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_horario, null);
        final EditText nomeE = mView.findViewById(R.id.nomeD);
        Button mSim =  mView.findViewById(R.id.btnSim);
        Button mNao =  mView.findViewById(R.id.btnNao);

        alertDialog.setTitle("Editar Horário de Embarque");

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        nomeE.setText(getEmbarque);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String embarque = nomeE.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_FILHO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //progess.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //boolean success = jsonObject.getBoolean("success");
                                    String success = jsonObject.getString("success");

                                    if (success.equals("OK")){
                                        LocalBroadcastManager.getInstance(InfFilhosActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(coordinatorLayoutinfF, Snackbar.LENGTH_LONG, InfFilhosActivity.this);
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
                        params.put("nome", getNome);
                        params.put("dt_nas", getDtnas);
                        params.put("end_principal", getEnd);
                        params.put("embarque", embarque);
                        params.put("desembarque", getDesembarque);
                        params.put("idKids", idKids);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(InfFilhosActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        dialog.show();
    }

    public void editarDesembarque(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfFilhosActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_horario, null);
        final EditText nomeE = mView.findViewById(R.id.nomeD);
        Button mSim =  mView.findViewById(R.id.btnSim);
        Button mNao =  mView.findViewById(R.id.btnNao);

        alertDialog.setTitle("Editar Horário de Desembarque");

        alertDialog.setView(mView);
        final AlertDialog dialog = alertDialog.create();

        nomeE.setText(getDesembarque);

        mNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desembarque = nomeE.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_FILHO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //progess.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //boolean success = jsonObject.getBoolean("success");
                                    String success = jsonObject.getString("success");

                                    if (success.equals("OK")){
                                        LocalBroadcastManager.getInstance(InfFilhosActivity.this).sendBroadcast(new Intent("data_changed"));
                                        dialog.dismiss();
                                    }else {
                                        snackbar = showSnackbar(coordinatorLayoutinfF, Snackbar.LENGTH_LONG, InfFilhosActivity.this);
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
                        params.put("nome", getNome);
                        params.put("dt_nas", getDtnas);
                        params.put("end_principal", getEnd);
                        params.put("embarque", getEmbarque);
                        params.put("desembarque", desembarque);
                        params.put("idKids", idKids);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(InfFilhosActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        dialog.show();
    }

    //Pegar as infs do BD
    private void getUserDetail(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Mensagem GetUserDetail", response.toString());
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray nameArray = json.names();
                            String success = json.getString("error");
                            JSONArray valArray = json.toJSONArray( nameArray );
                            if (success.equals("sucesso")){
                                for (int i = 0; i < valArray.length(); i++) {
                                    JSONObject object = valArray.getJSONObject(i);
                                    //String id = object.getString("idKids").trim();
                                    String nome = object.getString("nome").trim();
                                    String dt_nas = object.getString("dt_nas").trim();
                                    String periodo = object.getString("periodo").trim();
                                    String end_principal = object.getString("end_principal").trim();
                                    String status = object.getString("status").trim();
                                    String embarque = object.getString("embarque").trim();
                                    String desembarque = object.getString("desembarque").trim();
                                    String nm_escola = object.getString("nm_escola").trim();
                                    String nm_tio = object.getString("nm_tio").trim();
                                    String strImage = object.getString("img").trim();

                                    txtApelido.setText(nome);
                                    nomeK.setText(nome);
                                    escolaK.setText(nm_escola);
                                    txtPeriodo.setText(periodo);
                                    endK.setText(end_principal);
                                    dt_nasc.setText(dt_nas);
                                    txtTios.setText(nm_tio);
                                    txtStatus.setText(status);
                                    txtEmbarque.setText(embarque);
                                    txtDesembarque.setText(desembarque);

                                    getNome = nome;
                                    getEscola = nm_escola;
                                    getPeriodo = periodo;
                                    getEnd = end_principal;
                                    getDtnas = dt_nas;
                                    getTios = nm_tio;
                                    getEmbarque = embarque;
                                    getDesembarque = desembarque;
                                    getStatus = status;

                                    if (getStatus.equals("Faltou")){
                                        txtStatus.setTextColor(Color.RED);
                                    }

                                    //Glide.with(InfFilhosActivity.this).load(strImage).apply(cropOptions).into(imgInfKids);
                                }
                            }else {
                                snackbar = showSnackbar(coordinatorLayoutinfF, Snackbar.LENGTH_LONG, InfFilhosActivity.this);
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
                param.put("idKids", idKids);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private BroadcastReceiver dataChangeReceiver1= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // update your listview
            getUserDetail();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getUserDetail();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserDetail();
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



