package factory;

import model.VehicleManagement.*;

public class VehicleFactory {
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
