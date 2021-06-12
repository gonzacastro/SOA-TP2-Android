package Models;

public class EventRequest {
    private String env;
    private String type_events;
    private String description;

    public EventRequest(String type_events, String description) {
        this.env = "PROD";
        this.type_events = type_events;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_events() {
        return type_events;
    }

    public void setType_events(String type_events) {
        this.type_events = type_events;
    }
}
