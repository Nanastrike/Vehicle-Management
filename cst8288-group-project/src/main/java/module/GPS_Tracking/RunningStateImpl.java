/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking;

/**
 * @author Qinyu Luo
 * @version 1.0
 * @since  2025/04/05
 * 
 * course CST8288
 * assignment group project
 * 
 * description Implements the PositionChangeListener interface to handle position updates.
 * This class simply logs the vehicle's new position to the console when a change occurs.
 * Implements the RunningStateListener interface to handle vehicle running state changes.
 * This class logs a message to the console whenever a vehicle starts or stops.
 */
public class RunningStateImpl implements RunningStateListener {

    /**
     * Called when the running state of a vehicle changes.
     * Logs whether the vehicle is now running or stopped.
     *
     * @param vehicleId  the ID of the vehicle whose state has changed
     * @param isRunning  true if the vehicle is running, false if it is stopped
     */
    @Override
    public void onRunningStateChanged(int vehicleId, boolean isRunning) {
        System.out.println("car id: " + vehicleId + " has become:" + (isRunning ? "running" : "stopped"));
    }

}
