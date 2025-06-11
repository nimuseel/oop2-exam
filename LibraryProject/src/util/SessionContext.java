package util;

public class SessionContext {
    private static SessionContext instance;
    private String currentUserId;

    private SessionContext() {}

    public static SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }
    
    public void clear() {
        currentUserId = null;
    }
}
