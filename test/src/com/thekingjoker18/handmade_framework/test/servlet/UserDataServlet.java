package com.thekingjoker18.handmade_framework.test.servlet;

import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thekingjoker18.handmade_framework.utils.MySession;

public class UserDataServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        try {

        } catch (Exception e) {
            throw new ServletException(e);
        }     
    }

    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        try {
            String projectName = (String) req.getAttribute("projectName");
            MySession mysession = (MySession) req.getAttribute("mysession");
            Vector<String> ls_projectNames = null;
            if (mysession.get("ls_projectNames") == null) {
                ls_projectNames = new Vector<String>();
            } else {
                ls_projectNames = (Vector<String>) mysession.get("ls_projectNames");
            }
            ls_projectNames.add(projectName);
            mysession.add("ls_projectNames", ls_projectNames);
            res.sendRedirect("session_result_test");
            
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
