package model.MaintenanceTask;

public enum ComponentType {
    MECHANICAL("Mechanical"),
    ELECTRICAL("Electrical"),
    ENGINE("Engine");
    
    private final String displayName;
    
    ComponentType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 