package presenter;

import businesslayer.observer.MaintenanceAlertObserver;
import businesslayer.observer.VehicleMaintenanceMonitor;
import model.MaintenanceAlert;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/maintenance/*")
public class MaintenanceServlet extends HttpServlet {
    private MaintenancePresenter presenter;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        presenter = new MaintenancePresenter("BUS001");
        gson = new Gson();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
            return;
        }
        
        switch (pathInfo) {
            case "/mechanical":
                handleMechanicalCheck(request, response);
                break;
            case "/electrical":
                handleElectricalCheck(request, response);
                break;
            case "/engine":
                handleEngineCheck(request, response);
                break;
            case "/clear":
                handleClearAlerts(response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }
    
    private void handleMechanicalCheck(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        MaintenanceRequest req = gson.fromJson(request.getReader(), MaintenanceRequest.class);
        presenter.checkMechanicalComponents(req.componentType, req.hoursOfUse);
        sendAlerts(response);
    }
    
    private void handleElectricalCheck(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        ElectricalRequest req = gson.fromJson(request.getReader(), ElectricalRequest.class);
        presenter.checkElectricalComponents(req.componentType, req.voltage, req.current);
        sendAlerts(response);
    }
    
    private void handleEngineCheck(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        EngineRequest req = gson.fromJson(request.getReader(), EngineRequest.class);
        presenter.checkEngineDiagnostics(req.componentType, req.temperature, req.pressure);
        sendAlerts(response);
    }
    
    private void handleClearAlerts(HttpServletResponse response) throws IOException {
        presenter.clearAlerts();
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":\"success\"}");
    }
    
    private void sendAlerts(HttpServletResponse response) throws IOException {
        List<MaintenanceAlert> alerts = presenter.getAlerts();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(alerts.get(alerts.size() - 1)));
    }
    
    // 請求數據類別
    private static class MaintenanceRequest {
        String componentType;
        double hoursOfUse;
    }
    
    private static class ElectricalRequest {
        String componentType;
        double voltage;
        double current;
    }
    
    private static class EngineRequest {
        String componentType;
        double temperature;
        double pressure;
    }
} 