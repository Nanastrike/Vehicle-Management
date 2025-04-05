/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package module.GPS_Tracking;

/**
 * @author :Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: group project
 * @time: 2025/04/05
 * @description:Interface for detecting changes in the running state of vehicles.
 * Implementing classes can define custom behaviors for start/stop events.
 * Defines an interface for listening to vehicle running state changes.
 * Classes implementing this interface can respond when a vehicle starts or stops.
 */
public interface RunningStateListener {
    /**
     * Called when the running state of a vehicle changes.
     *
     * @param vehicleId  the ID of the vehicle whose running state changed
     * @param isRunning  true if the vehicle is running, false if it is stopped
     */
    void onRunningStateChanged(int vehicleId, boolean isRunning);
}
