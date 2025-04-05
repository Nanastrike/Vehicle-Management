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
 * @description: Defines a listener for detecting and responding to vehicle position updates.
 * Interface for listening to vehicle position changes.
 * Classes implementing this interface will be notified when a vehicle's position changes.
 */
public interface PositionChangeListener {
    /**
     * Called when the position of a specific vehicle changes.
     *
     * @param vehicleId    the ID of the vehicle whose position has changed
     * @param newPosition  the updated position value of the vehicle
     */
    void onPositionChanged(int vehicleId, double newPosition);
}
