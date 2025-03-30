/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS.Tracking;

/**
 *
 * @author silve
 */
public class RunningStateImpl implements RunningStateListener {

    @Override
    public void onRunningStateChanged(int vehicleId, boolean isRunning) {
        System.out.println("car id: " + vehicleId + " has become:" + (isRunning ? "running" : "stopped"));
    }

}
