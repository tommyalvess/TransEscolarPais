package br.com.apptransescolar.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IPais {

    @FormUrlEncoded
    @POST("createPais")
    Call<ResponseBody> createuser (
            @Field("nm_pai") String nome,
            @Field("email") String email,
            @Field("cpf") String cpf,
            @Field("tell") String tell,
            @Field("senha") String senha
    );

    @FormUrlEncoded
    @POST("createKids")
    Call<ResponseBody> createkids (
            @Field("nome") String nome,
            @Field("dt_nas") String data,
            @Field("end_principal") String end,
            @Field("periodo") String periodo,
            @Field("embarque") String embarque,
            @Field("desembarque") String desembarque,
            @Field("idTios") int idTios,
            @Field("idEscola") int idEscola,
            @Field("idPais") int idPais
    );

}
