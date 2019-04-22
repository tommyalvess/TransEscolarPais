package br.com.apptransescolar.Classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tios implements Serializable {
    @SerializedName("IdT") private int id;
    @SerializedName("Nome") private String nome;
    @SerializedName("Email")private String email;
    @SerializedName("Cpf")private String cpf;
    @SerializedName("Apelido")private String apelido;
    @SerializedName("Placa")private String placa;
    @SerializedName("Tell")private String tell;
    @SerializedName("Senha")private String senha;
    @SerializedName("Img")private String img;

    public Tios(String nome, String email, String cpf, String apelido, String placa, String tell, String senha, String img, int id) {
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

    public Tios() {
    }

    public Tios(int tios) {
        this.id = tios;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String toString(){
        return this.nome;
    }
}
