package Servlets.AgentServlets;

import DataBase.Agent;
import DataBase.UBoat;
import ServerDTOs.Allies.AlliesDTO;
import UserManager.userManager;
import Utils.ServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static Constans.ConstantsServer.USER_NAME;

@WebServlet(name = "AlliesListServlet", urlPatterns = "/allieslist")
public class AlliesListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            userManager userManager = ServletUtils.getUserManager(getServletContext());
            Set<String> usersList = userManager.getAllies();
            String json = gson.toJson(usersList);
            out.println(json);
            out.flush();
        }
    }

}