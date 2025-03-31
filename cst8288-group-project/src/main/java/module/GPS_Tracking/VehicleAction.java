/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package module.GPS_Tracking;

/**
 *
 * @author silve
 */
//以车为主体的行为interface
public interface VehicleAction {
    
    //根据行驶距离和道路id去判断是否已到站
    boolean isArrived(double carDistance, int roadNumber);
    
    //车辆行驶距离，内置一个math.random();
    double vehicleMovedDistance(int roadNumber);
    
    //发送坐标id
    double updatePosition(double position);
    
    //车的状态，行驶中还是停止中？已到站是另一个method
    void setRunning(boolean running);

}
