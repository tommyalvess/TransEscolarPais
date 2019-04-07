package br.com.apptransescolar.Classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Kids implements Serializable {

    @SerializedName("idKids") private int idKids;
    @SerializedName("Nome") private String nome;
    @SerializedName("Periodo") private String periodo;
    @SerializedName("Img") private String img;
    @SerializedName("Escola") private String nm_escola;
    @SerializedName("DtNas") private String dt_nas;
    @SerializedName("End") private String end_principal;


    public Kids(String nome, String escola, String periodo, String endereco, String dtNas) {
        this.nome = nome;
        this.nm_escola = escola;
        this.periodo = periodo;
        this.end_principal = endereco;
        this.dt_nas = dtNas;

    }


    public Kids(int id, String nome, String escola, String periodo, String endereco, String dtNas) {
        this.idKids = id;
        this.nome = nome;
        this.nm_escola = escola;
        this.periodo = periodo;
        this.end_principal = endereco;
        this.dt_nas = dtNas;
    }

    public Kids() {

    }

    public int getIdKids() {
        return idKids;
    }

    public void setIdKids(int idKids) {
        this.idKids = idKids;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNm_escola() {
        return nm_escola;
    }

    public void setNm_escola(String nm_escola) {
        this.nm_escola = nm_escola;
    }

    public String getDt_nas() {
        return dt_nas;
    }

    public void setDt_nas(String dt_nas) {
        this.dt_nas = dt_nas;
    }

    public String getEnd_principal() {
        return end_principal;
    }

    public void setEnd_principal(String end_principal) {
        this.end_principal = end_principal;
    }

    public String toString(){
        return this.nome;
    }
}
