-- Database: PTFMS (Public Transit Fleet Management System)
CREATE DATABASE IF NOT EXISTS PTFMS;
USE PTFMS;

-- Drop tables if they already exist to avoid conflicts
DROP TABLE IF EXISTS Operator_Status;
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
    MaxPassengers INT NOT NULL,
    RouteID INT,
    LastMaintenanceDate DATE,
    FOREIGN KEY (VehicleTypeID) REFERENCES VehicleTypes(VehicleTypeID) ON DELETE CASCADE,
    FOREIGN KEY (FuelTypeID) REFERENCES FuelTypes(FuelTypeID) ON DELETE CASCADE,
    FOREIGN KEY (RouteID) REFERENCES Routes(RouteID) ON DELETE CASCADE
);


-- GPS Tracking Table (For Real-Time Tracking)
CREATE TABLE GPS_Tracking (
    TrackingID INT AUTO_INCREMENT PRIMARY KEY,
    VehicleID INT NOT NULL,
    Position DECIMAL(10,6) NOT NULL,
    Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID) ON DELETE CASCADE
);

-- Fuel Consumption Table (For Monitoring Usage)
CREATE TABLE Fuel_Consumption (
    ConsumptionID INT AUTO_INCREMENT PRIMARY KEY,
    VehicleID INT NOT NULL,
    FuelTypeID INT NOT NULL,
    FuelUsed FLOAT NOT NULL,
    DistanceTraveled FLOAT NOT NULL,
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
CREATE TABLE Operator_Status (
    StatusEntryID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    VehicleID INT NOT NULL,
    StatusID INT NOT NULL,
    StartTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    EndTime DATETIME NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID) ON DELETE CASCADE,
    FOREIGN KEY (StatusID) REFERENCES OperatorStatusTypes(StatusID) ON DELETE CASCADE
);

-- Insert default values for lookup tables
INSERT INTO UserTypes (TypeName) VALUES ('Manager'), ('Operator');
INSERT INTO VehicleTypes (TypeName) VALUES ('Diesel Bus'), ('Electric Light Rail'), ('Diesel-Electric Train');
INSERT INTO FuelTypes (TypeName) VALUES ('Diesel'), ('CNG'), ('Electric');
INSERT INTO AlertSeverity (SeverityName) VALUES ('Low'), ('Medium'), ('High');
INSERT INTO OperatorStatusTypes (StatusName) VALUES ('Active'), ('On Break'), ('Out of Service');
