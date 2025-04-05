/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking.vehicles;

import data.gps_tracking.RouteDao;
import data.gps_tracking.RouteDaoImpl;
import data.gps_tracking.VehicleActionDTO;
import data.gps_tracking.VehicleActionDao;
import data.gps_tracking.VehicleActionDaoImpl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.VehicleManagement.Vehicle;
import module.GPS_Tracking.PositionChangeListener;
import module.GPS_Tracking.RunningStateListener;

/**
 * Implementation of the VehicleAction interface. This class manages vehicle
 * behavior, including tracking its movement, updating position, handling
 * running state, and marking arrival time.
 *
 * @author :Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: group project
 * @time: 2025/04/05
 * Description: Implements vehicle-related logic for tracking
 * distance, position updates, running state changes, and route completion.
 */
public class VehicleActionImpl implements VehicleAction {

    /**
     * The vehicle entity associated with this action class
     */
    private Vehicle vehicle;

    /**
     * The total distance the vehicle has traveled
     */
    private double carDistance;

    /**
     * The ID of the vehicle
     */
    private int vehicleID;

    /**
     * Listeners for position change events
     */
    private List<PositionChangeListener> listeners = new ArrayList<>();

    /**
     * Indicates whether the vehicle is currently running
     */
    private boolean isRunning;

    /**
     * Listeners for running state change events
     */
    private List<RunningStateListener> runningListeners = new ArrayList<>();

    /**
     * The timestamp when the vehicle starts moving
     */
    private LocalDateTime leavingTime;

    /**
     * The timestamp when the vehicle arrives at its destination
     */
    private LocalDateTime arriveTime;

    /**
     * DAO class for database operations related to vehicle tracking
     */
    private VehicleActionDao vehicleDao = new VehicleActionDaoImpl();

    /**
     * DAO class for accessing route data
     */
    private final RouteDao routeDao = new RouteDaoImpl();

    /**
     * The operator ID currently assigned to this vehicle
     */
    private int operatorID;

    /**
     * Constructs a VehicleActionImpl for a specific vehicle.
     *
     * @param vehicle the vehicle object to be managed
     */
    public VehicleActionImpl(Vehicle vehicle) {
        super();
        this.vehicle = vehicle;
        this.vehicleID = vehicle.getVehicleID();
    }

    /**
     * Default constructor.
     */
    public VehicleActionImpl() {
        super();
    }

    /**
     * Sets the DAO responsible for handling vehicle-related database
     * operations.
     *
     * @param dao the VehicleActionDao implementation
     */
    public void setDao(VehicleActionDao dao) {
        this.vehicleDao = dao;
    }

    /**
     * Checks whether the vehicle has arrived at the end of the route. If so,
     * arrival time is marked (handled externally), and "arrived" status should
     * be set.
     *
     * @param carDistance the distance the car has run
     * @param roadNumber the ID of the route
     * @return true if the vehicle has arrived, false otherwise
     */
    @Override
    public boolean isArrived(double carDistance, int roadNumber) {
        double distanceLimit = routeDao.getRoadDistanceByRouteID(roadNumber);
        System.out.println("distanceLimit: " + distanceLimit);
        System.out.println("carDistance: " + carDistance);
        return carDistance >= distanceLimit;
    }

    /**
     * Sets the leaving time if it hasn't been set already. This method ensures
     * the leavingTime is recorded only once.
     */
    public void setLeavingTimeIfAbsent() {
        if (this.leavingTime == null) { // Set leavingTime only once
            this.leavingTime = LocalDateTime.now();
        }
    }

