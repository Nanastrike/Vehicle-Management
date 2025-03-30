/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package module.GPS.Tracking;

/**
 *
 * @author silve
 */
public interface PositionChangeListener {
    //监听坐标变化
    void onPositionChanged(int vehicleId, double newPosition);
}
