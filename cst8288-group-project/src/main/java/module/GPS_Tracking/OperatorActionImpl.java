/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking;

/**
 *
 * @author silve
 */
public class OperatorActionImpl implements OperatorAction {

    @Override
    public void runVehicle(VehicleAction vehicle) {
        vehicle.setRunning(true);
    }

    @Override
    public void stopVehicle(VehicleAction vehicle) {
        vehicle.setRunning(false);
    }

}
