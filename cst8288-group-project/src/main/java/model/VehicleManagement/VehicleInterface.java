package model.VehicleManagement;

/**
 * Interface for vehicle-related functionality.
 * Classes implementing this interface must provide a method to display vehicle information.
 * 
 * <p>This promotes polymorphism by allowing different vehicle types to define
 * their own version of how vehicle information should be displayed.</p>
 * 
 * @author Zhennan
 * @version 1.0
 * @since Java 21
 */
public interface VehicleInterface {

    /**
     * Displays detailed information about the vehicle.
     * Implementing classes should define how vehicle data is presented.
     */
    void displayVehicleInfo();
}

