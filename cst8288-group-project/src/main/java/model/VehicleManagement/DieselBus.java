package model.VehicleManagement;

/**
 * Represents a Diesel Bus vehicle implementation.
 * Implements the {@link VehicleInterface} and provides
 * specific behavior for diesel bus vehicles.
 *
 * <p>This class can be instantiated through a factory pattern such as {@link factory.VehicleFactory}.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 * @see VehicleInterface
 */
public class DieselBus implements VehicleInterface {

    /**
     * Displays information specific to a Diesel Bus.
     * This method overrides {@code displayVehicleInfo()} from {@link VehicleInterface}.
     */
    @Override
    public void displayVehicleInfo() {
        System.out.println("This is a Diesel Bus.");
    }
}
