package model.VehicleManagement;

public class FuelType {
    private int fuelTypeID;
    private String typeName;

    public FuelType() {}

    public FuelType(int fuelTypeID, String typeName) {
        this.fuelTypeID = fuelTypeID;
        this.typeName = typeName;
    }

    public int getFuelTypeID() {
        return fuelTypeID;
    }

    public void setFuelTypeID(int fuelTypeID) {
        this.fuelTypeID = fuelTypeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
