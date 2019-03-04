package br.com.apptransescolar.Classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tio implements Serializable {
    @SerializedName("nome") private String nome;
    @SerializedName("email")private String email;
    @SerializedName("cpf")private String cpf;
    @SerializedName("apelido")private String apelido;
    @SerializedName("placa")private String placa;
    @SerializedName("tell")private String tell;
    @SerializedName("senha")private String senha;
    @SerializedName("img")private String img;
    @SerializedName("idTios") private int id;

    public Tio(String nome, String email, String cpf, String apelido, String placa, String tell, String senha, String img, int id) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.apelido = apelido;
        this.placa = placa;
        this.tell = tell;
        this.senha = senha;
        this.img = img;
        this.id = id;
    }

    public Tio() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
