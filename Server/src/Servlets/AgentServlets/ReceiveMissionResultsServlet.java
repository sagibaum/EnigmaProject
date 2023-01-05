package Servlets.AgentServlets;

import DataBase.Agent;
import UserManager.userManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import missions.results.MissionResults;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "ReceiveMissionResultsServlet", urlPatterns = "/sendResults")
public class ReceiveMissionResultsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML

        String agentUserName = SessionUtils.getUsername(request); // gets uboat user name

        response.setContentType("text/plain;charset=UTF-8"); //returning type is JSON

        Gson gson = new Gson();
        userManager userManager = ServletUtils.getUserManager(getServletContext());
        Agent thisAgent = userManager.getRelevantAgentFromUserName(agentUserName); //get this agent saved in server

        BufferedReader rd = request.getReader();
        String jsonStr = rd.lines().collect(Collectors.joining());
        List<MissionResults> data = gson.fromJson(jsonStr, new TypeToken<ArrayList<MissionResults>>(){}.getType());

        synchronized (this) {
            if (data == null) {
                String errorMessage = "Username " + agentUserName + " , data received from JSON is null.";
                // stands for unauthorized as there is already such user with this name
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
                response.getOutputStream().print(errorMessage);
            }
            else {
                thisAgent.getAllCandidates().clear(); //clear results
                thisAgent.getAllCandidates().addAll(data); //add all the results
                thisAgent.setTasksCompleted(data.get(0).getTasksCompleted());
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