    /**
     * Calculates the distance the vehicle has moved during its operation. -
     * Restores previously recorded distance if exists (prevents distance reset)
     * - Adds a random movement distance between 0.5 and 5.0 units - Marks
     * arriveTime when vehicle reaches destination - Only writes leavingTime and
     * arriveTime to the database once
     *
     * @param roadNumber the ID of the route
     * @param operatorID the ID of the operator
     * @return the updated total distance the vehicle has moved
     */
    @Override
    public double vehicleMovedDistance(int roadNumber, int operatorID) {
        this.operatorID = operatorID;

        VehicleActionDao dao = new VehicleActionDaoImpl();
        VehicleActionDTO log = dao.getVehicleLogs(vehicleID); // Fetch existing log record

        //  Restore previous distance (avoid reset on each request)
        if (log != null) {
            this.carDistance = log.getCarDistance();
        }

        //  Add random movement distance between 0.5 and 5.0
        double i = 0.5 + Math.random() * (5.0 - 0.5);
        BigDecimal rounded = new BigDecimal(i).setScale(2, RoundingMode.HALF_UP);
        carDistance += rounded.doubleValue();

        //  If arrived at destination, mark arrival time and stop running
        if (isArrived(carDistance, roadNumber)) {
            setRunning(false);
            if (arriveTime == null) {
                this.arriveTime = LocalDateTime.now(); // Mark only once
            }
        }

        //  First-time log insert
        if (log == null) {
            VehicleActionDTO newLog = new VehicleActionDTO();
            newLog.setVehicleID(this.vehicleID);
            newLog.setCarDistance(this.carDistance);

            // Set leaving time if not set
            if (this.leavingTime == null) {
                this.leavingTime = LocalDateTime.now();
            }

            newLog.setLeavingTime(this.leavingTime);
            newLog.setArriveTime(this.arriveTime);
            newLog.setOperatorID(this.operatorID);

            try {
                dao.insertDistanceLog(newLog);
            } catch (SQLException ex) {
                Logger.getLogger(VehicleActionImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //  Update existing log
            log.setCarDistance(this.carDistance);
            log.setOperatorID(this.operatorID);

            if (this.arriveTime != null && log.getArriveTime() == null) {
                log.setArriveTime(this.arriveTime); // Only set if not already recorded
            }

            dao.updateVehicleLogs(log);
        }

        return carDistance;
    }

    /**
     * Updates the current car distance and notifies all registered position
     * listeners. Also inserts a new distance log into the database using the
     * current car state.
     *
     * @param newCarDistance the updated car distance
     * @return the updated car distance after notifying listeners and logging
     */
    @Override
    public double updatePosition(double newCarDistance) {
        this.carDistance = newCarDistance;

        // Notify all position change listeners
        for (PositionChangeListener listener : listeners) {
            listener.onPositionChanged(vehicleID, newCarDistance);
        }

        // Write to database
        VehicleActionDTO dto = new VehicleActionDTO();
        dto.setVehicleID(vehicleID);
        dto.setCarDistance(carDistance);
        dto.setLeavingTime(leavingTime); // Use fixed leaving time
        dto.setArriveTime(arriveTime);   // Could be null — that's fine

        try {
            vehicleDao.insertDistanceLog(dto);
        } catch (SQLException e) {
        }

        return this.carDistance;
    }

    /**
     * Adds a listener to receive vehicle position change updates.
     *
     * @param listener the listener to add
     */
    public void addPositionChangeListener(PositionChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Adds a listener to receive vehicle running state change updates.
     *
     * @param listener the listener to add
     */
    public void addRunningStateListener(RunningStateListener listener) {
        runningListeners.add(listener);
    }

    /**
     * Returns whether the vehicle is currently running.
     *
     * @return true if the vehicle is running, false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Sets the running state of the vehicle and notifies all running state
     * listeners.
     *
     * @param running true to set the vehicle as running, false to stop it
     */
    @Override
    public void setRunning(boolean running) {
        this.isRunning = running;

        // Notify all running state listeners
        for (RunningStateListener listener : runningListeners) {
            listener.onRunningStateChanged(vehicleID, running);
        }
    }

    /**
     * Sets the ID of the operator currently managing this vehicle.
     *
     * @param operatorID the operator's ID
     */
    @Override
    public void setOperatorID(int operatorID) {
        this.operatorID = operatorID;
    }
}
