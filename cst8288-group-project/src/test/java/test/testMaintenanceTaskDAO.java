/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import data.MaintenanceTaskDAO;
import model.MaintenanceTask.MaintenanceTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the MaintenanceTaskDAO class.
 * 
 */
public class testMaintenanceTaskDAO {

    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockRs;
    private MaintenanceTaskDAO taskDAO;

    @BeforeEach
    void setup() throws Exception {
        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        taskDAO = new MaintenanceTaskDAO(mockConn);
    }

    /**
     * Tests the creation of a maintenance task and ensures the generated key is set.
     */
    @Test
    void testCreateTask_success() throws Exception {
        MaintenanceTask task = new MaintenanceTask();
        task.setVehicleId("V001");
        task.setScheduledDate(LocalDateTime.now());
        task.setStatus("Scheduled");
        task.setCreatedBy("Admin");
        task.setPriority("High");
        task.setTaskType("Oil Change");

        when(mockConn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt(1)).thenReturn(1001);

        taskDAO.createTask(task);

        assertEquals(1001, task.getTaskId());
    }

    /**
     * Tests the update of a task's status.
     */
    @Test
    void testUpdateTaskStatus_success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> taskDAO.updateTaskStatus(1, "Completed"));
        verify(mockStmt).setString(1, "Completed");
        verify(mockStmt).setInt(2, 1);
    }

    /**
     * Tests the deletion of a task by its ID.
     */
    @Test
    void testDeleteTask_success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> taskDAO.deleteTask(1));
        verify(mockStmt).setInt(1, 1);
    }

    /**
     * Tests retrieving maintenance tasks by vehicle ID.
     */
    @Test
    void testGetTasksByVehicle_returnsTasksList() throws Exception {
        String vehicleId = "V001";

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        // Simulate one result row
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("TaskID")).thenReturn(101);
        when(mockRs.getString("VehicleID")).thenReturn(vehicleId);
        when(mockRs.getInt("ComponentID")).thenReturn(5);
        when(mockRs.getTimestamp("MaintenanceDate"))
                .thenReturn(Timestamp.valueOf(LocalDateTime.of(2025, 4, 1, 10, 0)));
        when(mockRs.getString("Status")).thenReturn("Scheduled");
        when(mockRs.getString("CreatedBy")).thenReturn("Admin");

        List<MaintenanceTask> tasks = taskDAO.getTasksByVehicle(vehicleId);

        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        MaintenanceTask task = tasks.get(0);
        assertEquals(101, task.getTaskId());
        assertEquals("V001", task.getVehicleId());
        assertEquals(5, task.getComponentId());
        assertEquals("Scheduled", task.getStatus());
        assertEquals("Admin", task.getCreatedBy());
    }
}