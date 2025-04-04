package model.VehicleManagement;

/**
 * Represents a vehicle type used in the system.
 * Each vehicle type has a unique ID and a name, such as "Diesel Bus" or "Electric Light Rail".
 *
 * <p>This class is typically used when assigning types to vehicles or retrieving type-related metadata
 * from the {@code VehicleTypes} table in the database.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 */
public class VehicleType {

    /** The unique identifier for the vehicle type. */
    private int vehicleTypeID;

    /** The name of the vehicle type (e.g., "Diesel Bus"). */
    private String typeName;

    /**
     * Default constructor.
     */
    public VehicleType() {}

    /**
     * Full constructor to initialize a VehicleType object.
     *
     * @param vehicleTypeID the unique ID of the vehicle type
     * @param typeName the name of the vehicle type
     */
    public VehicleType(int vehicleTypeID, String typeName) {
        this.vehicleTypeID = vehicleTypeID;
        this.typeName = typeName;
    }

    /**
     * Gets the vehicle type ID.
     *
     * @return the vehicle type ID
     */
    public int getVehicleTypeID() {
        return vehicleTypeID;
    }

    /**
     * Sets the vehicle type ID.
     *
     * @param vehicleTypeID the vehicle type ID to set
     */
    public void setVehicleTypeID(int vehicleTypeID) {
        this.vehicleTypeID = vehicleTypeID;
    }

    /**
     * Gets the name of the vehicle type.
     *
     * @return the type name
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the name of the vehicle type.
     *
     * @param typeName the type name to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
