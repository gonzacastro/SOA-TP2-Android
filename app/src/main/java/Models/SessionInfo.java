package Models;

public class SessionInfo {
    private static String authToken;

    public SessionInfo(String at) {
        authToken = at;
    }

    public String getToken() {
        return authToken;
    }
}
