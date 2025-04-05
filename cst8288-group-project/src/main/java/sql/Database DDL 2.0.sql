-- Database: PTFMS (Public Transit Fleet Management System)
SET FOREIGN_KEY_CHECKS = 0;
USE PTFMS;

-- Drop tables if they already exist to avoid conflicts
DROP TABLE IF EXISTS Operator;
DROP TABLE IF EXISTS Maintenance_Alerts;
DROP TABLE IF EXISTS Fuel_Consumption;
DROP TABLE IF EXISTS GPS_Tracking;
DROP TABLE IF EXISTS Routes;
DROP TABLE IF EXISTS Vehicles;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS UserTypes;
DROP TABLE IF EXISTS VehicleTypes;
DROP TABLE IF EXISTS FuelTypes;
DROP TABLE IF EXISTS AlertSeverity;
DROP TABLE IF EXISTS OperatorStatusTypes;
DROP TABLE IF EXISTS Component_Status;
DROP TABLE IF EXISTS Maintenance_Tasks;

-- User Types Lookup Table
CREATE TABLE UserTypes (
    UserTypeID INT AUTO_INCREMENT PRIMARY KEY,
    TypeName VARCHAR(50) UNIQUE NOT NULL
);

-- Vehicles Types Lookup Table
CREATE TABLE VehicleTypes (
    VehicleTypeID INT AUTO_INCREMENT PRIMARY KEY,
    TypeName VARCHAR(50) UNIQUE NOT NULL
);

-- Fuel Types Lookup Table
CREATE TABLE FuelTypes (
    FuelTypeID INT AUTO_INCREMENT PRIMARY KEY,
    TypeName VARCHAR(50) UNIQUE NOT NULL
);

-- Alert Severity Lookup Table
CREATE TABLE AlertSeverity (
    SeverityID INT AUTO_INCREMENT PRIMARY KEY,
    SeverityName VARCHAR(50) UNIQUE NOT NULL
);

-- Operator Status Lookup Table
CREATE TABLE OperatorStatusTypes (
    StatusID INT AUTO_INCREMENT PRIMARY KEY,
    StatusName VARCHAR(50) UNIQUE NOT NULL
);

-- Users Table (User Registration & Authentication)
CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    UserTypeID INT NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserTypeID) REFERENCES UserTypes(UserTypeID) ON DELETE CASCADE
);

-- Routes Table (Tracking Assigned Routes)
CREATE TABLE Routes (
    RouteID INT AUTO_INCREMENT PRIMARY KEY,
    RouteName VARCHAR(100) NOT NULL,
    StartLocation VARCHAR(255) NOT NULL,
    EndLocation VARCHAR(255) NOT NULL,
    Distance FLOAT NOT NULL
);

-- Vehicles Table (Vehicle Management)
CREATE TABLE Vehicles (
    VehicleID INT AUTO_INCREMENT PRIMARY KEY,
    VehicleNumber VARCHAR(50) UNIQUE NOT NULL,
    VehicleTypeID INT NOT NULL,
    FuelTypeID INT NOT NULL,
    ConsumptionRate FLOAT NOT NULL,
    DieselRate DOUBLE DEFAULT 0.0,
    ElectricRate DOUBLE DEFAULT 0.0,
    MaxPassengers INT NOT NULL,
    RouteID INT,
    LastMaintenanceDate DATE,
    FOREIGN KEY (VehicleTypeID) REFERENCES VehicleTypes(VehicleTypeID) ON DELETE CASCADE,
    FOREIGN KEY (FuelTypeID) REFERENCES FuelTypes(FuelTypeID) ON DELETE CASCADE,
    FOREIGN KEY (RouteID) REFERENCES Routes(RouteID) ON DELETE CASCADE
);

-- GPS Tracking Table (Updated)
CREATE TABLE GPS_Tracking (
    TrackingID INT AUTO_INCREMENT PRIMARY KEY,
    VehicleID INT NOT NULL,
    Position DECIMAL(10,6) NOT NULL,
    CurrentTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    LeavingTime DATETIME,
    ArriveTime DATETIME,
    OperatorID INT,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID) ON DELETE CASCADE
);

