package APIs;

import java.util.Map;

import Models.EventRequest;
import Models.EventResponse;
import Models.LoginRequest;
import Models.RegistroRequest;
import Models.APIResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface RetrofitAPI {

    @POST("api/api/register")
    Call<APIResponse> postRegister(@Body RegistroRequest dr);

    @POST("api/api/login")
    Call<APIResponse> postLogin(@Body LoginRequest lr);

    @PUT("api/api/refresh")
    Call<APIResponse> putRefreshToken(@HeaderMap Map<String, String> headers);

    @POST("api/api/event")
    Call<EventResponse> postEvent(@HeaderMap Map<String, String> headers, @Body EventRequest er);

}
