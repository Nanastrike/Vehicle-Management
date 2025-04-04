/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_test;

/**
 *
 * @author xiaox
 */

import java.sql.Connection;
import Fuel_dao.DatabaseConnection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println(" Database connection test: SUCCESS");
            } else {
                System.out.println(" Database connection test: FAILED");
            }
        } catch (Exception e) {
            System.out.println(" Error during connection test:");
            e.printStackTrace();
        }
    }
}
