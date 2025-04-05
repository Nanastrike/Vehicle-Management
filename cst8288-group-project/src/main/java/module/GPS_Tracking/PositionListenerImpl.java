/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking;

/**
 * @author :Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: group project
 * @time: 2025/04/05
 * @description: Implements the PositionChangeListener interface to handle position updates.
 * This class simply logs the vehicle's new position to the console when a change occurs.
 */
public class PositionListenerImpl implements PositionChangeListener {

    /**
     * Called when the position of a vehicle changes.
     * Logs the new position to the system console.
     *
     * @param vehicleId   the ID of the vehicle whose position changed
     * @param newPosition the updated position of the vehicle
     */
    @Override
    public void onPositionChanged(int vehicleId, double newPosition) {
        System.out.println("car id: " + vehicleId + "'s new position is :" + newPosition);
    }

    

}
