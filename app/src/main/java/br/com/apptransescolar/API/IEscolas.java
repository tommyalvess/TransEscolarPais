package br.com.apptransescolar.API;

import java.util.List;

import br.com.apptransescolar.Classes.Escolas;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IEscolas {

    @GET("getcontacts.php")
    Call<List<Escolas>> getEscolas(
            @Query("item_type") String item_type,
            @Query("key") String keyword
    );


}
