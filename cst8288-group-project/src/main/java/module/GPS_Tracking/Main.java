/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking;

/**
 *
 * @author silve
 */
import data.VehicleActionDao;
import data.VehicleActionDaoImpl;
import data.VehicleActionDTO;
import model.VehicleManagement.Vehicle;
import module.GPS_Tracking.PositionListenerImpl;
import module.GPS_Tracking.RunningStateImpl;
import module.GPS_Tracking.operators.OperatorActionImpl;
import module.GPS_Tracking.vehicles.VehicleActionImpl;

public class Main {

    public static void main(String[] args) {
        // 1. 创建车辆实体
        Vehicle vehicle = new Vehicle(); // 你可以根据真实类结构来改
        VehicleActionImpl vehicleAction = new VehicleActionImpl(vehicle);
        vehicle.setVehicleID(1); // 假设 ID 是 1

        // 2. 添加监听器
        vehicleAction.addPositionChangeListener(new PositionListenerImpl());
        vehicleAction.addRunningStateListener(new RunningStateImpl());

        // 3. 创建 operator，开车！
        OperatorActionImpl operator = new OperatorActionImpl();
        operator.runVehicle(vehicleAction);

        // 4. 模拟行驶过程（直到到达终点）
        int roadNumber = 1;
        while (!vehicleAction.isArrived(vehicleAction.vehicleMovedDistance(roadNumber), roadNumber)) {
            try {
                Thread.sleep(1000); // 模拟 1 秒一段距离
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        operator.stopVehicle(vehicleAction); // 手动停一下（其实上面 isArrived 也会停）

        // 5. 查询数据库内容
        VehicleActionDao dao = new VehicleActionDaoImpl();
        VehicleActionDTO dto = dao.getVehicleLogs(1);
        if (dto != null) {
            System.out.println("----- 数据库记录 -----");
            System.out.println("车辆ID: " + dto.getVehicleID());
            System.out.println("行驶距离: " + dto.getCarDistance());
            System.out.println("起始时间: " + dto.getLeavingTime());
            System.out.println("到达时间: " + dto.getArriveTime());
            System.out.println("当前时间（UI查询）: " + dto.getCurrentTime());
        } else {
            System.out.println("未查询到车辆记录！");
        }
    }
}

