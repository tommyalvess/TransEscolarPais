package br.com.apptransescolar.Classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Kids implements Serializable {

    @SerializedName("idKids") private int idKids;
    @SerializedName("nome") private String nome;
    @SerializedName("periodo") private String periodo;
    @SerializedName("img") private String img;
    @SerializedName("nm_escola") private String nm_escola;
    @SerializedName("dt_nas") private String dt_nas;
    @SerializedName("end_principal") private String end_principal;
    @SerializedName("status") private String status;
    @SerializedName("nm_tio") private String tio;
    @SerializedName("embarque") private String embarque;
    @SerializedName("desembarque") private String desembarque;


    public Kids(int idKids, String nome, String periodo, String img, String nm_escola, String dt_nas, String end_principal, String status, String tio, String embarque, String desembarque) {
        this.idKids = idKids;
        this.nome = nome;
        this.periodo = periodo;
        this.img = img;
        this.nm_escola = nm_escola;
        this.dt_nas = dt_nas;
        this.end_principal = end_principal;
        this.status = status;
        this.tio = tio;
        this.embarque = embarque;
        this.desembarque = desembarque;
    }

    public Kids() {

    }

    public Kids(int id, String nome, String escola, String periodo, String endereco, String aniver) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTio() {
        return tio;
    }

    public void setTio(String tio) {
        this.tio = tio;
    }

    public String getEmbarque() {
        return embarque;
    }

    public void setEmbarque(String embarque) {
        this.embarque = embarque;
    }

    public String getDesembarque() {
        return desembarque;
    }

    public void setDesembarque(String desembarque) {
        this.desembarque = desembarque;
    }
}
