/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking.operators;

import module.GPS_Tracking.vehicles.VehicleAction;
import module.GPS_Tracking.vehicles.VehicleActionImpl;

/**
 *
 * @author silve
 */
public class OperatorActionImpl implements OperatorAction {

    @Override
    public void runVehicle(VehicleAction vehicle) {
        vehicle.setRunning(true);
        
        if (vehicle instanceof VehicleActionImpl impl) {
        impl.setLeavingTimeIfAbsent(); // 设置 leavingTime（只一次）
    }
    }

    @Override
    public void stopVehicle(VehicleAction vehicle) {
        vehicle.setRunning(false);
    }

}
