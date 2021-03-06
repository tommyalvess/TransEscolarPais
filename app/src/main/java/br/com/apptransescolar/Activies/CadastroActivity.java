package br.com.apptransescolar.Activies;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.apptransescolar.API.ApiClient;
import br.com.apptransescolar.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    EditText editNome, editCpf, editTell, editSenha, editEmail;
    ProgressBar progressBar;
    Button btnSaveCadastro;
    TextView text;
    static Snackbar snackbar;
    static ConstraintLayout constraintLayoutCas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Cadastro");     //Titulo para ser exibido na sua Action Bar em frente à seta


        editNome = findViewById(R.id.editNomeT);
        editCpf =  findViewById(R.id.end);
        editTell =  findViewById(R.id.periodo);
        editSenha = findViewById(R.id.editSenhaT);
        editEmail = findViewById(R.id.dtNasc);
        constraintLayoutCas = findViewById(R.id.constraintLayoutCas);

        editNome.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);


        findViewById(R.id.btnSaveCadastro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });


    }

    private void registerUser() {

        final String nome = editNome.getText().toString().trim();
        final String cpf = editCpf.getText().toString().trim();
        final String tell = editTell.getText().toString().trim();
        final String senha = editSenha.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();

        if (nome.isEmpty()){
            editNome.setError("Insira o seu nome");
            editNome.requestFocus();
            return;
        }
        if (cpf.isEmpty()){
            editCpf.setError("Insira o seu CPF");
            editCpf.requestFocus();
            return;
        }

        if (tell.isEmpty()){
            editTell.setError("Insira o telefone");
            editTell.requestFocus();
            return;
        }
        if (email.isEmpty()){
            editEmail.setError("Insira um email!");
            editEmail.requestFocus();
            return;
        }
        if (validateEmail(email) != true){
            editEmail.setError("Email invalido!");
            editEmail.requestFocus();
            return;
        }


        if (senha.isEmpty()){
            editSenha.setError("Insira sua senha!");
            editSenha.requestFocus();
            return;
        }

        if (senha.length() < 6){
            editSenha.setError("Senha deve ter 6 caracteres!");
            editSenha.requestFocus();
            return;
        }

        Call<ResponseBody> call = ApiClient
                .getInstance()
                .getApi()
                .createuser(nome, email, cpf, tell, senha);

      call.enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              String s = null;

              try {
                  if (response.code() == 201) {

                      s = response.body().string();
                      Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                      startActivity(intent);

                  }else {
                      s = response.errorBody().string();
                      snackbar = showSnackbar(constraintLayoutCas, Snackbar.LENGTH_LONG, CadastroActivity.this);
                      snackbar.show();
                      View view = snackbar.getView();
                      TextView tv = (TextView) view.findViewById(R.id.textSnack);
                      tv.setText(s);
                  }
              } catch (IOException e) {
                  e.printStackTrace();
              }

              if (s != null){
                  try {
                      JSONObject jsonObject = new JSONObject(s);

                      snackbar = showSnackbar(constraintLayoutCas, Snackbar.LENGTH_LONG, CadastroActivity.this);
                      snackbar.show();
                      View view = snackbar.getView();
                      TextView tv = (TextView) view.findViewById(R.id.textSnack);
                      tv.setText(jsonObject.getString("message"));

                  }catch (JSONException e){
                      e.printStackTrace();
                      Log.e("Call", "Erro", e);
                  }
              }
          }

          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
              Log.e("Call", "Error", t);
          }
      });

    }// registerUser

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}"
        );
        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
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

    private boolean isEmailCorrect(String email) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        return emailPattern.matcher(email).matches();
    }

    private static boolean validateEmail(String email) {
        Pattern patternEmail = Patterns.EMAIL_ADDRESS;
        if (TextUtils.isEmpty(email))
            return false;

        Matcher matcher = patternEmail.matcher(email);
        return matcher.matches();
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

}//CadastroActivity
