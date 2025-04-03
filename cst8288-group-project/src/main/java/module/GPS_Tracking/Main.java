/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking;

import data.DatabaseConnection;
import data.VehicleActionDao;
import data.VehicleActionDaoImpl;
import data.VehicleActionDTO;
import java.sql.Connection;
import model.VehicleManagement.Vehicle;
import module.GPS_Tracking.PositionListenerImpl;
import module.GPS_Tracking.RunningStateImpl;
import module.GPS_Tracking.operators.OperatorActionImpl;
import module.GPS_Tracking.vehicles.VehicleActionImpl;

public class Main {

    public static void main(String[] args) {
        // 连接数据库
        Connection conn = DatabaseConnection.getInstance().getConnection();

        // 创建车辆并设置 vehicleID
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleID(1); // 可改为任意不重复 ID
        VehicleActionImpl vehicleAction = new VehicleActionImpl(vehicle);

        // 设置数据库 DAO，并传入 vehicleAction 中
        VehicleActionDao dao = new VehicleActionDaoImpl(conn);
        vehicleAction.setDao(dao);  // 你刚刚已经加了这个方法，完美！

        // 添加监听器（用于打印输出）
        vehicleAction.addPositionChangeListener(new PositionListenerImpl());
        vehicleAction.addRunningStateListener(new RunningStateImpl());

        // 操作员启动车辆
        OperatorActionImpl operator = new OperatorActionImpl();
        operator.runVehicle(vehicleAction);

        // 模拟车辆不断前进直到到达终点
        int roadNumber = 1;
        while (!vehicleAction.isArrived(vehicleAction.vehicleMovedDistance(roadNumber), roadNumber)) {
            try {
                Thread.sleep(1000); // 模拟每秒移动一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 停车
        operator.stopVehicle(vehicleAction);

        // 从数据库查询该车辆的最后记录并输出
        VehicleActionDTO dto = dao.getVehicleLogs(vehicle.getVehicleID());
        if (dto != null) {
            System.out.println("\n----- logs -----");
            System.out.println("vehicle ID: " + dto.getVehicleID());
            System.out.println("positon: " + dto.getCarDistance());
            System.out.println("leaving time: " + dto.getLeavingTime());
            System.out.println("arrive time: " + dto.getArriveTime());
            System.out.println("current time: " + dto.getCurrentTime());
        } else {
            System.out.println("no record of this vehicle！");
        }

        // 可选：关闭连接（如果你不再需要它）
        // try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
    }
}
