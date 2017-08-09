package kotov.invisible.taitihoteladmin.interfaces;

import java.util.List;

import kotov.invisible.taitihoteladmin.ApiEngine.APIAnswer;
import kotov.invisible.taitihoteladmin.ApiEngine.Order;
import kotov.invisible.taitihoteladmin.ApiEngine.Request;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TaitiApi {

    String prefix = "";

    @GET(prefix + "api/v1/order.getAll")
    Call<List<Order>> orderGetAll();

    @GET(prefix + "api/v1/request.getAll")
    Call<List<Request>> requestGetAll();

    @DELETE(prefix + "api/v1/order.delete/{id}")
    Call<APIAnswer> orderDelete(@Path("id") int id);

    @DELETE(prefix + "api/v1/request.delete/{id}")
    Call<APIAnswer> requestDelete(@Path("id") int id);

    @POST(prefix + "api/v1/order.add")
    Call<APIAnswer> orderAdd(@Body Order order);
}
