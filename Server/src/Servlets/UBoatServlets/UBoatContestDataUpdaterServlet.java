package Servlets.UBoatServlets;

import UserManager.userManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static Constans.ConstantsServer.*;

@WebServlet(name = "UBoatContestDataUpdaterServlet", urlPatterns = "/UBoatContestDataUpdaterServlet")

public class UBoatContestDataUpdaterServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            userManager userManager = ServletUtils.getUserManager(getServletContext());
            String UBoatName = SessionUtils.getUsername(request); // gets uboat user name
            String machineConf = request.getParameter(MACHINE_CONF);//gets the new machine conf
            String encryptMessage = request.getParameter(ENCRYPT_MESSAGE);//gets the new encrypt message
            String wordToDecrypt  = request.getParameter(WORD_TO_DECRYPT);
            userManager.getUBoatAvailable().get(UBoatName).setCurrentMachineConf(machineConf);
            userManager.getUBoatAvailable().get(UBoatName).setEncryptMessage(encryptMessage);
            userManager.getUBoatAvailable().get(UBoatName).setMessageBeforeUnencrypted(wordToDecrypt);
            userManager.getUBoatAvailable().get(UBoatName).setMachineInEngine();//set machine each time conf is changing
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("Machine conf (" + machineConf + ")" + " and Encrypt message(" + encryptMessage + ") updated successfully");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(e.getCause().getMessage());//send back the issue caused to the problem
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException { processRequest(request, response);}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {processRequest(request, response);}
}
