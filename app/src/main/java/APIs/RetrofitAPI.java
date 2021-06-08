package APIs;

import Models.LoginRequest;
import Models.RegistroRequest;
import Models.APIResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RetrofitAPI {

    @POST("api/api/register")
    Call<APIResponse> postRegister(@Body RegistroRequest dr);

    @POST("api/api/login")
    Call<APIResponse> postLogin(@Body LoginRequest lr);

}
