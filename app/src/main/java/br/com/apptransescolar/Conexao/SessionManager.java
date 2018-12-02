package br.com.apptransescolar.Conexao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import br.com.apptransescolar.Activies.HomeActivity;
import br.com.apptransescolar.Activies.LoginActivity;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String CPF = "CPF";
    public static final String TELL = "TELL";
    public static final String IMG = "IMG";
    public static final String ID = "ID";

    public static final String DATE = "dt_nas";
    public static final String ENDERECO = "end_principal";
    public static final String PERIODO = "periodo";
    public static final String IDT = "idTios";
    public static final String IDE = "idEscola";
    public static final String IDP = "idPais";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String id, String nome, String email, String cpf, String tell, String img, String idTio){

        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, nome);
        editor.putString(EMAIL, email);
        editor.putString(CPF, cpf);
        editor.putString(TELL, tell);
        editor.putString(IMG, img);
        editor.putString(IDT, idTio);
        editor.putString(ID, id);
        editor.apply();

    }

    public void createSession(String id, String nome, String email, String cpf, String tell){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, nome);
        editor.putString(EMAIL, email);
        editor.putString(CPF, cpf);
        editor.putString(TELL, tell);
        editor.putString(ID, id);
        editor.apply();

    }

    public void createSessionKid(String nome, String dt_nas, String end_principal, String periodo, String idTios, String idEscola, String idPais, String id){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, nome);
        editor.putString(DATE, dt_nas);
        editor.putString(ENDERECO, end_principal);
        editor.putString(PERIODO, periodo);
        editor.putString(IDT, idTios);
        editor.putString(IDE, idEscola);
        editor.putString(IDP, idPais);
        editor.putString(ID, id);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }


    public void checkLogin(){

        if (!this.isLoggin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((HomeActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(CPF, sharedPreferences.getString(CPF, null));
        user.put(TELL, sharedPreferences.getString(TELL, null));
        user.put(IMG, sharedPreferences.getString(IMG, null));
        user.put(IDT, sharedPreferences.getString(IDT, null));
        user.put(ID, sharedPreferences.getString(ID, null));

        return user;
    }

    public HashMap<String, String> getPaiDetail(){

        HashMap<String, String> tios = new HashMap<>();
        tios.put(NAME, sharedPreferences.getString(NAME, null));
        tios.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        tios.put(CPF, sharedPreferences.getString(CPF, null));
        tios.put(TELL, sharedPreferences.getString(TELL, null));
        tios.put(ID, sharedPreferences.getString(ID, null));

        return tios;
    }

    public HashMap<String, String> getKidDetail(){
        HashMap<String, String> kid = new HashMap<>();
        kid.put(NAME, sharedPreferences.getString(NAME, null));
        kid.put(DATE, sharedPreferences.getString(DATE, null));
        kid.put(ENDERECO, sharedPreferences.getString(ENDERECO, null));
        kid.put(PERIODO, sharedPreferences.getString(PERIODO, null));
        kid.put(IDT, sharedPreferences.getString(IDT, null));
        kid.put(IDE, sharedPreferences.getString(IDE, null));
        kid.put(IDP, sharedPreferences.getString(IDP, null));
        kid.put(ID, sharedPreferences.getString(ID, null));
        return kid;
    }

    public boolean logout(){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
