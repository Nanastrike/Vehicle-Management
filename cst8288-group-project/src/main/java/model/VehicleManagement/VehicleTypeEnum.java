package model.VehicleManagement;

/**
 * Enumeration of supported vehicle types in the transit fleet management system.
 * Each value represents a different type of public transit vehicle.
 * 
 * <p>This enum can be used for categorizing vehicles and enforcing type safety
 * when working with different types of transit vehicles.</p>
 * 
 * @author Zhennan
 * @version 1.0
 * @since Java 21
 */
public enum VehicleTypeEnum {

    /** Diesel-powered bus used for short to medium routes. */
    DIESEL_BUS,

    /** Electrically-powered light rail vehicle used in urban transit. */
    ELECTRIC_LIGHT_RAIL,

    /** Train powered by a combination of diesel and electric energy. */
    DIESEL_ELECTRIC_TRAIN
}
