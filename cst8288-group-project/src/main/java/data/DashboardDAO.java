package data;

import model.VehicleManagement.Vehicle;
import java.util.*;

public class DashboardDAO {
    private VehicleDAO vehicleDAO;

    public DashboardDAO(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    public Map<String, Integer> getVehicleTypeCounts() {
        Map<String, Integer> counts = new HashMap<>();
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        for (Vehicle v : vehicleList) {
            String type = v.getVehicleType().getTypeName();
            counts.put(type, counts.getOrDefault(type, 0) + 1);
        }
        return counts;
    }

    public Vehicle getLastAddedVehicle() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        return vehicles.isEmpty() ? null : vehicles.get(vehicles.size() - 1);
    }
}

