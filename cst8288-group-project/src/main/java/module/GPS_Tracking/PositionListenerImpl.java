/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking;

/**
 *
 * @author silve
 */
public class PositionListenerImpl implements PositionChangeListener {

    @Override
    public void onPositionChanged(int vehicleId, double newPosition) {
        System.out.println("car id: " + vehicleId + "'s new position is :" + newPosition);
    }

    

}
