/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package module.GPS.Tracking;

/**
 * a listener interface for the vehicle's condition
 * @author silve
 */
public interface RunningStateListener {
    void onRunningStateChanged(int vehicleId, boolean isRunning);
}
