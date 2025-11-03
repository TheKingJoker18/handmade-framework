package com.thekingjoker18.handmade_framework.test.controller;

import com.thekingjoker18.handmade_framework.annotation.AnnotationController;
import com.thekingjoker18.handmade_framework.annotation.Url;
import com.thekingjoker18.handmade_framework.utils.ModelView;

@AnnotationController(name = "/")
public class TestController {
    public TestController() {}

    @Url(value = "")
    public ModelView index() {
        ModelView view = new ModelView();
        view.setUrl("test/index.jsp");
        view.addObject("message", "Welcome, it works very well!");
        return view;
    }
}
