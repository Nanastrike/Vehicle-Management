package model.MaintenanceTask;

import java.util.HashMap;
import java.util.Map;

public class ComponentMonitoringParams {
    private final String vehicleId;
    private final ComponentType componentType;
    private final Map<String, Double> parameters;
    
    public ComponentMonitoringParams(String vehicleId, ComponentType componentType) {
        this.vehicleId = vehicleId;
        this.componentType = componentType;
        this.parameters = new HashMap<>();
    }
    
    public void addParameter(String name, double value) {
        parameters.put(name, value);
    }
    
    public String getVehicleId() {
        return vehicleId;
    }
    
    public ComponentType getComponentType() {
        return componentType;
    }
    
    public Map<String, Double> getParameters() {
        return new HashMap<>(parameters);
    }
    
    public Double getParameter(String name) {
        return parameters.get(name);
    }
} 