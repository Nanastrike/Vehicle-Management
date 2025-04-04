/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_observer;

/**
 *
 * @author xiaox
 */
public class AlertService implements Observer {
    private String managerName; // e.g. so we can identify which manager is receiving alerts

    public AlertService(String managerName) {
        this.managerName = managerName;
    }

    @Override
    public void update(double consumption) {
        System.out.println("Alert for " + managerName 
            + ": Consumption exceeded threshold! Current = " + consumption);
    }
}

