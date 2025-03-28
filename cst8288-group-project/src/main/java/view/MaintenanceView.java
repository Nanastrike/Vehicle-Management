package view;

import model.MaintenanceAlert;
import java.util.List;

public class MaintenanceView {
    public void displayAlert(MaintenanceAlert alert) {
        System.out.println("=== 維護警報通知 ===");
        System.out.println("時間: " + alert.getTimestamp());
        System.out.println("車輛ID: " + alert.getVehicleId());
        System.out.println("組件類型: " + alert.getComponentType());
        System.out.println("磨損程度: " + alert.getWearLevel() + "%");
        System.out.println("警報訊息: " + alert.getAlertMessage());
        System.out.println("==================");
    }
    
    public void displayAllAlerts(List<MaintenanceAlert> alerts) {
        if (alerts.isEmpty()) {
            System.out.println("目前沒有維護警報");
            return;
        }
        
        System.out.println("=== 所有維護警報 ===");
        for (MaintenanceAlert alert : alerts) {
            displayAlert(alert);
        }
        System.out.println("==================");
    }
    
    public void displayMaintenanceMenu() {
        System.out.println("\n=== 維護系統選單 ===");
        System.out.println("1. 檢查機械組件");
        System.out.println("2. 檢查電氣組件");
        System.out.println("3. 檢查引擎診斷");
        System.out.println("4. 顯示所有警報");
        System.out.println("5. 清除所有警報");
        System.out.println("6. 退出");
        System.out.println("請選擇操作 (1-6): ");
    }
} 