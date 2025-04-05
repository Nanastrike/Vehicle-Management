package data;

import Fuel_dao.FuelConsumptionDAO;
import Fuel_model.FuelConsumption;
import data.gps_tracking.VehicleActionDTO;
import data.gps_tracking.VehicleActionDaoImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import model.VehicleManagement.Vehicle;
import model.MaintenanceTask.MaintenanceTask;

public class DashboardDAO {

    private VehicleDAO vehicleDAO;
    private MaintenanceTaskDAO maintenanceTaskDAO;
    private VehicleActionDaoImpl vehicleActionDaoImpl;
    private FuelConsumptionDAO fuelDAO;
    
    public DashboardDAO(VehicleDAO vehicleDAO, 
            MaintenanceTaskDAO maintenanceTaskDAO, 
            VehicleActionDaoImpl vehicleActionDaoImpl,
            FuelConsumptionDAO fuelDAO) {
        this.vehicleDAO = vehicleDAO;
        this.maintenanceTaskDAO = maintenanceTaskDAO;
        this.vehicleActionDaoImpl = vehicleActionDaoImpl;
        this.fuelDAO = fuelDAO;
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

    // ✅ Count HIGH priority maintenance tasks
    public int getHighPriorityTaskCount() throws SQLException {
        return maintenanceTaskDAO.getHighPriorityTaskCount();
    }

    // ✅ Get most recent maintenance task
    public MaintenanceTask getMostRecentTask() throws SQLException {
        return maintenanceTaskDAO.getMostRecentTask();
    }
    
    public int getRunningVehiclesCount() throws SQLException{
        return vehicleActionDaoImpl.getRunningVehiclesCount();
    }
    
    public List<VehicleActionDTO> getRecentVehicleActions(int limit) throws SQLException{
        return vehicleActionDaoImpl.getRecentVehicleActions(3);
    }
    
    public int getCriticalFuelCount() throws SQLException{
        return fuelDAO.getCriticalFuelCount();
    }
    
    public List<FuelConsumption> getRecentFuelRecords(int limit) throws SQLException{
        return fuelDAO.getRecentFuelRecords(3);
    }
} 