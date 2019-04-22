package br.com.apptransescolar.Activies;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import static br.com.apptransescolar.API.URLs.URL_UPLOAD;
import static br.com.apptransescolar.API.URLs.URL_UPLOAD_KIDS;


public class InfFilhosActivity extends AppCompatActivity {

    TextView nomeK, escolaK, endK, dt_nasc, txtApelido, txtTios, txtStatus, txtPeriodo;
    String id;
    CircleImageView imgInfKids;
    private static final String TAG = PerfilActivity.class.getSimpleName();
    private Bitmap bitmap;
    Kids kids;

    String idKids;

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

        txtTios = findViewById(R.id.txtTios);
        txtStatus = findViewById(R.id.txtStatus);
        txtPeriodo = findViewById(R.id.txtPeriodo);

        txtApelido.setText(kids.getNome());
        nomeK.setText(kids.getNome());
        escolaK.setText(kids.getNm_escola());
        txtPeriodo.setText(kids.getPeriodo());
        endK.setText(kids.getEnd_principal());
        dt_nasc.setText(kids.getDt_nas());
        txtTios.setText(kids.getTio());

        idKids = String.valueOf(kids.getIdKids());

        RequestOptions cropOptions = new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).placeholder(R.drawable.kids).error(R.drawable.kids);
        Glide.with(this).load(kids.getImg()).apply(cropOptions).into(imgInfKids);

        imgInfKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosseFile();

            }
        });

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

                            if (success.equals("1")){

                                Toast.makeText(InfFilhosActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Erro", "Upload", e);
                            Toast.makeText(InfFilhosActivity.this,"Opss! Tente Novamente!",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InfFilhosActivity.this,"Opss! Algo deu errado!",Toast.LENGTH_LONG).show();
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

                            if (success.equals("1")){
                                Toast.makeText(InfFilhosActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(InfFilhosActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e1) {
                            Toast.makeText(InfFilhosActivity.this, "Ops! Algo deu errado!", Toast.LENGTH_SHORT).show();
                            Log.e("JSON", "Error parsing JSON", e1);
                        }
                        Log.e(TAG, "response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InfFilhosActivity.this, "Ops! Algo deu errado!", Toast.LENGTH_SHORT).show();
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
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(InfFilhosActivity.this);
        builder.setMessage("VOCÊ DESEJA DELETAR?")
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletarKidsDialog();
                        Intent intent = new Intent(InfFilhosActivity.this, FilhosActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
    }
}



