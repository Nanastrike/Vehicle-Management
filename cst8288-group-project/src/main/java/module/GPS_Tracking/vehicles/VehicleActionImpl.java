/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking.vehicles;

import data.VehicleActionDTO;
import data.VehicleActionDao;
import data.VehicleActionDaoImpl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import model.VehicleManagement.Vehicle;
import module.GPS_Tracking.PositionChangeListener;
import module.GPS_Tracking.Route;
import module.GPS_Tracking.RunningStateListener;

/**
 * 核心文件
 * @author silve
 */
public class VehicleActionImpl implements VehicleAction {

    private Vehicle vehicle;
    private double carDistance; //the distance the car has run.
    private int vehicleID;
    private List<PositionChangeListener> listeners = new ArrayList<>();
    private boolean isRunning;
    private List<RunningStateListener> runningListeners = new ArrayList<>();
    private LocalTime leavingTime;
    private LocalTime arriveTime;
    private VehicleActionDao vehicleDao = new VehicleActionDaoImpl(); // 用于操作数据库

    public VehicleActionImpl(Vehicle vehicle) {
        super();
        this.vehicle = vehicle;
        this.vehicleID = vehicle.getVehicleID();
    }

    public VehicleActionImpl() {
        super();
    }

    /**
     * mark the arrive time when the vehicle is arrived only once
     * and set the isArrived status as true
     * @param carDistance :the distance the car has run.
     * @param roadNumber
     * @return true: vehicle is arrived false: vehicle is not arrived yet
     */
    @Override
    public boolean isArrived(double carDistance, int roadNumber) {
        boolean arrived = carDistance >= Route.getRoadDistance(roadNumber);
        if (arrived && this.arriveTime == null) {
            this.arriveTime = LocalTime.now();
        }
        return arrived;
    }

    
    /**
     * set leavingTime for only once
     */
    public void setLeavingTimeIfAbsent() {
        if (this.leavingTime == null) {//设置 leavingTime（只设置一次）
            this.leavingTime = LocalTime.now();
        }
    }

    /**
     * calculate the vehicle's moved distance
     * @param roadNumber
     * @return the vehicle's moved distance the default distance is between 0.5
     * and 5 each instance has its own carDistance
     */
    @Override
    public double vehicleMovedDistance(int roadNumber) {
        if (carDistance >= Route.getRoadDistance(roadNumber)) {
            setRunning(false); // 如果超过道路长度，判断车辆已停下
            if (arriveTime == null) {
                arriveTime = LocalTime.now(); // 只设置一次
            }
        }

        double i = 0.5 + Math.random() * (5.0 - 0.5);
        BigDecimal rounded = new BigDecimal(i).setScale(2, RoundingMode.HALF_UP);
        carDistance += rounded.doubleValue(); //只保留两位小数

        // 设置 leavingTime（如果还没设置）
        if (leavingTime == null) {
            leavingTime = LocalTime.now();
        }

        // 数据库插入记录
        if (vehicleDao != null) {
            VehicleActionDTO dto = new VehicleActionDTO();
            dto.setVehicleID(this.vehicleID);
            dto.setCarDistance(this.carDistance);
            dto.setLeavingTime(leavingTime);  // 不再是 now()
            dto.setArriveTime(arriveTime);    // 如果为 null 数据库也能接受
            try {
                vehicleDao.insertDistanceLog(dto);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return carDistance;
    }

    /**
     * when the car distance changes, it will update to listener
     *
     * @param newCarDistance
     * @return new car moved distance
     */
    @Override
    public double updatePosition(double newCarDistance) {
        this.carDistance = newCarDistance;
        for (PositionChangeListener listener : listeners) {
            listener.onPositionChanged(vehicleID, newCarDistance);
        }

        // 写入数据库
        VehicleActionDTO dto = new VehicleActionDTO();
        dto.setVehicleID(vehicleID);
        dto.setCarDistance(carDistance);
        dto.setLeavingTime(leavingTime); // 使用固定 leavingTime
        dto.setArriveTime(arriveTime);   // 可能为 null，没关系
        try {
            vehicleDao.insertDistanceLog(dto);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.carDistance;
    }

    //增加一个监听车辆距离的listener
    public void addPositionChangeListener(PositionChangeListener listener) {
        listeners.add(listener);
    }

    //增加一个监听车辆是否还在行驶
    public void addRunningStateListener(RunningStateListener listener) {
        runningListeners.add(listener);
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void setRunning(boolean running) {
        this.isRunning = running;
        // 通知所有监听器：运行状态改变了
        for (RunningStateListener listener : runningListeners) {
            listener.onRunningStateChanged(vehicleID, running);
        }
    }
}
