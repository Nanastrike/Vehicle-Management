package model.MaintenanceTask;

import data.MaintenanceTaskDAO;
import data.ComponentStatusDAO;
import data.DatabaseConnection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;

public class MaintenanceTaskManager {
    private final MaintenanceTaskDAO taskDAO;
    private final ComponentStatusDAO componentStatusDAO;
    private final VehicleComponentMonitor componentMonitor;
    
    public MaintenanceTaskManager() throws SQLException {
        this.taskDAO = new MaintenanceTaskDAO(DatabaseConnection.getInstance().getConnection());
        this.componentStatusDAO = new ComponentStatusDAO(DatabaseConnection.getInstance().getConnection());
        this.componentMonitor = new VehicleComponentMonitor();
    }
    
    public List<MaintenanceTask> getAllMaintenanceTasks() throws SQLException {
        return taskDAO.getPendingTasks();
    }
    
    public void createMaintenanceTask(int vehicleId, String taskType, LocalDateTime scheduledDate, String createdBy, String priority) throws SQLException {
        MaintenanceTask task = new MaintenanceTask();
        task.setVehicleId(String.valueOf(vehicleId));
        task.setTaskType(taskType);
        task.setScheduledDate(scheduledDate);
        task.setCreatedBy(createdBy);
        task.setStatus("Scheduled");
        task.setPriority(priority);
        
        taskDAO.createTask(task);
    }
    
    public void updateTaskStatus(int taskId, String status) throws SQLException {
        taskDAO.updateTaskStatus(taskId, status);
    }
    
    public void deleteMaintenanceTask(int taskId) throws SQLException {
        taskDAO.deleteTask(taskId);
    }
    
    public void monitorComponents(String vehicleId, String[] components, Date lastMaintenanceDate) throws SQLException {
        // 計算使用時間（從最後維修日期到現在的小時數）
        long hoursUsed = (System.currentTimeMillis() - lastMaintenanceDate.getTime()) / (60 * 60 * 1000);
        
        // 計算磨損程度
        double wearLevel = calculateWearLevel(hoursUsed);
        
        // 為每個組件創建狀態記錄
        for (String component : components) {
            ComponentStatus status = new ComponentStatus(
                vehicleId,
                component,
                (int)hoursUsed,
                wearLevel
            );
            
            // 保存到數據庫
            componentStatusDAO.createComponentStatus(status);
            
            // 通知觀察者
            componentMonitor.updateComponentStatus(status);
        }
    }
    
    private double calculateWearLevel(long hoursUsed) {
        if (hoursUsed >= 300) {
            return 80;
        } else if (hoursUsed >= 200) {
            return 60;
        } else if (hoursUsed >= 100) {
            return 40;
        } else {
            return (hoursUsed / 100.0) * 40;
        }
    }
    
    public List<ComponentStatus> getComponentStatuses(String vehicleId) throws SQLException {
        return componentStatusDAO.getComponentStatusesByVehicle(vehicleId);
    }
} 