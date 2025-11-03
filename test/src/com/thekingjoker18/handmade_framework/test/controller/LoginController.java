package com.thekingjoker18.handmade_framework.test.controller;

import com.thekingjoker18.handmade_framework.annotation.AnnotationController;
import com.thekingjoker18.handmade_framework.annotation.Authentified;
import com.thekingjoker18.handmade_framework.annotation.Get;
import com.thekingjoker18.handmade_framework.annotation.ModelAttribute;
import com.thekingjoker18.handmade_framework.annotation.Param;
import com.thekingjoker18.handmade_framework.annotation.Post;
import com.thekingjoker18.handmade_framework.annotation.Role;
import com.thekingjoker18.handmade_framework.annotation.Url;
import com.thekingjoker18.handmade_framework.test.model.Account;
import com.thekingjoker18.handmade_framework.utils.ModelView;
import com.thekingjoker18.handmade_framework.utils.MySession;

@AnnotationController(name = "/login_controller")
public class LoginController {
    MySession mysession;

    public MySession getMysession() { return this.mysession; }
    public void setMysession(MySession mysession) { this.mysession = mysession; }

    public LoginController() {}
    
    @Url("/session_form_test")
    public ModelView test_session_form(MySession mysession) {
        ModelView view = new ModelView();
        view.setUrl("session_form.jsp");
        view.addObject("mysession", mysession);
        return view;
    }

    @Post
    @Url("/session_login_test")
    public ModelView test_session_login(@ModelAttribute(name = "account") Account account, MySession mysession) {
        ModelView view = new ModelView();
        view.setUrl("UserLoginServlet");
        view.addObject("account", account);
        view.addObject("mysession", mysession);
        return view;
    }

    @Url("/session_home_test")
    @Authentified(session_name = "account")  
    public ModelView test_session_home(MySession mysession) {
        ModelView view = new ModelView();
        view.setUrl("session_home.jsp");
        view.addObject("mysession", mysession);
        return view;
    }

    @Post
    @Url("/session_add_test")
    @Authentified(session_name = "account")  
    public ModelView test_session_add(@Param(name = "projectName") String projectName, MySession mysession) {
        ModelView view = new ModelView();
        view.setUrl("UserDataServlet");
        view.addObject("projectName", projectName);
        view.addObject("mysession", mysession);
        return view;
    }

    @Url("/session_result_test")
    @Authentified(session_name = "account")
    @Role(authorized_roles = {"admin", "user"})
    public ModelView test_session_result(MySession mysession) {
        ModelView view = new ModelView();
        view.setUrl("session_result.jsp");
        view.addObject("mysession", mysession);
        return view;
    }

    @Get
    @Url("/session_disconnect_test")
    @Authentified(session_name = "account")  
    public ModelView test_session_disconnect(MySession mysession) {
        ModelView view = new ModelView();
        view.setUrl("UserLoginServlet");
        view.addObject("mysession", mysession);
        return view;
    }
    
}
