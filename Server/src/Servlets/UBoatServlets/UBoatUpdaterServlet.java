package Servlets.UBoatServlets;


import UserManager.userManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class UBoatUpdaterServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            Gson gson = new Gson();
            String json;
            userManager userManager = ServletUtils.getUserManager(getServletContext());
            String UBoatName = SessionUtils.getUsername(request); // gets uboat user name
            if(UBoatName!=null){
            userManager.getUBoatAvailable().get(UBoatName).refreshList();//refreshes each call to get the latest data
            userManager.getUBoatAvailable().get(UBoatName).updateCandidates();//refreshes each call to get the latest data
             json = gson.toJson( userManager.getUBoatDTO(UBoatName));
            }
            else  json=null;
            out.println(json);
            out.flush();
        } catch (Exception e) { // invalid file !
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(e.getCause().getMessage());//send back the issue caused to the problem
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException { processRequest(request, response);}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {processRequest(request, response);}
}
