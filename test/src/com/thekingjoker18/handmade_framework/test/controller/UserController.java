package com.thekingjoker18.handmade_framework.test.controller;

import com.thekingjoker18.handmade_framework.annotation.AnnotationController;
import com.thekingjoker18.handmade_framework.annotation.Authentified;
import com.thekingjoker18.handmade_framework.annotation.ModelAttribute;
import com.thekingjoker18.handmade_framework.annotation.Param;
import com.thekingjoker18.handmade_framework.annotation.Post;
import com.thekingjoker18.handmade_framework.annotation.Restapi;
import com.thekingjoker18.handmade_framework.annotation.Url;
import com.thekingjoker18.handmade_framework.test.model.Account;
import com.thekingjoker18.handmade_framework.test.model.Department;
import com.thekingjoker18.handmade_framework.utils.FileUpload;
import com.thekingjoker18.handmade_framework.utils.ModelView;
import com.thekingjoker18.handmade_framework.utils.MySession;

@AnnotationController(name = "/user_controller")
@Authentified(session_name = "account")
public class UserController {
    MySession mysession;

    public MySession getMysession() { return this.mysession; }
    public void setMysession(MySession mysession) { this.mysession = mysession; }

    public UserController() {}
    
    @Restapi
    @Url("/string_get")
    @Authentified(session_name = "account")
    public String test_string() {
        return "Bomboclat! Test works successfully...";
    }

    @Url("/duplicated_get")
    public String test_duplicated() {
        return "Bomboclat!";
    }

    @Restapi
    @Url("/modelview_get")
    public ModelView test_modelview() {
        ModelView view = new ModelView();
        view.setUrl("modelview.jsp");
        view.addObject("message", "It's work!!!");
        return view;
    }

    @Url("/int_get")
    public int test_int() {
        return 6;
    }

    @Url("/form_test")
    public ModelView test_form() {
        ModelView view = new ModelView();
        view.setUrl("test/form.jsp");
        return view;
    }

    @Post
    @Url("/form_test")
    public ModelView test_result(@Param(name = "nom") String nom, @Param(name = "age") int age) {
        ModelView view = new ModelView();
        view.setUrl("result.jsp");
        view.addObject("nom", nom);
        view.addObject("age", age);
        return view;
    }

    @Url("/object_form_test")
    public ModelView test_object_form() {
        ModelView view = new ModelView();
        view.setUrl("object_form.jsp");
        return view;
    }

    @Url("/object_result_test")
    public ModelView test_object_result(@ModelAttribute(name = "account") Account account, @Param(name = "age") int age, @ModelAttribute(name = "department") Department department) {
        ModelView view = new ModelView();
        view.setUrl("object_result.jsp");
        view.addObject("account", account);
        view.addObject("age", age);
        view.addObject("department", department);
        return view;
    }

    @Url("/file_upload_form_test")
    public ModelView test_file_upload_form() {
        ModelView view = new ModelView();
        view.setUrl("file_upload_form.jsp");
        return view;
    }

    @Post
    @Url("/file_upload_form_test")
    public ModelView test_file_upload_result(@Param(name = "file") FileUpload file, @Param(name = "name") String name) {
        ModelView view = new ModelView();
        view.setUrl("FileUploadServlet");
        view.addObject("file", file);
        view.addObject("name", name);
        return view;
    }

}
