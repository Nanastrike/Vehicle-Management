/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking.vehicles;

import data.gps_tracking.RouteDao;
import data.gps_tracking.RouteDaoImpl;
import data.gps_tracking.VehicleActionDTO;
import data.gps_tracking.VehicleActionDao;
import data.gps_tracking.VehicleActionDaoImpl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.VehicleManagement.Vehicle;
import module.GPS_Tracking.PositionChangeListener;
import module.GPS_Tracking.RunningStateListener;

/**
 * 记录车辆的出发、行径距离、状态、监听器
 *
 * @author silve
 */
public class VehicleActionImpl implements VehicleAction {

    private Vehicle vehicle;
    private double carDistance; //the distance the car has run.
    private int vehicleID;
    private List<PositionChangeListener> listeners = new ArrayList<>();
    private boolean isRunning;
    private List<RunningStateListener> runningListeners = new ArrayList<>();
    private LocalDateTime leavingTime;
    private LocalDateTime arriveTime;
    private VehicleActionDao vehicleDao = new VehicleActionDaoImpl(); // 用于操作数据库
    private final RouteDao routeDao = new RouteDaoImpl(); //用于连接route表
    private int operatorID;

    public VehicleActionImpl(Vehicle vehicle) {
        super();
        this.vehicle = vehicle;
        this.vehicleID = vehicle.getVehicleID();
    }

    public VehicleActionImpl() {
        super();
    }

    /**
     * 设置负责数据库连接的class
     *
     * @param dao
     */
    public void setDao(VehicleActionDao dao) {
        this.vehicleDao = dao;
    }

    /**
     * mark the arrive time when the vehicle is arrived only once and set the
     * isArrived status as true
     *
     * @param carDistance :the distance the car has run.
     * @param roadNumber
     * @return true: vehicle is arrived false: vehicle is not arrived yet
     */
    @Override
    public boolean isArrived(double carDistance, int roadNumber) {
        double distanceLimit = routeDao.getRoadDistanceByRouteID(roadNumber);
        return carDistance >= distanceLimit;
    }

    /**
     * set leavingTime for only once
     */
    public void setLeavingTimeIfAbsent() {
        if (this.leavingTime == null) {//设置 leavingTime（只设置一次）
            this.leavingTime = LocalDateTime.now();
        }
    }

    /**
     * calculate the vehicle's moved distance leavingTime will only be created
     * and written to db once arriveTime will only be written when the car is
     * arrived
     *
     * @param roadNumber
     * @return the vehicle's moved distance the default distance is between 0.5
     * and 5 each instance has its own carDistance
     */
    @Override
    public double vehicleMovedDistance(int roadNumber, int operatorID) {
        this.operatorID = operatorID;
        //计算每次行径距离
        double i = 0.5 + Math.random() * (5.0 - 0.5); //每次行径距离在0-5之间
        BigDecimal rounded = new BigDecimal(i).setScale(2, RoundingMode.HALF_UP);
        carDistance += rounded.doubleValue(); //只保留两位小数

        //计算已有距离是否超过总长度
        if (isArrived(carDistance, roadNumber)) {
            setRunning(false); // 如果超过道路长度，判断车辆已停下
            if (arriveTime == null) {
                this.arriveTime = LocalDateTime.now(); // 只设置一次
            }
        }

//        LocalDateTime leavingTimeFromDB = null;
        VehicleActionDao dao = new VehicleActionDaoImpl();
        VehicleActionDTO log = dao.getVehicleLogs(vehicleID); // 查有没有旧记录

        if (log == null) {
            // 没有记录，说明是第一次 => 插入
            VehicleActionDTO newLog = new VehicleActionDTO();
            newLog.setVehicleID(this.vehicleID);
            newLog.setCarDistance(this.carDistance);

            // leavingTime 只设一次（如果你还没有从数据库查它）
            if (this.leavingTime == null) {
                this.leavingTime = LocalDateTime.now();
            }
            newLog.setLeavingTime(this.leavingTime);
            newLog.setArriveTime(this.arriveTime);
            newLog.setOperatorID(this.operatorID);

            try {
                dao.insertDistanceLog(newLog);
            } catch (SQLException ex) {
                Logger.getLogger(VehicleActionImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            // 有记录 => 更新
            log.setCarDistance(this.carDistance);
            log.setOperatorID(this.operatorID); 

            if (this.arriveTime != null && log.getArriveTime() == null) {
                log.setArriveTime(this.arriveTime);
            }

            dao.updateVehicleLogs(log);
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
    
    public void setOperatorID(int operatorID) {
    this.operatorID = operatorID;
}
}
