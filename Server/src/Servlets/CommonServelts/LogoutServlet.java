package Servlets.CommonServelts;

import UserManager.userManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static Constans.ConstantsServer.*;

@WebServlet(name = "LogoutServlet", urlPatterns = "/Logout")

public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession="";
        String type = request.getParameter(USER_TYPE);
        if(type.equals(AGENT))
            usernameFromSession = request.getParameter(USER_NAME);
        else
          usernameFromSession = SessionUtils.getUsername(request);

        userManager userManager = ServletUtils.getUserManager(getServletContext());
        if (usernameFromSession != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            SessionUtils.clearSession(request);
            userManager.removeUser(usernameFromSession,type);

        }
    }
}
