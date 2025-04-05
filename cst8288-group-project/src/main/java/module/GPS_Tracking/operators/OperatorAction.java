/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package module.GPS_Tracking.operators;

import module.GPS_Tracking.vehicles.VehicleAction;

/**
 * Defines an interface representing actions performed by an operator.
 * This includes starting and stopping a vehicle through an associated vehicle action object.
 * 
 * @author Qinyu Luo
 * @version 1.0
 * @since  2025/04/05
 * 
 * course CST8288
 * assignment group project
 * 
 * Description: Represents operator-initiated actions such as running or stopping a vehicle.
 */

public interface OperatorAction {

    /**
     * Starts or resumes driving a given vehicle.
     *
     * @param vehicle the vehicle to be driven
     */
    void runVehicle(VehicleAction vehicle);

    /**
     * Pauses or stops the given vehicle.
     *
     * @param vehicle the vehicle to be stopped
     */
    void stopVehicle(VehicleAction vehicle);
}
