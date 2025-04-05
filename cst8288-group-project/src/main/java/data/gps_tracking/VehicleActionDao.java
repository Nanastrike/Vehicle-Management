/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package data.gps_tracking;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object (DAO) interface for managing vehicle tracking logs.
 * Provides CRUD operations and utility queries for handling vehicle movement
 * data, including distance, timestamps, and operator-related logs.
 *
 * @author : Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: Group Project
 * @time: 2025/04/05
 * @Description: Defines database interaction methods for vehicle GPS tracking
 * records, based primarily on vehicle ID.
 */
public interface VehicleActionDao {

    /**
     * Retrieves the latest movement record for all vehicles (e.g., last known
     * state).
     *
     * @return a list of the most recent VehicleActionDTO entries per vehicle
     */
    List<VehicleActionDTO> getAllVehicleLogs();

    /**
     * Retrieves all movement logs for a specific vehicle.
     *
     * @param vehicleID the vehicle's unique ID
     * @return a list of VehicleActionDTO records associated with the vehicle
     */
    List<VehicleActionDTO> getAllLogsByVehicleID(int vehicleID);

    /**
     * Retrieves the most recent movement log of a vehicle.
     *
     * @param vehicleID the vehicle's ID
     * @return the latest VehicleActionDTO record, or null if not found
     */
    VehicleActionDTO getVehicleLogs(int vehicleID);

    /**
     * Inserts a new vehicle distance tracking log into the database.
     *
     * @param vehicle the log object containing tracking data
     * @throws SQLException if the database operation fails
     */
    void insertDistanceLog(VehicleActionDTO vehicle) throws SQLException;

    /**
     * Deletes all movement logs for a given vehicle.
     *
     * @param vehicleID the vehicle's ID
     */
    void deleteVehicleLogs(int vehicleID);

    /**
     * Updates an existing vehicle tracking record in the database.
     *
     * @param vehicle the updated vehicle action object
     */
    void updateVehicleLogs(VehicleActionDTO vehicle);

    /**
     * Checks if the vehicle already has a leavingTime in the database. If it
     * exists, use it; if not, generate and store it once.
     *
     * @param vehicleID the vehicle's ID
     * @return the leaving time recorded in the database (if any)
     */
    LocalDateTime getLeavingTimeFromDB(int vehicleID);

    /**
     * Returns the number of vehicles that are currently marked as "running".
     *
     * @return count of vehicles currently in operation
     * @throws SQLException if the query fails
     */
    int getRunningVehiclesCount() throws SQLException;

    /**
     * Retrieves a limited number of the most recent vehicle tracking logs.
     *
     * @param limit the maximum number of records to return
     * @return a list of recent VehicleActionDTO entries
     * @throws SQLException if the query fails
     */
    List<VehicleActionDTO> getRecentVehicleActions(int limit) throws SQLException;

    /**
     * Calculates the efficiency (e.g., distance per trip or session) for each
     * operator.
     *
     * @return a map of operator names to efficiency values
     * @throws SQLException if the calculation query fails
     */
    Map<String, Double> calculateOperatorEfficiency() throws SQLException;
}
