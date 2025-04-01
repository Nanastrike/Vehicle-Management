/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking.vehicles;

import data.VehicleActionDTO;
import data.VehicleActionDao;
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
    private VehicleActionDao vehicleActionDao; 

    public VehicleActionImpl(Vehicle vehicle, VehicleActionDao vehicleActionDao) {
        super();
        this.vehicle = vehicle;
        this.vehicleActionDao = vehicleActionDao;
    }

    public VehicleActionImpl() {
        super();
    }

    /**
     * @param carDistance :the distance the car has run.
     * @param roadNumber
     * @return true: vehicle is arrived false: vehicle is not arrived yet
     */
    @Override
    public boolean isArrived(double carDistance, int roadNumber) {
        return carDistance >= Route.getRoadDistance(roadNumber);
    }

    /**
     * calculate the vehicle's moved distance
     *
     * @param roadNumber
     * @return the vehicle's moved distance the default distance is between 0.5
     * and 5 each instance has its own carDistance
     */
    @Override
    public double vehicleMovedDistance(int roadNumber) {
        if (carDistance >= Route.getRoadDistance(roadNumber)) {
            setRunning(false); // 如果超过道路长度，判断车辆已停下
        }
        double i = 0.5 + Math.random() * (5.0 - 0.5);
        BigDecimal rounded = new BigDecimal(i).setScale(2, RoundingMode.HALF_UP);
        carDistance += rounded.doubleValue(); //只保留两位小数
        
        if (vehicleActionDao != null) {
        VehicleActionDTO dto = new VehicleActionDTO();
        dto.setVehicleID(this.vehicleID);     
        dto.setCarDistance(this.carDistance); // 这里你要跟 Position / CarDistance 对上
        dto.setLeavingTime(LocalTime.now()); //这里不应该是now
        dto.setArriveTime(LocalTime.now()); 


        try {
            vehicleActionDao.insertDistanceLog(dto);
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
