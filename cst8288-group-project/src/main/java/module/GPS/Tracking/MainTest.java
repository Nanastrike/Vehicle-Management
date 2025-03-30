/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS.Tracking;

import model.VehicleManagement.Vehicle;

public class MainTest {
    public static void main(String[] args) {
        // ============= 1. 创建车辆数据对象 (Vehicle) =============
        // 这个 model.VehicleManagement.Vehicle 里你自己定义的类，
        // 假设它有一个 setId() 方法供我们设置 ID
        Vehicle v = new Vehicle();
        v.setVehicleID(1);
        
        // ============= 2. 创建车辆行为实现类 (VehicleActionImpl) =============
        // 传入 Vehicle 数据对象，这样 VehicleActionImpl 就能记录到它是谁
        VehicleActionImpl carAction = new VehicleActionImpl(v);
        
        //（可选）如果你想让 VehicleActionImpl 内部的 vehicleID 等于 Vehicle 的 ID
        // 可以在 VehicleActionImpl 构造里写： this.vehicleID = vehicle.getId();
        // 或者在这里手动:
        // carAction.setVehicleID(v.getId()); // 如果你想补充 setVehicleID 方法

        // ============= 3. 注册 "车辆运行状态" 监听器 (RunningStateListener) =============
        // 这样当车辆 run/stop 时，会自动调用它
        carAction.addRunningStateListener(new RunningStateImpl());
        
        // ============= 4. 注册 "车辆位置" 监听器 (PositionChangeListener) =============
        // 前提是你在 VehicleActionImpl 里添加了 addPositionChangeListener(...) 方法
        carAction.addPositionChangeListener(new PositionListenerImpl());
        
        // ============= 5. 创建操作员 (Operator) =============
        OperatorAction operator = new OperatorActionImpl();

        // ============= 6. 让操作员启动车辆 =============
        operator.runVehicle(carAction);
        System.out.println(">>>operator is driving a car...");

        // ============= 7. 模拟车辆在路段(roadNumber=1)上多次行驶 =============
        int roadNumber = 1;
        for (int i = 0; i < 10; i++) {
            // 如果车已经不在运行，就跳出循环
            if (!carAction.isRunning()) {
                System.out.println("car stops");
                break;
            }
            
            // 让车辆在第1条路上跑一段距离
            double newDistance = carAction.vehicleMovedDistance(roadNumber);
            System.out.println("after moved " + (i + 1) + " times,total moved distance = " + newDistance);

            // 判断是否到站
            boolean arrived = carAction.isArrived(newDistance, roadNumber);
            if (arrived) {
                System.out.println("car arrived and stops");
                // 手动调用停止（也可以在 vehicleMovedDistance 里检测超出自动停）
                operator.stopVehicle(carAction);
            }

            // 让线程稍微睡一下，模拟现实中每隔几秒行驶一次
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // ============= 8. 最终输出车辆状态 =============
        System.out.println("driving is over. for now isRunning = " + carAction.isRunning());
    }
}
