package model.MaintenanceTask;

import java.util.List;

public interface ComponentObserver {
    void update(List<ComponentStatus> statuses);
} 