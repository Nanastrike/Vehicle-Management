<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Maintenance Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style>
            .alert-container {
                margin: 20px;
                padding: 15px;
                border: 1px solid #ddd;
                border-radius: 5px;
            }
            .alert {
                margin: 10px 0;
                padding: 10px;
                border-left: 4px solid #ff4444;
                background-color: #f8f8f8;
            }
            .alert.critical {
                border-left-color: #ff4444;
            }
            .alert.warning {
                border-left-color: #ffbb33;
            }
            .alert.normal {
                border-left-color: #00C851;
            }
            .alert-info {
                margin: 5px 0;
                color: #666;
            }
            .menu {
                margin: 20px;
            }
            .menu button {
                margin: 5px;
                padding: 8px 15px;
                background-color: #007bff;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            .menu button:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <h1>System Maintenance Monitor</h1>
        
        <div class="menu">
            <button onclick="checkMechanical()">Check Mechanical Components</button>
            <button onclick="checkElectrical()">Check Electrical Components</button>
            <button onclick="checkEngine()">Check Engine Diagnostics</button>
            <button onclick="clearAlerts()">Clear All Alerts</button>
        </div>

        <div class="alert-container">
            <h2>Maintenance Alerts</h2>
            <div id="alertsList">
                <!-- Alerts will be displayed here dynamically -->
            </div>
        </div>

        <p><a href="index.html">Back to Home</a></p>

        <script>
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
                    <div class="alert-info">Time: ${alert.timestamp}</div>
                    <div class="alert-info">Vehicle ID: ${alert.vehicleId}</div>
                    <div class="alert-info">Component Type: ${alert.componentType}</div>
                    <div class="alert-info">Wear Level: ${alert.wearLevel}%</div>
                    <div class="alert-info">Alert Message: ${alert.alertMessage}</div>
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