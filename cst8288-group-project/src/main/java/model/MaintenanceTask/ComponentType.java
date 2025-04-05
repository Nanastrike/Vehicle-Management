package model.MaintenanceTask;

/**
 * Enumeration of different types of vehicle components that can be monitored.
 * Each component type has an associated display name for user interface purposes.
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 */
public enum ComponentType {
    /** Mechanical components such as brakes, wheels, and bearings */
    MECHANICAL("Mechanical"),
    /** Electrical components such as wiring, batteries, and motors */
    ELECTRICAL("Electrical"),
    /** Engine-related components including the main engine and its subsystems */
    ENGINE("Engine");
    
    /** The human-readable name for the component type */
    private final String displayName;
    
    /**
     * Constructs a component type with a specified display name.
     * @param displayName The human-readable name for this component type
     */
    ComponentType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of the component type.
     * @return The human-readable name for this component type
     */
    public String getDisplayName() {
        return displayName;
    }
} 