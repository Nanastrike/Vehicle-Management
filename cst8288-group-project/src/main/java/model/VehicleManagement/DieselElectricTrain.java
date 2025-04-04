package model.VehicleManagement;

/**
 * Represents a Diesel-Electric Train vehicle.
 * Implements the {@link VehicleInterface} to provide specific behavior 
 * for diesel-electric train types in the system.
 *
 * <p>This class is typically created through a factory pattern such as {@link factory.VehicleFactory}.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 * @see VehicleInterface
 */
public class DieselElectricTrain implements VehicleInterface {

    /**
     * Displays specific information about this Diesel-Electric Train.
     * Overrides the method defined in {@link VehicleInterface}.
     */
    @Override
    public void displayVehicleInfo() {
        System.out.println("This is a Diesel-Electric Train.");
    }
}

