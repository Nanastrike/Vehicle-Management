<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User.User" %>
<%@ page import="model.VehicleManagement.Vehicle" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Retrieve the logged-in user from the session
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        response.sendRedirect("LoginPage.jsp");
        return;
    }
    
    List<Vehicle> vehicleList = (List<Vehicle>) request.getAttribute("vehicleList");
    Vehicle currentVehicle = (Vehicle) request.getAttribute("currentVehicle");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Maintenance Management</title>
        <!-- Google Font - Quicksand for consistency -->
        <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">

        <style>
            body {
                font-family: 'Quicksand', sans-serif;
                background-color: #f8f9fa;
                margin: 0;
                padding: 0;
            }

            /* Main Container and Content */
            .container {
                max-width: 1200px;
                margin: 20px auto;
                padding: 20px;
                background-color: #fff;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            .section {
                margin-bottom: 30px;
                padding: 20px;
                background-color: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
            }

            h2 {
                color: #555;
                text-align: left;
                font-size: 24px;
                margin-bottom: 20px;
            }

            /* Form Styles */
            .form-grid {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
                gap: 20px;
                margin-bottom: 20px;
            }

            .form-group {
                margin-bottom: 15px;
            }

            .form-group label {
                display: block;
                margin-bottom: 5px;
                color: #555;
                font-weight: 500;
            }

            .form-group input,
            .form-group select,
            .form-group textarea {
                width: 100%;
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-family: 'Quicksand', sans-serif;
                font-size: 14px;
            }

            .form-group textarea {
                height: 100px;
                resize: vertical;
            }

            /* Button Styles */
            .btn {
                background-color: #4682B4;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 8px;
                font-weight: 600;
                transition: background-color 0.3s ease-in-out;
                border: none;
                cursor: pointer;
                margin: 5px;
            }

            .btn:hover {
                background-color: #4169E1;
            }

            .btn-container {
                margin-bottom: 20px;
            }

            /* Table Styles */
            .maintenance-table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }

            .maintenance-table th,
            .maintenance-table td {
                padding: 12px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }

            .maintenance-table th {
                background-color: #f8f9fa;
                font-weight: 600;
                color: #555;
            }

            .maintenance-table tr:hover {
                background-color: #f8f9fa;
            }

            .component-status {
                margin-top: 20px;
            }

            .status-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                gap: 20px;
                margin-top: 20px;
            }

            .status-section {
                background-color: #fff;
                padding: 15px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }

            .status-table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 10px;
            }

            .status-table th,
            .status-table td {
                padding: 8px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }

            .status-table th {
                background-color: #f8f9fa;
                font-weight: 600;
            }

            .critical {
                color: #dc3545;
                font-weight: bold;
            }

            .warning {
                color: #ffc107;
                font-weight: bold;
            }

            .normal {
                color: #28a745;
            }

            .alert {
                margin: 10px 0;
                padding: 15px;
                border-radius: 8px;
                background-color: #fff;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }

            .alert.critical {
                border-left: 4px solid #dc3545;
            }

            .alert.warning {
                border-left: 4px solid #ffc107;
            }

            .alert.normal {
                border-left: 4px solid #28a745;
            }

            .alert-info {
                margin: 5px 0;
            }

            .alert-info strong {
                color: #333;
                font-weight: 600;
            }

            .monitoring-buttons {
                display: flex;
                gap: 10px;
                margin-bottom: 20px;
                flex-wrap: wrap;
            }

            .monitoring-btn {
                flex: 1;
                min-width: 200px;
                margin: 5px;
                padding: 10px;
                background-color: #4a90e2;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                text-align: center;
                text-decoration: none;
                display: inline-block;
            }

            .monitoring-btn:hover {
                background-color: #357abd;
            }

            .modal-toggle {
                display: none;
            }

            .modal {
                display: none;
                position: fixed;
                z-index: 1;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.4);
            }

            .modal-content {
                background-color: #fefefe;
                margin: 15% auto;
                padding: 20px;
                border: 1px solid #888;
                width: 80%;
                max-width: 600px;
                border-radius: 8px;
                position: relative;
            }

            .close {
                color: #aaa;
                float: right;
                font-size: 28px;
                font-weight: bold;
                cursor: pointer;
                position: absolute;
                right: 20px;
                top: 10px;
            }

            .close:hover {
                color: black;
            }

            #mechanical-modal-toggle:checked ~ #mechanicalModal,
            #electrical-modal-toggle:checked ~ #electricalModal,
            #engine-modal-toggle:checked ~ #engineModal {
                display: block;
            }

            .form-group {
                margin-bottom: 15px;
            }

            .form-group label {
                display: block;
                margin-bottom: 5px;
            }

            .form-group input,
            .form-group select {
                width: 100%;
                padding: 8px;
                border: 1px solid #ddd;
                border-radius: 4px;
            }
        </style>
    </head>
    <body>
        <!-- Include Navigation Bar -->
        <jsp:include page="navbar.jsp" />

        <div class="container">
            <!-- Maintenance Alert Section -->
            <div class="maintenance-alert-section">
                <h2>Maintenance Alerts</h2>
                <c:if test="${not empty vehiclesNeedingMaintenance}">
                    <div class="alert warning">
                        <h3>Vehicles requiring maintenance (over 3 months since last service):</h3>
                        <ul>
                            <c:forEach var="vehicle" items="${vehiclesNeedingMaintenance}">
                                <li>
                                    Vehicle Number: ${vehicle.vehicleNumber} - 
                                    Vehicle Type: ${vehicle.vehicleType.typeName} - 
                                    Last Maintenance Date: ${vehicle.lastMaintenanceDate}
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
                <c:if test="${empty vehiclesNeedingMaintenance}">
                    <div class="alert normal">
                        <p>No vehicles currently require maintenance.</p>
                    </div>
                </c:if>
            </div>

            <div class="section">
                <h2>Schedule Maintenance Task</h2>
                <form action="MaintenanceServlet" method="post">
                    <input type="hidden" name="action" value="scheduleTask">
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="vehicleNumber">Vehicle Number</label>
                            <select id="vehicleNumber" name="vehicleNumber" required>
                                <option value="">Select Vehicle Number</option>
                                <c:forEach var="vehicle" items="${vehicleList}">
                                    <option value="${vehicle.vehicleID}">
                                        ${vehicle.vehicleNumber} - ${vehicle.vehicleType.typeName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="taskType">Task Type</label>
                            <select id="taskType" name="taskType" required>
                                <option value="">Select Type</option>
                                <option value="ROUTINE">Routine Maintenance</option>
                                <option value="REPAIR">Repair</option>
                                <option value="INSPECTION">Inspection</option>
                                <option value="EMERGENCY">Emergency Maintenance</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="scheduledDate">Scheduled Date</label>
                            <input type="date" id="scheduledDate" name="scheduledDate" required>
                        </div>
                        <div class="form-group">
                            <label for="priority">Priority</label>
                            <select id="priority" name="priority" required>
                                <option value="">Select Priority</option>
                                <option value="HIGH">High</option>
                                <option value="MEDIUM">Medium</option>
                                <option value="LOW">Low</option>
                            </select>
                        </div>
                    </div>
                        <button type="submit" class="btn">Schedule Task</button>
                </form>
            </div>

            <div class="section">
                <h2>Scheduled Tasks</h2>
                <table class="maintenance-table">
                    <thead>
                        <tr>
                            <th>Vehicle Number</th>
                            <th>Task Type</th>
                            <th>Scheduled Date</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="task" items="${scheduledTasks}">
                            <tr>
                                <td>${task.vehicleNumber}</td>
                                <td>${task.taskType}</td>
                                <td>${task.scheduledDate}</td>
                                <td>${task.priority}</td>
                                <td>${task.status}</td>
                                <td>
                                    <form action="MaintenanceServlet" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="deleteTask">
                                        <input type="hidden" name="taskId" value="${task.taskId}">
                                        <button type="submit" class="btn">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="section">
                <h2>Component Monitoring</h2>
                <div class="section">
                    <form action="MaintenanceServlet" method="GET">
                        <div class="form-group">
                            <label for="vehicleNumber">Vehicle Number:</label>
                            <select id="vehicleNumber" name="vehicleNumber" onchange="this.form.submit()">
                                <option value="">Select Vehicle</option>
                                <c:forEach items="${vehicleList}" var="vehicle">
                                    <option value="${vehicle.vehicleNumber}" ${param.vehicleNumber eq vehicle.vehicleNumber ? 'selected' : ''}>
                                        ${vehicle.vehicleNumber}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label>Last Maintenance Date:</label>
                            <span>
                                <c:choose>
                                    <c:when test="${currentVehicle != null && currentVehicle.lastMaintenanceDate != null}">
                                        ${currentVehicle.lastMaintenanceDate}
                                    </c:when>
                                    <c:otherwise>
                                        No maintenance record
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>

                        <div class="form-group">
                            <label>Components to Check:</label>
                            <div class="checkbox-group">
                                <h4>Mechanical Components:</h4>
                                <input type="checkbox" id="brakes" name="components" value="brakes">
                                <label for="brakes">Brakes</label>
                                <input type="checkbox" id="tires" name="components" value="tires">
                                <label for="tires">Tires</label>
                                
                                <h4>Electrical Components:</h4>
                                <input type="checkbox" id="battery" name="components" value="battery">
                                <label for="battery">Battery</label>
                                <input type="checkbox" id="lights" name="components" value="lights">
                                <label for="lights">Lights</label>
                                
                                <h4>Engine Components:</h4>
                                <input type="checkbox" id="oil" name="components" value="oil">
                                <label for="oil">Oil Level</label>
                                <input type="checkbox" id="coolant" name="components" value="coolant">
                                <label for="coolant">Coolant Level</label>
                            </div>
                        </div>
                        
                        <input type="hidden" name="action" value="checkComponents">
                        <button type="submit" class="btn">Submit Maintenance Check</button>
                    </form>
                </div>

                <h2>Component Status</h2>
                <div class="status-grid">
                    <div class="status-section">
                        <h3>Mechanical Components</h3>
                        <table class="status-table">
                            <thead>
                                <tr>
                                    <th>Vehicle Number</th>
                                    <th>Component</th>
                                    <th>Hours Used</th>
                                    <th>Wear Level</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="status" items="${componentStatuses}">
                                    <tr>
                                        <td>${status.vehicleId}</td>
                                        <td>${status.componentName}</td>
                                        <td>${status.hoursUsed}</td>
                                        <td>${status.wearLevel}%</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    
                    <div class="status-section">
                        <h3>Electrical Components</h3>
                        <table class="status-table">
                            <thead>
                                <tr>
                                    <th>Vehicle Number</th>
                                    <th>Component</th>
                                    <th>Hours Used</th>
                                    <th>Wear Level</th>
                                </tr>
                            </thead>
                            <tbody id="electricalStatus">
                                <!-- This will be populated by JavaScript -->
                            </tbody>
                        </table>
                    </div>
                    
                    <div class="status-section">
                        <h3>Engine Diagnostics</h3>
                        <table class="status-table">
                            <thead>
                                <tr>
                                    <th>Vehicle Number</th>
                                    <th>Parameter</th>
                                    <th>Value</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody id="engineStatus">
                                <!-- This will be populated by JavaScript -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- 維護警報顯示 -->
            <div class="alert-container">
                <h2>Maintenance Alerts</h2>
                <div id="alertsList">
                    <c:forEach var="alert" items="${maintenanceAlerts}">
                        <div class="alert ${alert.componentStatus.status == 'CRITICAL' ? 'critical' : alert.componentStatus.status == 'WARNING' ? 'warning' : 'normal'}">
                            <div class="alert-info"><strong>Time:</strong> ${alert.timestamp}</div>
                            <div class="alert-info"><strong>Vehicle ID:</strong> ${alert.componentStatus.vehicleId}</div>
                            <div class="alert-info"><strong>Component:</strong> ${alert.componentStatus.componentName}</div>
                            <div class="alert-info"><strong>Hours Used:</strong> ${alert.componentStatus.hoursUsed}</div>
                            <div class="alert-info"><strong>Wear Level:</strong> ${alert.componentStatus.wearLevel}%</div>
                            <div class="alert-info"><strong>Message:</strong> ${alert.componentStatus.alertMessage}</div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <script>
            document.getElementById('vehicleNumber').addEventListener('change', function() {
                const vehicleNumber = this.value;
                // 這裡應該發送AJAX請求來獲取最後維修日期
                // 示例代碼：
                fetch(`/getLastMaintenance?vehicleNumber=${vehicleNumber}`)
                    .then(response => response.json())
                    .then(data => {
                        document.getElementById('lastMaintenanceDate').textContent = data.lastMaintenanceDate;
                        updateComponentStatus(vehicleNumber);
                    });
            });

            function updateComponentStatus(vehicleNumber) {
                // 這裡應該發送AJAX請求來獲取組件狀態
                // 示例代碼：
                fetch(`/getComponentStatus?vehicleNumber=${vehicleNumber}`)
                    .then(response => response.json())
                    .then(data => {
                        const tbody = document.getElementById('mechanicalStatus');
                        tbody.innerHTML = '';
                        
                        // 添加剎車行
                        tbody.innerHTML += `
                            <tr>
                                <td>${data.vehicleNumber}</td>
                                <td>Brakes</td>
                                <td>${data.brakeHours}</td>
                                <td>${data.brakeWearLevel.toFixed(1)}%</td>
                            </tr>
                        `;
                    });
            }
        </script>
    </body>
</html> 