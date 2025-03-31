/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package data;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author silve
 */
//增删改查：车辆距离/时间表相关的数据库内容，基于车辆id
public interface VehicleActionDao {
  
    /**
     * output all the vehicle records
     * @return 
     */
    List<VehicleActionDTO> getAllVehicleLogs();
    
    List<VehicleActionDTO> getVehicleLogs(int vehicleID);
    
    /**
     * 
     * @param vehicle
     * @throws SQLException 
     */
    void insertDistanceLog(VehicleActionDTO vehicle) throws SQLException;

    
    /**
     * delete the chosen vehicle records
     * @param vehicleID
     */
    void deleteVehicleLogs(int vehicleID);
    
    /**
     * change the info of chosen vehicle record
     * @param vehicleID
     */
    void updateVehicleLogs(int vehicleID);
}
