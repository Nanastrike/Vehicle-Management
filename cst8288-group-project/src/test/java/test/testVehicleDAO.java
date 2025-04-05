package test;

import data.VehicleDAO;
import model.VehicleManagement.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class testVehicleDAO {

    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockRs;
    private VehicleDAO vehicleDAO;

    @BeforeEach
    void setup() throws Exception {
        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        vehicleDAO = new VehicleDAO(mockConn);
    }


    @Test
    void testIsVehicleNumberExists_returnsFalse() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // No match

        boolean exists = vehicleDAO.isVehicleNumberExists("UNKNOWN");
        assertFalse(exists);
    }

    @Test
    void testAddVehicle_success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1); // Simulate insert success

        VehicleType vt = new VehicleType(1, "Diesel Bus");
        FuelType ft = new FuelType(1, "Diesel");
        Vehicle vehicle = new Vehicle(0, "BUS-123", vt, ft, 10.0f, 50, 2, new Date(System.currentTimeMillis()));

        boolean added = vehicleDAO.addVehicle(vehicle);
        assertTrue(added);
    }
}
