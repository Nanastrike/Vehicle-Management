<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User.User" %>

<%
    // Retrieve the logged-in user from the session
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        response.sendRedirect("LoginPage.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Maintenance Management</title>
        <!-- Google Font - Quicksand for consistency -->
        <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">
        <!-- Include jQuery UI for datepicker -->
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

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

            /* Alert Styles */
            .alert-container {
                margin-top: 20px;
            }

            .alert {
                margin: 10px 0;
                padding: 15px;
                border-radius: 8px;
                background-color: #fff;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }

            .alert.critical {
                border-left: 4px solid #ff4444;
            }

            .alert.warning {
                border-left: 4px solid #ffbb33;
            }

            .alert.normal {
                border-left: 4px solid #00C851;
            }

            .alert-info {
                margin: 5px 0;
                color: #555;
            }

            .alert-info strong {
                color: #333;
                font-weight: 600;
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
        </style>
    </head>
    <body>
        <!-- Include Navigation Bar -->
        <jsp:include page="navbar.jsp" />

        <div class="container">
            <div class="section">
                <h2>Schedule Maintenance Task</h2>
                <form id="maintenanceForm" onsubmit="return scheduleMaintenanceTask(event)">
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="vehicleId">Vehicle ID</label>
                            <select id="vehicleId" name="vehicleId" required>
                                <option value="">Select Vehicle</option>
                                <!-- Will be populated dynamically -->
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
                            <input type="text" id="scheduledDate" name="scheduledDate" required>
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
                        <div class="form-group" style="grid-column: span 2;">
                            <label for="description">Description</label>
                            <textarea id="description" name="description" required></textarea>
                        </div>
                    </div>
                    <button type="submit" class="btn">Schedule Task</button>
                </form>
            </div>

            <div class="section">
                <h2>Component Monitoring</h2>
                <div class="btn-container">
                    <button class="btn" onclick="checkMechanical()">Check Mechanical Components</button>
                    <button class="btn" onclick="checkElectrical()">Check Electrical Components</button>
                    <button class="btn" onclick="checkEngine()">Check Engine Diagnostics</button>
                    <button class="btn" onclick="clearAlerts()">Clear All Alerts</button>
                </div>

                <div class="alert-container">
                    <h2>Maintenance Alerts</h2>
                    <div id="alertsList">
                        <!-- Alerts will be displayed here dynamically -->
                    </div>
                </div>
            </div>

            <div class="section">
                <h2>Scheduled Tasks</h2>
                <table class="maintenance-table">
                    <thead>
                        <tr>
                            <th>Vehicle ID</th>
                            <th>Task Type</th>
                            <th>Scheduled Date</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="scheduledTasksList">
                        <!-- Tasks will be displayed here dynamically -->
                    </tbody>
                </table>
            </div>
        </div>

        <script>
            $(document).ready(function() {
                // Initialize datepicker
                $("#scheduledDate").datepicker({
                    dateFormat: 'yy-mm-dd',
                    minDate: 0
                });

                // Load vehicles for dropdown
                loadVehicles();
                
                // Load scheduled tasks
                loadScheduledTasks();
            });

            function loadVehicles() {
                fetch('api/vehicles')
                    .then(response => response.json())
                    .then(vehicles => {
                        const select = document.getElementById('vehicleId');
                        vehicles.forEach(vehicle => {
                            const option = document.createElement('option');
                            option.value = vehicle.id;
                            option.textContent = `Vehicle ${vehicle.id} - ${vehicle.type}`;
                            select.appendChild(option);
                        });
                    });
            }

            function scheduleMaintenanceTask(event) {
                event.preventDefault();
                const formData = new FormData(event.target);
                const task = Object.fromEntries(formData.entries());

                fetch('api/maintenance/schedule', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(task)
                })
                .then(response => response.json())
                .then(result => {
                    alert('Task scheduled successfully!');
                    loadScheduledTasks();
                    event.target.reset();
                })
                .catch(error => {
                    alert('Error scheduling task: ' + error.message);
                });

                return false;
            }

            function loadScheduledTasks() {
                fetch('api/maintenance/tasks')
                    .then(response => response.json())
                    .then(tasks => {
                        const tbody = document.getElementById('scheduledTasksList');
                        tbody.innerHTML = '';
                        tasks.forEach(task => {
                            const row = document.createElement('tr');
                            row.innerHTML = `
                                <td>${task.vehicleId}</td>
                                <td>${task.taskType}</td>
                                <td>${task.scheduledDate}</td>
                                <td>${task.priority}</td>
                                <td>${task.status}</td>
                                <td>
                                    <button class="btn" onclick="updateTaskStatus(${task.id}, 'COMPLETED')">Complete</button>
                                    <button class="btn" onclick="deleteTask(${task.id})">Delete</button>
                                </td>
                            `;
                            tbody.appendChild(row);
                        });
                    });
            }

            function updateTaskStatus(taskId, status) {
                fetch(`api/maintenance/tasks/${taskId}/status`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ status: status })
                })
                .then(() => {
                    loadScheduledTasks();
                });
            }

            function deleteTask(taskId) {
                if (confirm('Are you sure you want to delete this task?')) {
                    fetch(`api/maintenance/tasks/${taskId}`, {
                        method: 'DELETE'
                    })
                    .then(() => {
                        loadScheduledTasks();
                    });
                }
            }

            function checkMechanical() {
                fetch('api/maintenance/mechanical', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        componentType: 'Brake System',
                        hoursOfUse: 800
                    })
                })
                .then(response => response.json())
                .then(data => {
                    displayAlert(data);
                });
            }

            function checkElectrical() {
                fetch('api/maintenance/electrical', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        componentType: 'Pantograph',
                        voltage: 750,
                        current: 50
                    })
                })
                .then(response => response.json())
                .then(data => {
                    displayAlert(data);
                });
            }

            function checkEngine() {
                fetch('api/maintenance/engine', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        componentType: 'Engine',
                        temperature: 85,
                        pressure: 180
                    })
                })
                .then(response => response.json())
                .then(data => {
                    displayAlert(data);
                });
            }

            function clearAlerts() {
                fetch('api/maintenance/clear', {
                    method: 'POST'
                })
                .then(() => {
                    document.getElementById('alertsList').innerHTML = '';
                });
            }

            function displayAlert(alert) {
                const alertsList = document.getElementById('alertsList');
                const alertDiv = document.createElement('div');
                alertDiv.className = 'alert ' + getAlertClass(alert.wearLevel);
                
                alertDiv.innerHTML = `
                    <div class="alert-info"><strong>Time:</strong> ${alert.timestamp}</div>
                    <div class="alert-info"><strong>Vehicle ID:</strong> ${alert.vehicleId}</div>
                    <div class="alert-info"><strong>Component Type:</strong> ${alert.componentType}</div>
                    <div class="alert-info"><strong>Wear Level:</strong> ${alert.wearLevel}%</div>
                    <div class="alert-info"><strong>Alert Message:</strong> ${alert.alertMessage}</div>
                `;
                
                alertsList.insertBefore(alertDiv, alertsList.firstChild);
            }

            function getAlertClass(wearLevel) {
                if (wearLevel >= 80) return 'critical';
                if (wearLevel >= 60) return 'warning';
                return 'normal';
            }
        </script>
    </body>
</html> 