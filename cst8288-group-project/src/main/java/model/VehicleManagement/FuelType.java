package model.VehicleManagement;

/**
 * Represents a type of fuel used by a vehicle in the system.
 * This class holds the ID and name of the fuel type (e.g., Diesel, Electric, CNG).
 * It is used by vehicle-related classes such as {@link Vehicle}.
 * 
 * <p>Typically maps to a record in the {@code FuelTypes} database table.</p>
 * 
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 */
public class FuelType {
    private int fuelTypeID;
    private String typeName;

    /**
     * Default constructor.
     */
    public FuelType() {}

    /**
     * Full constructor to initialize all fields.
     * 
     * @param fuelTypeID the unique ID of the fuel type
     * @param typeName the name/label of the fuel type
     */
    public FuelType(int fuelTypeID, String typeName) {
        this.fuelTypeID = fuelTypeID;
        this.typeName = typeName;
    }

    /**
     * Gets the fuel type ID.
     * 
     * @return the fuel type ID
     */
    public int getFuelTypeID() {
        return fuelTypeID;
    }

    /**
     * Sets the fuel type ID.
     * 
     * @param fuelTypeID the ID to set
     */
    public void setFuelTypeID(int fuelTypeID) {
        this.fuelTypeID = fuelTypeID;
    }

    /**
     * Gets the fuel type name.
     * 
     * @return the type name
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the fuel type name.
     * 
     * @param typeName the name to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
