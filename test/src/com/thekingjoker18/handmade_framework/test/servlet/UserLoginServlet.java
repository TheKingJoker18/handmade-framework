package com.thekingjoker18.handmade_framework.test.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thekingjoker18.handmade_framework.test.model.Account;
import com.thekingjoker18.handmade_framework.utils.MySession;

public class UserLoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        try  {
            MySession mysession = (MySession) req.getAttribute("mysession");
            mysession.delete("account");
            mysession.delete("ls_projectNames");
            String html = "<script type=\"text/javascript\">";
            html += "alert(\"Account disconnected as successfully\");";
            html += "window.location.href = \"session_form_test\";";
            html += "</script>";
            out.println(html);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        try {
            Account account = (Account) req.getAttribute("account");
            if (account.getEmail().compareTo("") == 0 && account.getPassword().compareTo("") == 0) {
                String html = "<script type=\"text/javascript\">";
                html += "alert(\"The Email and the Password of the account must not be empty\");";
                html += "window.location.href = \"session_form_test\";";
                html += "</script>";
                out.println(html);

            } else {
                MySession mysession = (MySession) req.getAttribute("mysession");
                mysession.add("account", account);
                mysession.add("role", "user");
                res.sendRedirect("session_home_test");
            }
            
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
