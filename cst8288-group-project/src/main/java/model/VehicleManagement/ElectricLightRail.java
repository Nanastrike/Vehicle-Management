package model.VehicleManagement;

/**
 * Represents an Electric Light Rail vehicle.
 * Implements the {@link VehicleInterface} to provide specific behavior 
 * for electric light rail types in the system.
 *
 * <p>This class is typically used with a factory pattern such as {@link factory.VehicleFactory}.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 * @see VehicleInterface
 */
public class ElectricLightRail implements VehicleInterface {

    /**
     * Displays specific information about this Electric Light Rail.
     * Overrides the method defined in {@link VehicleInterface}.
     */
    @Override
    public void displayVehicleInfo() {
        System.out.println("This is an Electric Light Rail.");
    }
}
