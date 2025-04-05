/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gps_tracking_test;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import presentation.gps_tracking.StartDrivingServlet;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Unit test for the {@link StartDrivingServlet} servlet.
 *
 * <p>
 * This test verifies that when a vehicle is selected and a valid user ID is
 * present in the session, the servlet correctly initializes the driving session
 * and redirects the user to the operator dashboard.</p>
 *
 * <p>
 * It mocks the necessary request, session, and response objects using
 * Mockito.</p>
 *
 * <p>
 * Tested method: {@code doPost()}</p>
 *
 * @author : Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: Group Project
 * @time: 2025/04/05
 * @Description: Initializes vehicle trip state and starts a new session for
 * tracking.
 */
public class StartDrivingServletTest {

    /**
     * Tests the {@code doPost()} method of {@link StartDrivingServlet}.
     *
     * <p>
     * This test simulates a POST request where the operator selects a vehicle
     * to start driving. It mocks the HTTP request, session, and response
     * objects, and verifies that the servlet redirects to the operator
     * dashboard after processing.</p>
     *
     * <p>
     * Expected behavior:
     * <ul>
     * <li>Request contains a vehicle ID.</li>
     * <li>Session contains a valid user ID.</li>
     * <li>Response is redirected to {@code operatorDashboard}.</li>
     * </ul>
     * </p>
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testDoPostRedirectsToDashboard() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getParameter("vehicleId")).thenReturn("1");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userID")).thenReturn(123);

        StartDrivingServlet servlet = new StartDrivingServlet();
        servlet.doPost(request, response);

        verify(response, times(1)).sendRedirect("operatorDashboard");
    }
}
