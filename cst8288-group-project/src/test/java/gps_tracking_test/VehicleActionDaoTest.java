/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gps_tracking_test;

import data.gps_tracking.VehicleActionDaoImpl;
import data.gps_tracking.VehicleActionDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit test for VehicleActionDaoImpl.
 * 
 * This test ensures that getVehicleLogs() correctly returns null
 * when there is no active (non-arrived) record for the given vehicle ID.
 *
 * This is important for distinguishing whether a new record should be inserted.
 * It covers an edge case where a vehicle has never been used or has already arrived.
 * 
 * @author : Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: Group Project
 * @time: 2025/04/05 
 * @Description: Validates null response when querying logs for a non-existent vehicle.
 */
public class VehicleActionDaoTest {

    @Test
    public void testGetVehicleLogsReturnsNullIfNoRecord() {
        VehicleActionDaoImpl dao = new VehicleActionDaoImpl();
        int nonExistentVehicleId = 9999; // 确保这个 ID 在测试库中不存在

        VehicleActionDTO result = dao.getVehicleLogs(nonExistentVehicleId);
        assertNull(result, "Expected null when no log exists for the vehicle ID.");
    }
}
