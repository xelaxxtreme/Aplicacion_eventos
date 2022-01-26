package my.wab.festejaper.db;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Interface {

    @GET("/app/eventos")
    Call<List<cEventos>> obtener();
}
