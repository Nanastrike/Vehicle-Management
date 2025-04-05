package data;

import Fuel_dao.FuelConsumptionDAO;
import Fuel_model.FuelConsumption;
import data.gps_tracking.VehicleActionDTO;
import data.gps_tracking.VehicleActionDaoImpl;
import java.sql.SQLException;
import java.util.*;
import model.VehicleManagement.Vehicle;
import model.MaintenanceTask.MaintenanceTask;

/**
 * Data Access Object (DAO) for aggregating and retrieving dashboard-related data.
 * This class serves as a data aggregation layer by calling multiple DAOs to produce
 * summaries, counts, and recent activity for display in the system dashboard.
 * 
 * <p>It combines data from vehicle management, maintenance, GPS tracking,
 * fuel consumption, and report modules.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 21
 */
public class DashboardDAO {

    private VehicleDAO vehicleDAO;
    private MaintenanceTaskDAO maintenanceTaskDAO;
    private VehicleActionDaoImpl vehicleActionDaoImpl;
    private FuelConsumptionDAO fuelDAO;
    private reportDAO reportDAO;

    /**
     * Constructs a new DashboardDAO with the necessary DAO dependencies.
     *
     * @param vehicleDAO the DAO for vehicle-related operations
     * @param maintenanceTaskDAO the DAO for maintenance task operations
     * @param vehicleActionDaoImpl the DAO for vehicle GPS tracking and actions
     * @param fuelDAO the DAO for fuel consumption operations
     * @param reportDAO the DAO for report-related operations
     */
    public DashboardDAO(VehicleDAO vehicleDAO, 
                        MaintenanceTaskDAO maintenanceTaskDAO, 
                        VehicleActionDaoImpl vehicleActionDaoImpl,
                        FuelConsumptionDAO fuelDAO, 
                        reportDAO reportDAO) {
        this.vehicleDAO = vehicleDAO;
        this.maintenanceTaskDAO = maintenanceTaskDAO;
        this.vehicleActionDaoImpl = vehicleActionDaoImpl;
        this.fuelDAO = fuelDAO;
        this.reportDAO = reportDAO;
    }

    /**
     * Retrieves a count of vehicles grouped by vehicle type.
     *
     * @return a map of vehicle type names to their counts
     */
    public Map<String, Integer> getVehicleTypeCounts() {
        Map<String, Integer> counts = new HashMap<>();
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        for (Vehicle v : vehicleList) {
            String type = v.getVehicleType().getTypeName();
            counts.put(type, counts.getOrDefault(type, 0) + 1);
        }
        return counts;
    }

    /**
     * Retrieves the most recently added vehicle from the list.
     *
     * @return the last added Vehicle, or null if none exist
     */
    public Vehicle getLastAddedVehicle() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        return vehicles.isEmpty() ? null : vehicles.get(vehicles.size() - 1);
    }

    /**
     * Retrieves the count of high-priority maintenance tasks.
     *
     * @return the number of high-priority maintenance tasks
     * @throws SQLException if a database access error occurs
     */
    public int getHighPriorityTaskCount() throws SQLException {
        return maintenanceTaskDAO.getHighPriorityTaskCount();
    }

    /**
     * Retrieves the most recently created maintenance task.
     *
     * @return the latest MaintenanceTask object
     * @throws SQLException if a database access error occurs
     */
    public MaintenanceTask getMostRecentTask() throws SQLException {
        return maintenanceTaskDAO.getMostRecentTask();
    }

    /**
     * Retrieves the count of vehicles currently running (active GPS tracking).
     *
     * @return the number of running vehicles
     * @throws SQLException if a database access error occurs
     */
    public int getRunningVehiclesCount() throws SQLException {
        return vehicleActionDaoImpl.getRunningVehiclesCount();
    }

    /**
     * Retrieves the most recent vehicle actions up to the specified limit.
     *
     * @param limit the maximum number of records to return
     * @return a list of recent VehicleActionDTO records
     * @throws SQLException if a database access error occurs
     */
    public List<VehicleActionDTO> getRecentVehicleActions(int limit) throws SQLException {
        return vehicleActionDaoImpl.getRecentVehicleActions(limit);
    }

    /**
     * Retrieves the number of vehicles with a "Critical" fuel status.
     *
     * @return the count of critically fueled vehicles
     * @throws SQLException if a database access error occurs
     */
    public int getCriticalFuelCount() throws SQLException {
        return fuelDAO.getCriticalFuelCount();
    }

    /**
     * Retrieves the most recent fuel consumption records up to the specified limit.
     *
     * @param limit the maximum number of records to retrieve
     * @return a list of recent FuelConsumption records
     * @throws SQLException if a database access error occurs
     */
    public List<FuelConsumption> getRecentFuelRecords(int limit) throws SQLException {
        return fuelDAO.getRecentFuelRecords(limit);
    }

    /**
     * Retrieves a count of reports grouped by report type.
     *
     * @return a map of report type names to their counts
     * @throws SQLException if a database access error occurs
     */
    public Map<String, Integer> getReportTypeCounts() throws SQLException {
        return reportDAO.getReportTypeCounts();
    }
}
