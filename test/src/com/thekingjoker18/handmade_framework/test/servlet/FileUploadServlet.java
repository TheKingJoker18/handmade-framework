package com.thekingjoker18.handmade_framework.test.servlet;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thekingjoker18.handmade_framework.utils.FileUpload;

public class FileUploadServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        try {

        } catch (Exception e) {
            throw new ServletException(e);
        }     
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        res.setContentType("text/html");
        try {
            FileUpload file = (FileUpload) req.getAttribute("file");
            String name = (String) req.getAttribute("name");

            if (!name.trim().isEmpty() && !name.startsWith(" ")) {
                file.setName(name);
            }

            PrintWriter out = res.getWriter();
            out.println("<center>");
            out.println("<p>Name: " + file.getName() + "</p>");
            if (file.getFileType().compareTo("image") == 0) {
                out.println("<img src=\"" + file.getContent() + "\" alt=\"Some Image\" style=\"width: 200px;\" />");
            } else {
                out.println("<p>Content: " + file.getContent() + "</p>");
            }
            out.println("</center>");

            try {
                // By using a relative path, the initial directory is "C:/xampp/tomcat" that's why we add the part "webapps/..." 
                String uploadFolder = "webapps/public/files/" + file.getFileType();
                out.println("<p><b>Upload Folder: " + uploadFolder + "</b></p>");
                
                file.saveTo(uploadFolder);
                out.println("<p>" + file.getName() + " saved successfully!</p>");

            } catch (Exception e) {
                out.println("<p>" + file.getName() + " save failed...</p>");
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }     
    }

}
