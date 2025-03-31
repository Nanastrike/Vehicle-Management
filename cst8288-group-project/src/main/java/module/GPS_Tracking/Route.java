/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking;

/*
 *一维道路的最长长度
 * 
 */
public class Route {
    public static int getRoadDistance(int roadNumber){
        switch (roadNumber) {
            case 1:
                return 10;
            case 2:
                return 20;
            case 3:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid road type!");
        }
    }
}
