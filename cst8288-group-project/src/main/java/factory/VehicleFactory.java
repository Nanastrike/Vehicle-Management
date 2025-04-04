package factory;

import model.VehicleManagement.*;

/**
 * Factory class for creating vehicle instances based on the {@link VehicleTypeEnum}.
 * This class abstracts the instantiation of specific vehicle types such as DieselBus, ElectricLightRail, etc.
 *
 * <p>Usage:</p>
 * <pre>{@code
 * VehicleInterface vehicle = VehicleFactory.getVehicle(VehicleTypeEnum.DIESEL_BUS);
 * }</pre>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 *
 * @see model.VehicleManagement.VehicleInterface
 * @see model.VehicleManagement.VehicleTypeEnum
 * @see model.VehicleManagement.DieselBus
 * @see model.VehicleManagement.ElectricLightRail
 * @see model.VehicleManagement.DieselElectricTrain
 */
public class VehicleFactory {

    /**
     * Returns an instance of a vehicle class that implements {@link VehicleInterface},
     * based on the given {@link VehicleTypeEnum}.
     *
     * @param vehicleType the type of vehicle to create
     * @return an instance of a class implementing VehicleInterface
     * @throws IllegalArgumentException if the provided vehicle type is not supported
     */
    public static VehicleInterface getVehicle(VehicleTypeEnum vehicleType) {
        switch (vehicleType) {
            case DIESEL_BUS:
                return new DieselBus();
            case ELECTRIC_LIGHT_RAIL:
                return new ElectricLightRail();
            case DIESEL_ELECTRIC_TRAIN:
                return new DieselElectricTrain();
            default:
                throw new IllegalArgumentException("Invalid vehicle type!");
        }
    }
}
