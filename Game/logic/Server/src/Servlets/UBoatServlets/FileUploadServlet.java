package Servlets.UBoatServlets;
import UserManager.userManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import schem.out.CTEEnigma;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import static Constans.ConstantsServer.FILENAME;
import static Constans.ConstantsServer.GSON;

@WebServlet(name = "FileUploadServlet", urlPatterns = "/UploadFile")

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        String filename, UBoatName;
        CTEEnigma cteEnigma;
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();

        StringBuilder fileContent = new StringBuilder();
        for (Part part : parts) {
            fileContent.append(readFromInputStream(part.getInputStream()));
        }
        if (parts.size() > 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Error! Only one file is acceptable");
        }

        else {
            InputStream file = new ByteArrayInputStream(fileContent.toString().getBytes()); // converting string builder to input stream
            userManager userManager = ServletUtils.getUserManager(getServletContext());
            UBoatName = SessionUtils.getUsername(request); // gets uboat user name
            filename = request.getParameter(FILENAME);  //  getParameterValues(FILENAME);
            try {
                if (!userManager.checkIfFileExist(UBoatName, filename)) {
                    // Check if the Uboat file already exist, if so it will not creat new one
                    cteEnigma = userManager.getEngine().UploadXmlFileServer(file);
                    userManager.getUBoatAvailable().get(UBoatName).setUBoatDataEnigma(cteEnigma, filename, true);
                    userManager.checkIfFileNameIsInAbandonList(cteEnigma.getCTEBattlefield().getBattleName());
                    response.setStatus(HttpServletResponse.SC_OK);
                    Gson gson = new Gson(); // creating new gson object to send it back to the specified UBoat
                    String json = gson.toJson(userManager.getUBoatAvailable().get(UBoatName).getCteEnigma());
                    out.println(json);
                    out.flush();
                }
                else { userManager.getUBoatAvailable().get(UBoatName).setValidFile(false);
                    response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                    out.print("File already exist in system!");
                }

            } catch (Exception e) { // invalid file !
                userManager.getUBoatAvailable().get(UBoatName).setValidFile(false);//sets indicator of file validation in order to disable data flow to u-boat
                response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                out.print(e.getCause().getMessage());//send back the issue caused to the problem

            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }


    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}