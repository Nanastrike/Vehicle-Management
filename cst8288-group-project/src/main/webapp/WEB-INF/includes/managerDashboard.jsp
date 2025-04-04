

<!-- Main Container -->
    <div class="container">

        <!-- Row 1: Whole Overview and Reports Overview -->
        <div class="card-container row-1">
            <jsp:include page="/WEB-INF/includes/wholeOverviewCard.jsp" />
            <jsp:include page="/WEB-INF/includes/reportsOverviewCard.jsp" />
        </div>

        <!-- Row 2: Vehicle Management and GPS Tracking Overview -->
        <div class="card-container row-2">
            <div class="card">
                <!-- Include Vehicle Overview (Counts + Last Vehicle) -->
                <jsp:include page="/WEB-INF/includes/vehicleOverviewCard.jsp" />
            </div>
            
            <!-- Include GPS Tracking Card -->
            <jsp:include page="/WEB-INF/includes/gpsTrackingCard.jsp" />
        </div>

        <!-- Row 3: Maintenance and Fuel/Energy Overview -->
        <div class="card-container row-3">
            <!-- Include Maintenance Card -->
            <jsp:include page="/WEB-INF/includes/maintenanceCard.jsp" />
            <!-- Include Fuel/Energy Card -->
            <jsp:include page="/WEB-INF/includes/fuelEnergyCard.jsp" />
        </div>

    </div>
