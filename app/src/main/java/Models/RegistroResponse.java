package Models;

import com.google.gson.annotations.SerializedName;

public class RegistroResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("token")
    private String token;
    @SerializedName("token_refresh")
    private String token_refresh;
    @SerializedName("env")
    private String env;

    public RegistroResponse(boolean success, String token, String token_refresh, String env) {
        this.success = success;
        this.token = token;
        this.token_refresh = token_refresh;
        this.env = env;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken_refresh() {
        return token_refresh;
    }

    public void setToken_refresh(String token_refresh) {
        this.token_refresh = token_refresh;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return "RegistroResponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                ", token_refresh='" + token_refresh + '\'' +
                ", env='" + env + '\'' +
                '}';
    }
}
