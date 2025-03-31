/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package module.GPS_Tracking.operators;

import module.GPS_Tracking.vehicles.VehicleAction;

/**
 *
 * @author silve
 */

//以操作员的行为为主体的interface
public interface OperatorAction {
    
    //驾驶一辆车
    void runVehicle(VehicleAction vehicle);
    
    //暂停一辆车
    void stopVehicle(VehicleAction vehicle);
}
