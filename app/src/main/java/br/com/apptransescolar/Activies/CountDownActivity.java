package br.com.apptransescolar.Activies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import br.com.apptransescolar.Classes.Tios;
import br.com.apptransescolar.Conexao.SessionManager;
import br.com.apptransescolar.R;

public class CountDownActivity extends AppCompatActivity {

    TextView txtTempo;
    Button btnOK;
    String meuTio, tioCPF, getCpf, getId;


    SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        txtTempo = findViewById(R.id.txtTempo);
        btnOK = findViewById(R.id.btnOK);

        Tios tios = (Tios) getIntent().getExtras().get("tios");

        meuTio = tios.getApelido().trim();
        tioCPF = tios.getCpf().trim();

        //iniciando a sessão
        sessionManager = new SessionManager(this);

        //pegando dados da sessão
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.NAME);
        getCpf = user.get(sessionManager.CPF);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotificationFirst();
                Intent intent = new Intent(CountDownActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        startTimer();
    }

    private void startTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtTempo.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                Toast.makeText(CountDownActivity.this, "Atentção! O Tio já vai sair!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CountDownActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

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
                    String send_email = tioCPF;

//                    //This is a Simple Logic to Send Notification different Device Programmatically....
//                    if (LoggedIn_User_Email.equals(getCpf)) {
//                        send_email = tioCPF;
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
                                + "\"contents\": {\"en\": \" "+ getId +" "+": Estamos indo!"+"\"}"
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

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}
