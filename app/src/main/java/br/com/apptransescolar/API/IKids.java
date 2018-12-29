package br.com.apptransescolar.API;

import java.util.List;

import br.com.apptransescolar.Classes.Kids;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IKids {

    @GET("getKids.php")
    Call<List<Kids>> getAllKids(
            @Query("item_type") String item_type,
            @Query("key") String keyword
    );

}
