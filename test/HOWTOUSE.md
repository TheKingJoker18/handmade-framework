# How to Use the Handmade Framework

Welcome to the Handmade Framework\! This guide will walk you through the main features of the framework using the provided controller, model, and servlet examples.

## Table of Contents

1. [Creating a Controller](#1-creating-a-controller)
2. [Defining Routes (URL Mapping)](#2-defining-routes-url-mapping)
3. [Returning Views (ModelView)](#3-returning-views-modelview)
4. [Handling Form Input](#4-handling-form-input)
      * [Using `@Param` for Simple Values](#using-param-for-simple-values)
      * [Using `@ModelAttribute` for Objects](#using-modelattribute-for-objects)
5. [Data Validation](#5-data-validation)
6. [Handling File Uploads](#6-handling-file-uploads)
7. [Session Management & Security](#7-session-management--security)
      * [Injecting the Session](#injecting-the-session)
      * [Authentication with `@Authentified`](#authentication-with-authentified)
      * [Authorization with `@Role`](#authorization-with-role)
      * [Example: Login/Logout Flow](#example-loginlogout-flow)
8. [Creating a REST API](#8-creating-a-rest-api)

-----

## 1\. Creating a Controller

A controller is a simple Java class annotated with `@AnnotationController`. This annotation defines a base URL path for all methods inside the class.

* **Example 1: Root Controller (`TestController.java`)**
    This controller handles the root URL (`/`) of your application.

    ```java
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
    ```

* **Example 2: Prefixed Controller (`UserController.java`)**
    All routes in this class will be prefixed with `/user_controller`.

    ```java
    package com.thekingjoker18.handmade_framework.test.controller;

    import com.thekingjoker18.handmade_framework.annotation.AnnotationController;
    // ... other imports

    @AnnotationController(name = "/user_controller")
    public class UserController {
        // ... methods
    }
    ```

## 2\. Defining Routes (URL Mapping)

You can map methods to specific URLs using the `@Url`, `@Get`, and `@Post` annotations.

* `@Url`: Maps a method to a URL. This method will respond to **any** HTTP verb (GET, POST, etc.) if `@Get` or `@Post` is not present.
* `@Get`: Restricts the method to only **GET** requests.
* `@Post`: Restricts the method to only **POST** requests.

The final URL is the combination of the controller's path and the method's path.

* **Example (`UserController.java` & `LoginController.java`)**

    ```java
    @AnnotationController(name = "/user_controller")
    public class UserController {
        
        // Responds to: GET, POST, etc. at /user_controller/form_test
        @Url("/form_test")
        public ModelView test_form() { /*...*/ }

        // Responds to: ONLY POST at /user_controller/form_test
        @Post
        @Url("/form_test")
        public ModelView test_result(...) { /*...*/ }
    }

    @AnnotationController(name = "/login_controller")
    public class LoginController {

        // Responds to: ONLY GET at /login_controller/session_disconnect_test
        @Get
        @Url("/session_disconnect_test")
        public ModelView test_session_disconnect(MySession mysession) { /*...*/ }
    }
    ```

## 3\. Returning Views (ModelView)

The `ModelView` object is used to tell the framework which view to render (like a JSP) or which servlet to forward to. You can also use it to pass data from your controller to the view/servlet.

* `setUrl(String url)`: Sets the destination. This can be a `.jsp` file or a Servlet URL.

* `addObject(String key, Object value)`: Adds data to the request, which can be retrieved in the JSP or Servlet.

* **Example 1: Forwarding to a JSP (`TestController.java`)**

    ```java
    @Url(value = "")
    public ModelView index() {
        ModelView view = new ModelView();
        view.setUrl("test/index.jsp"); // Forwards to a JSP
        view.addObject("message", "Welcome, it works very well!");
        return view;
    }

    // In test/index.jsp, you could use: <%= request.getAttribute("message") %>
    ```

* **Example 2: Forwarding to a Servlet (`LoginController.java`)**

    ```java
    @Post
    @Url("/session_login_test")
    public ModelView test_session_login(@ModelAttribute(name = "account") Account account, MySession mysession) {
        ModelView view = new ModelView();
        view.setUrl("UserLoginServlet"); // Forwards to UserLoginServlet
        view.addObject("account", account);
        view.addObject("mysession", mysession);
        return view;
    }

    // In UserLoginServlet.java, you retrieve the data:
    // Account account = (Account) req.getAttribute("account");
    ```

## 4\. Handling Form Input

The framework can automatically inject form data into your controller methods as parameters.

### Using `@Param` for Simple Values

Use `@Param` for individual form fields like `String`, `int`, `double`, etc.

* **Example (`UserController.java`)**

    ```java
    // Handles a form with fields "nom" and "age"
    @Post
    @Url("/form_test")
    public ModelView test_result(@Param(name = "nom") String nom, @Param(name = "age") int age) {
        ModelView view = new ModelView();
        view.setUrl("result.jsp");
        view.addObject("nom", nom);
        view.addObject("age", age);
        return view;
    }
    ```

### Using `@ModelAttribute` for Objects

Use `@ModelAttribute` to automatically map multiple form fields to a Java object (Model). This requires your model class to use `@ModelField` annotations.

* **Step 1: The Model (`Department.java`)**
    Annotate fields with `@ModelField` to link them to form input names.

    ```java
    package com.thekingjoker18.handmade_framework.test.model;

    import com.thekingjoker18.handmade_framework.annotation.ModelField;

    public class Department {
        @ModelField(name = "name") // Maps to form input: <input name="name">
        String name;
        
        @ModelField(name = "money") // Maps to form input: <input name="money">
        double money;
        
        // ... getters and setters
    }
    ```

* **Step 2: The Controller (`UserController.java`)**
    The framework will create an `Account` object and a `Department` object and fill them with data from the request.

    ```java
    @Url("/object_result_test")
    public ModelView test_object_result(
        @ModelAttribute(name = "account") Account account, 
        @Param(name = "age") int age, // You can mix @Param and @ModelAttribute
        @ModelAttribute(name = "department") Department department
    ) {
        ModelView view = new ModelView();
        view.setUrl("object_result.jsp");
        view.addObject("account", account); // Pass the whole object to the view
        view.addObject("age", age);
        view.addObject("department", department);
        return view;
    }
    ```

## 5\. Data Validation

You can add validation annotations to your model fields. The framework will check these (as defined in Sprint 13/14) when you use `@ModelAttribute`.

* **Example (`Account.java`)**

    ```java
    package com.thekingjoker18.handmade_framework.test.model;

    import com.thekingjoker18.handmade_framework.annotation.Email;
    import com.thekingjoker18.handmade_framework.annotation.Length;
    import com.thekingjoker18.handmade_framework.annotation.ModelField;
    import com.thekingjoker18.handmade_framework.annotation.NotNull;
    import com.thekingjoker18.handmade_framework.annotation.Range;

    public class Account {
        @ModelField(name = "id")
        @Range(min = "1", max = "10") // Value must be between 1 and 10
        int id;

        @ModelField(name = "email")
        @Email // Must be a valid email format
        String email;

        @ModelField(name = "password")
        @NotNull // Cannot be null
        @Length(value = 8) // Must be exactly 8 characters long
        String password;
        
        // ... getters and setters
    }
    ```

## 6\. Handling File Uploads

The framework can inject uploaded files directly using the `FileUpload` class.

* **Step 1: The Controller (`UserController.java`)**
    Use `@Param` to get the `FileUpload` object from a form input of type `file`.

    ```java
    @Post
    @Url("/file_upload_form_test")
    public ModelView test_file_upload_result(@Param(name = "file") FileUpload file, @Param(name = "name") String name) {
        ModelView view = new ModelView();
        view.setUrl("FileUploadServlet"); // Forward to a servlet to process the file
        view.addObject("file", file);
        view.addObject("name", name);
        return view;
    }
    ```

* **Step 2: Processing in the Servlet (`FileUploadServlet.java`)**
    You can retrieve the `FileUpload` object and use its methods.

    ```java
    public class FileUploadServlet extends HttpServlet {
        public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
            try {
                FileUpload file = (FileUpload) req.getAttribute("file");
                String name = (String) req.getAttribute("name");

                // ... set name if needed ...

                PrintWriter out = res.getWriter();
                out.println("<p>Name: " + file.getName() + "</p>");
                out.println("<p>Content: " + file.getContent() + "</p>"); // e.g., for text files or base64 image

                // Save the file
                String uploadFolder = "webapps/public/files/" + file.getFileType();
                file.saveTo(uploadFolder);
                out.println("<p>" + file.getName() + " saved successfully!</p>");

            } catch (Exception e) {
                throw new ServletException(e);
            }     
        }
    }
    ```

## 7\. Session Management & Security

The framework provides robust session management and security through annotations.

### Injecting the Session

Get the current session by adding `MySession` as a method parameter.

* **Example (`LoginController.java`)**

    ```java
    @Url("/session_form_test")
    public ModelView test_session_form(MySession mysession) { // Session is injected
        ModelView view = new ModelView();
        view.setUrl("session_form.jsp");
        view.addObject("mysession", mysession); // Pass it to the view
        return view;
    }
    ```

### Authentication with `@Authentified`

This annotation restricts access to users who are logged in. It checks if a specific key exists in the session. It can be applied to a single method or an entire class.

* **Example 1: Method-Level (`LoginController.java`)**
    Only authenticated users (with "account" in their session) can access the home page.

    ```java
    @Url("/session_home_test")
    @Authentified(session_name = "account")  
    public ModelView test_session_home(MySession mysession) {
        // ...
    }
    ```

* **Example 2: Class-Level (`UserController.java`)**
    *All* methods in `UserController` require the user to have "account" in their session.

    ```java
    @AnnotationController(name = "/user_controller")
    @Authentified(session_name = "account")
    public class UserController {
        // All methods here are protected
    }
    ```

### Authorization with `@Role`

This annotation checks the user's role (stored in the session) against a list of authorized roles. It's used *with* `@Authentified`.

* **Example (`LoginController.java`)**
    Only users with the role "admin" or "user" can access this page. (Note: The role is assumed to be stored in the session under the key "role" as seen in `UserLoginServlet`).

    ```java
    @Url("/session_result_test")
    @Authentified(session_name = "account")
    @Role(authorized_roles = {"admin", "user"})
    public ModelView test_session_result(MySession mysession) {
        // ...
    }
    ```

### Example: Login/Logout Flow

1. **Login (POST):** A user submits their login form to `/session_login_test`.
2. **Controller:** `LoginController`'s `test_session_login` method catches this, creates a `ModelView`, and forwards to `UserLoginServlet`, passing the `Account` object and `MySession`.
3. **Servlet (`UserLoginServlet.java`):**
      * Retrieves the `Account` and `MySession`.
      * Validates the user.
      * **Adds user info to the session:**

        ```java
        mysession.add("account", account);
        mysession.add("role", "user"); // This is where the role is set
        ```

      * Redirects to the home page (`session_home_test`).
4. **Accessing Home:** The user hits `/session_home_test`. The framework checks `@Authentified(session_name = "account")`, finds "account" in the session, and grants access.
5. **Logout (GET):** The user clicks a link to `/session_disconnect_test`.
6. **Controller:** `LoginController`'s `test_session_disconnect` method forwards to `UserLoginServlet`.
7. **Servlet (`UserLoginServlet.java`):**
      * **Deletes user info from the session:**

        ```java
        mysession.delete("account");
        mysession.delete("ls_projectNames");
        ```

      * Redirects back to the login form.

## 8\. Creating a REST API

By adding the `@Restapi` annotation, you can make a method return raw data (like a String or JSON) instead of forwarding to a view.

* **Example 1: Returning a String (`UserController.java`)**
    Accessing `/user_controller/string_get` will return the raw text "Bomboclat\! Test works successfully..."

    ```java
    @Restapi
    @Url("/string_get")
    @Authentified(session_name = "account")
    public String test_string() {
        return "Bomboclat! Test works successfully...";
    }
    ```

* **Example 2: Returning JSON (`UserController.java`)**
    Accessing `/user_controller/modelview_get` will return a JSON representation of the `ModelView` object, thanks to the `@Restapi` annotation.

    ```java
    @Restapi
    @Url("/modelview_get")
    public ModelView test_modelview() {
        ModelView view = new ModelView();
        view.setUrl("modelview.jsp");
        view.addObject("message", "It's work!!!");
        return view;
        // Output will be JSON, e.g.: {"url":"modelview.jsp", "data":{"message":"It's work!!!"}}
    }
    ```
