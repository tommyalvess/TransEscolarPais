package br.com.apptransescolar.Activies;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import br.com.apptransescolar.Classes.Tios;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static br.com.apptransescolar.API.URLs.URL_COUNTTIOSKIDS;
import static br.com.apptransescolar.API.URLs.URL_COUNTTIOSPAIS;
import static br.com.apptransescolar.API.URLs.URL_DELETE_KIDS;
import static br.com.apptransescolar.API.URLs.URL_DELETE_TIOS;

public class InfTioActivity extends AppCompatActivity {

    TextView nomeT, emailT, placaT, tellT, apelidoT, mapOn, countKids,countPais;
    ImageView mapOnn;
    String cpf, getId;
    CircleImageView imgPerfilT;

    String getCpf, getNome, novoTell, idPais, idTios, getID;
    String msg;
    static String LoggedIn_User_Email;

    SessionManager sessionManager;

    View contextView;

    Tios tios;


    final String[] items = {"Desculpe, ele não irá hoje!", "Desculpe, estamos atrasados!"};

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_tio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contextView = findViewById(R.id.contextView);
        apelidoT = findViewById(R.id.txtApelido);
        imgPerfilT = findViewById(R.id.imgPerfilT);
        nomeT = findViewById(R.id.nomeT);
        tellT = findViewById(R.id.tellT);
        placaT = findViewById(R.id.txtPlaca);
        emailT = findViewById(R.id.txtEmail);
        mapOnn = findViewById(R.id.mapOnn);
        mapOn = findViewById(R.id.mapOn);
        countKids = findViewById(R.id.countKids);
        countPais = findViewById(R.id.countPais);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getCpf = user.get(sessionManager.CPF);
        getNome = user.get(sessionManager.NAME);
        getID = user.get(sessionManager.ID);

        tios = (Tios) getIntent().getExtras().get("tios");

        getId = String.valueOf(tios.getId());
        nomeT.setText(tios.getNome());
        tellT.setText(tios.getTell());
        placaT.setText(tios.getPlaca());
        apelidoT.setText(tios.getApelido());
        emailT.setText(tios.getEmail());

        novoTell = tios.getTell();

        idPais = getID;
        idTios = String.valueOf(tios.getId());

        String   tellAString = novoTell.replace("-", "" );
        String   tellBString = tellAString.replace("(", "" );
        final String   tellCString = tellBString.replace(")", "" );

        RequestOptions cropOptions = new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).error(R.drawable.kids);
        Glide.with(this).load(tios.getImg()).apply(cropOptions).into(imgPerfilT);


        LoggedIn_User_Email = tios.getCpf();

        mapOnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(InfTioActivity.this, PaisMapsActivity.class);
                it.putExtra("tios", tios);
                InfTioActivity.this.startActivity(it);
            }
        });

        mapOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(InfTioActivity.this, PaisMapsActivity.class);
                it.putExtra("tios", tios);
                InfTioActivity.this.startActivity(it);
            }
        });

        countKids();
        countPais();

    }

    private void notificationMesage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione a Mensagem!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if (item == 0){
                    Snackbar.make(contextView, R.string.mensagem_enviada, Snackbar.LENGTH_SHORT)
                            .show();
                    sendNotificationFirst();
                }else if (item == 1){
                    Snackbar.make(contextView, R.string.mensagem_enviada, Snackbar.LENGTH_SHORT)
                            .show();
                    sendNotificationSecond();
                }
            }
        });
        builder.show();
    }

    //OneSignal Notification

    private void sendNotificationFirst()
    {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email = cpf;

//                    //This is a Simple Logic to Send Notification different Device Programmatically....
//                    if (InfTioActivity.LoggedIn_User_Email.equals(cpf)) {
//                        send_email = getCpf;
//                    } else {
//                        send_email = getCpf;
//                    }

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MzUzNzMxMjMtOTIxNy00ZTBlLTg2YjktMDRlOTg4YmEwNzFh");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"ef54b0b1-d6b0-46e0-ad4f-4e15d9f7dfe6\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \" "+ getNome +":"+"Desculpe, ele não irá hoje!"+"\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendNotificationSecond()
    {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email = cpf;

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MzUzNzMxMjMtOTIxNy00ZTBlLTg2YjktMDRlOTg4YmEwNzFh");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"ef54b0b1-d6b0-46e0-ad4f-4e15d9f7dfe6\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \" "+ getNome +":"+"Desculpe, estamos atrasados!"+"\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendNotificationThird()
    {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email = cpf;

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MzUzNzMxMjMtOTIxNy00ZTBlLTg2YjktMDRlOTg4YmEwNzFh");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"ef54b0b1-d6b0-46e0-ad4f-4e15d9f7dfe6\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \" "+ getNome +":"+"Estamos atrasados!"+"\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    //Count Kids e Pais

    private void countKids(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COUNTTIOSKIDS,
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

                                    countKids.setText(nome);
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
                        Toast.makeText(InfTioActivity.this, "Opss! Algo deu errado!", Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", "Error", error);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("idTios", getId);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void countPais(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COUNTTIOSPAIS,
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

                                    countPais.setText(nome);
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
                        Toast.makeText(InfTioActivity.this, "Opss! Algo deu errado!", Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", "Error", error);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("idTios", getId);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inf_tio, menu);
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

    private void deletarTiosDialog() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_TIOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progess.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //boolean success = jsonObject.getBoolean("success");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(InfTioActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(InfTioActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e1) {
                            Log.e("JSON", "Error parsing JSON", e1);
                        }
                        Log.e("Chamada:", "response: " + response);
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
                params.put("idTios", idTios);
                params.put("idPais", idPais);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InfTioActivity.this);
        requestQueue.add(stringRequest);
    }


    public void deletarPais(MenuItem item) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(InfTioActivity.this);
        builder.setMessage("VOCÊ DESEJA DELETAR?")
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletarTiosDialog();
                        finish();
                        Intent intent = new Intent(InfTioActivity.this, TiosActivity.class);
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