-- Fuel Consumption Table (For Monitoring Usage)
CREATE TABLE Fuel_Consumption (
    ConsumptionID INT AUTO_INCREMENT PRIMARY KEY,
    VehicleID INT NOT NULL,
    FuelTypeID INT NOT NULL,
    FuelUsed FLOAT NOT NULL,
    DistanceTraveled FLOAT NOT NULL,
    Status VARCHAR(20) DEFAULT 'Normal',
    Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID) ON DELETE CASCADE,
    FOREIGN KEY (FuelTypeID) REFERENCES FuelTypes(FuelTypeID) ON DELETE CASCADE
);

-- Maintenance Alerts Table (For Predictive Maintenance Alerts)
CREATE TABLE Maintenance_Alerts (
    AlertID INT AUTO_INCREMENT PRIMARY KEY,
    VehicleID INT NOT NULL,
    SeverityID INT NOT NULL,
    AlertType VARCHAR(100) NOT NULL,
    AlertTimestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    Resolved BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID) ON DELETE CASCADE,
    FOREIGN KEY (SeverityID) REFERENCES AlertSeverity(SeverityID) ON DELETE CASCADE
);

-- Operator Status Table (For Logging Breaks & Out-of-Service Times)
CREATE TABLE Operator (
    OperatorID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL UNIQUE,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Component Status Table (For tracking component wear)
CREATE TABLE Component_Status (
    ComponentID INT AUTO_INCREMENT PRIMARY KEY,
    VehicleID INT NOT NULL,
    ComponentName VARCHAR(100) NOT NULL,
    HoursUsed INT NOT NULL DEFAULT 0,
    WearLevel DECIMAL(5,2) NOT NULL DEFAULT 0,
    LastUpdated DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID) ON DELETE CASCADE
);

-- Maintenance Tasks Table (For scheduling maintenance)
CREATE TABLE Maintenance_Tasks (
    TaskID INT AUTO_INCREMENT PRIMARY KEY,
    VehicleID INT NOT NULL,
    ComponentID INT,
    MaintenanceDate DATETIME NOT NULL,
    Status VARCHAR(20) NOT NULL DEFAULT 'Scheduled',
    CreatedBy VARCHAR(100),
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    Priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    TaskType VARCHAR(50) NOT NULL,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID) ON DELETE CASCADE,
    FOREIGN KEY (ComponentID) REFERENCES Component_Status(ComponentID) ON DELETE SET NULL
);

-- Insert default values for lookup tables
INSERT INTO UserTypes (TypeName) VALUES ('Manager'), ('Operator');
INSERT INTO VehicleTypes (TypeName) VALUES ('Diesel Bus'), ('Electric Light Rail'), ('Diesel-Electric Train');
INSERT INTO FuelTypes (TypeName) VALUES ('Diesel'), ('CNG'), ('Electric');
INSERT INTO AlertSeverity (SeverityName) VALUES ('Low'), ('Medium'), ('High');
INSERT INTO OperatorStatusTypes (StatusName) VALUES ('Active'), ('On Break'), ('Out of Service');

-- Insert initial data into Routes table
INSERT INTO Routes (RouteName, StartLocation, EndLocation, Distance) VALUES
('Downtown Loop', 'Union Station', 'Main Street Terminal', 12.5),
('Airport Express', 'City Center', 'International Airport', 25.8),
('East-West Connector', 'East Side Depot', 'West Hills Terminal', 18.2);

INSERT INTO Vehicles (
    VehicleID, VehicleNumber, VehicleTypeID, FuelTypeID, 
    ConsumptionRate, MaxPassengers, RouteID, LastMaintenanceDate
) VALUES (
    1, 'BUS-001', 1, 1, 5.0, 40, 1, '2024-01-01'
);

INSERT INTO Fuel_Consumption (VehicleID, FuelTypeID, FuelUsed, DistanceTraveled,Status)
VALUES (1, 1, 10.5, 100, 'Normal');


SET FOREIGN_KEY_CHECKS = 1;

