/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package module.GPS_Tracking.vehicles;

/**
 * Interface representing behaviors and actions related to a vehicle. This
 * interface is used to control vehicle movement, status, and coordinate updates
 * during the operation process.
 *
 * @author :Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: group project
 * @time: 2025/04/05
 * Description: Defines methods for vehicle-related logic,
 * including movement, position updates, and arrival detection.
 */
public interface VehicleAction {

    /**
     * Determines whether the vehicle has arrived at its destination.
     *
     * @param carDistance the distance the vehicle has traveled
     * @param roadNumber the ID of the current route/road
     * @return true if the vehicle has arrived, false otherwise
     */
    boolean isArrived(double carDistance, int roadNumber);

    /**
     * Calculates the distance the vehicle has moved. This may use a random
     * value generator internally to simulate movement.
     *
     * @param roadNumber the ID of the current route
     * @param operatorID the ID of the assigned operator
     * @return the updated distance moved
     */
    double vehicleMovedDistance(int roadNumber, int operatorID);

    /**
     * Updates the current position of the vehicle.
     *
     * @param position the new position value
     * @return the updated position
     */
    double updatePosition(double position);

    /**
     * Sets the running state of the vehicle (true for running, false for
     * stopped).
     *
     * @param running true if the vehicle is running, false if stopped
     */
    void setRunning(boolean running);

    /**
     * Sets the ID of the operator currently controlling the vehicle.
     *
     * @param operatorID the ID of the operator
     */
    void setOperatorID(int operatorID);

}
