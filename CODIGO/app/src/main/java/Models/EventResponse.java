package Models;

import com.google.gson.annotations.SerializedName;

public class EventResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("env")
    private String env;
    @SerializedName("event")
    private Event event;

    public EventResponse(boolean success, String env, Event event) {
        this.success = success;
        this.env = env;
        this.event = event;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "EventResponse{" +
                "success=" + success +
                ", env='" + env + '\'' +
                ", event=" + event.toString() +
                '}';
    }
}
