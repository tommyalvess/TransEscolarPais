package br.com.apptransescolar.Classes;

import com.google.gson.annotations.SerializedName;

public class Escolas {

    @SerializedName("idEscola") private int id;
    @SerializedName("nm_escola") private String nome;
    @SerializedName("endereco") private String endereco;
    @SerializedName("tell") private String tell;

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTell() {
        return tell;
    }


}
