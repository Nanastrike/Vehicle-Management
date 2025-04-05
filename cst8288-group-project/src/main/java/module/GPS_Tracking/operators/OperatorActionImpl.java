/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking.operators;

import module.GPS_Tracking.vehicles.VehicleAction;
import module.GPS_Tracking.vehicles.VehicleActionImpl;

/**
 * Implements the OperatorAction interface to perform real-world operator
 * actions. This class triggers changes in the vehicle's running state, and sets
 * the vehicle's leaving time if applicable when a vehicle starts moving.
 *
 * @author :Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: group project
 * @time: 2025/04/05
 * Description: Concrete implementation of operator actions
 * such as starting and stopping a vehicle.
 */
public class OperatorActionImpl implements OperatorAction {

    /**
     * Starts the vehicle by setting its running state to true. If the vehicle
     * is an instance of VehicleActionImpl, it sets the leaving time (only
     * once).
     *
     * @param vehicle the vehicle to be started
     */
    @Override
    public void runVehicle(VehicleAction vehicle) {
        vehicle.setRunning(true);

        // Set leavingTime only if the implementation supports it
        if (vehicle instanceof VehicleActionImpl impl) {
            impl.setLeavingTimeIfAbsent(); // Sets leavingTime only once
        }
    }

    /**
     * Stops the vehicle by setting its running state to false.
     *
     * @param vehicle the vehicle to be stopped
     */
    @Override
    public void stopVehicle(VehicleAction vehicle) {
        vehicle.setRunning(false);
    }

}
