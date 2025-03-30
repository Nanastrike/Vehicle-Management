package model.VehicleManagement;

public class VehicleType {
    private int vehicleTypeID;
    private String typeName;

    public VehicleType() {}

    public VehicleType(int vehicleTypeID, String typeName) {
        this.vehicleTypeID = vehicleTypeID;
        this.typeName = typeName;
    }

    public int getVehicleTypeID() {
        return vehicleTypeID;
    }

    public void setVehicleTypeID(int vehicleTypeID) {
        this.vehicleTypeID = vehicleTypeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
