# Handmade Framework

**Author**: RANAIVOSON NY Hoavisoa Misandratra (ETU002556)

A lightweight **Java-based web framework** designed to handle servlet-based applications with custom annotations for routing, file uploads, data validation, and authentication management. It emphasizes extensibility and ease of use within servlet-compatible environments.

---

## Table of Contents

- [Overview](#overview)
- [Requirements](#requirements)
- [Features](#features)
- [Project Timeline (Sprints)](#project-timeline-sprints)
- [Test Project (Usage Example)](#test-project-usage-example)
- [Setup Instructions (Compiling the Framework)](#setup-instructions-compiling-the-framework)
- [Deploying the Test Project](#deploying-the-test-project)
- [Usage Notes](#usage-notes)
- [Bug Reports](#bug-reports)

---

## Overview

The Handmade Framework is a custom-built Java web framework that simplifies the development of servlet-based applications. It provides features like custom annotations for routing, file uploads, form validation, and authentication/authorization management. The framework is designed to be extensible and easy to integrate into servlet-compatible servers.

**Warning**: In case there is an error in the compilation of the classes, please delete the duplicated classes that do not match the package structure.

---

## Requirements

To use the Handmade Framework, you need:

- **Java**: Version 8 or higher
- **Servlet-Compatible Server**: A server capable of running servlet applications (e.g., Apache Tomcat, Jetty)
- **Dependencies**: Gson library for JSON serialization (included in Sprint 9)

---

## Features

The framework offers a comprehensive set of tools for modern web development:

- **Custom Annotations**: Define controllers, routes, parameters, and authentication with annotations like `@Url`, `@Get`, `@Post`, `@Restapi`, `@Param`, `@ModelAttribute`, `@Authentified`, and `@Role`.
- **File Upload Handling**: Upload and manage files using the `FileUpload` class, including methods for getting file type, content, and saving to a specified folder.
- **Data Validation**: Validate form inputs using a dedicated `Validator` class and annotations:
  - `@NotNull`: Ensures a field is not null.
  - `@Email`: Validates the field is a properly formatted email address.
  - `@Range`: Checks if a numeric value is within a specified range [min/max](cite: 1).
  - `@Length`: Enforces a maximum length for a string field.
- **Form Validation Management**: Dedicated classes (`MessageValue.java`, `FormValidation.java`) manage form errors, allowing for the resending of users to forms with error messages and old input values.
- **Authentication & Authorization**: Secure methods and entire classes using:
  - `@Authentified`: Requires an authenticated user.
  - `@Role`: Restricts access based on user role[s](cite: 1).
- **REST API Support**: Handle JSON responses with the `@Restapi` annotation and utilize the `ModelView.toJson()` method [requires Gson](cite: 1).
- **Session Management**: Use `MySession` for session-based operations, including a `reset()` function to delete all session data.
- **Error Handling**: Custom exceptions with `MyException` allow for detailed error reporting, including custom error codes and the ability to specify the underlying cause of an exception.
- **Model/View Utilities**: `ModelView` helps prepare the view, and also handles the `base_url` configuration from `web.xml` for universal redirection and includes a `DecimalFormat` (`formatter`) for US-style number formatting in views.
- **Data Type Support**: Enhanced support for parameter and field types including `int`, `double`, `float`, `String`, `java.sql.Date`, `Time`, `Timestamp`, and `boolean`.

---

## Project Timeline (Sprints)

### Sprint 0-10: Foundation and Core Routing

- **Sprint 0**: Initial Setup with `FrontController.java` and `compilate.bat`.
- **Sprint 1**: Core Controller Logic with `AnnotationController.java`, `ControllerScanner.java`.
- **Sprint 2**: Annotation organization and addition of `Mapping.java`.
- **Sprint 3**: Reflection utility (`Refflect.java`) and `invokeMappedMethod`.
- **Sprint 4**: Model-View Support with `ModelView.java` and return type verification.
- **Sprint 5**: Enhanced error checks for package scanning, duplicate URLs, and request handling.
- **Sprint 6**: Parameter handling with `@Param` and reflection logic for parameters without an annotation.
- **Sprint 7**: Model binding with `@ModelAttribute` and `@ModelField`.
- **Sprint 8**: Session management with `MySession.java` and enhanced reflection methods.
- **Sprint 9**: REST API Support with `@Restapi`, `ModelView.toJson()`, and content type handling.
- **Sprint 10**: HTTP Verb Annotations (`@Url` renamed from `Get.java`), `@Get`, and `@Post` with verb validation in `FrontController`.

### Sprint 11: Refactoring and Custom Exceptions

- **VerbAction**: Created `VerbAction.java` to map method names to HTTP verbs.
- **Mapping**: Refactored `Mapping.java` to use `Set<VerbAction> ls_verbAction` for multiple methods per URL.
- **Exceptions**: Introduced `MyException.java` with custom error codes.
- **Refactoring**: Created `FrontControllerMethod.java` to separate method-related logic from `FrontController.java`.

### Sprint 12: File Uploads

- **URL Concatenation**: `FrontControllerMethod` now concatenates `AnnotationController` name with `Url` annotation value.
- **View Dispatching**: `ModelView.prepareModelView` adjusted for correct view pathing.
- **FileUpload**: Added `FileUpload.java` for handling file uploads using `Part` objects and methods for saving, getting type, and content.

### Sprint 13: Data Validation Setup

- **Type Checking**: Improved parameter and field type validation in `Mapping.java` to include `java.sql.Date` and `String`, throwing `IllegalStateException` for unsupported types.
- **Validation Annotations**: Added `@NotNull`, `@Email`, `@Range`, and `@Length`.
- **Validator**: Created `Validator.java` to centralize field validation logic.

### Sprint 14: Form Validation Management

- **Error Objects**: Added `MessageValue.java` (error message + old input value) and `FormValidation.java` [list of errors](cite: 1).
- **Error Handling**: `MyException.java` constructor updated to include an `exception_cause`.
- **Form Resubmission**: `Mapping.java` updated to capture validation errors in `FormValidation`, add submitted values, and forward the user back to the form with errors, preventing multiple forwards.

### Sprint 15-16: Authentication & Authorization

- **Sprint 15 (Method-Level)**: Added `@Authentified` and `@Role` annotations and implemented `verifyMethodPermission` in `Mapping.java`.
- **Sprint 16 (Class-Level)**: Modified `@Authentified` and `@Role` to be applicable at the class level, implemented `verifyClassPermission`.

### Sprint 17: Rectifications and Improvements

- **Data Types**: Added support for `boolean`, `Time`, and `Timestamp` in parameter handling.
- **ModelView**: Rectified `prepareModelView` for the root route (`/`) and to ensure universal pathing by changing `../` to `/`. Added `base_url` handling from `web.xml` for redirection. Added `DecimalFormat` (`formatter`) to request attributes for decimal number display.
- **Validation**: Corrected logic in `Validator.rangeCheck` for handling empty min/max values.
- **Model**: Added handling for the `@Ignored` annotation to skip field processing and `@DefaultNull` to set a field to `null` by default.
- **Session**: Added `MySession.reset()` to clear session data.
- **Error Handling**: Updated `FrontController.doGet/doPost` to include a button to return to the `base_url` on exception.

### Sprint 18: Rectification and Improvement no.2

- **Package Refactoring**: Modified all classes from `src` to use the package name `com.thekingjoker18.handmade_framework` to prevent conflicts when the framework is used as a library.
- **Test Project Uploaded**: Uploaded a complete test project to the GitHub repository to provide a tangible example of how to use the framework.
- **Web.xml Example**: Updated the `web.xml.example` to reflect the new package name for the controller servlet.

---

## Test Project (Usage Example)

To see a practical, working example of how to use this framework, please see the **test project** included in the main GitHub repository.

This test project demonstrates:

- Controller setup
- Form handling (simple and model-based)
- File uploads
- Session management (login/logout)
- Data validation

It also includes a detailed **`HOWTOUSE.md`** file that guides you through the examples.

---

## Setup Instructions (Compiling the Framework)

Follow these steps to compile the framework into a `.jar` file, which you can then include in your own web projects.

1. **Clone the Repository**:

    ```bash
    git clone <repository-url>
    ```

2. **Install Java 8+**:
    Ensure Java 8 or higher is installed.

3. **Add Dependencies**:
    Ensure all dependencies (like `servlet-api.jar` and `gson.jar`) are available. The `compilate.bat` script expects them in `..\test\lib`.

4. **Compile the Framework**:

    - **On Windows**:
        1. Navigate to the `development` folder (where `compilate.bat` is located).
        2. **Important:** Before running, you **must edit `compilate.bat`** and change the `lib` and `destination` variables to point to your project's `lib` folder.
        3. Run the `compilate.bat` script.
        4. This will compile the Java source files, create the `handmade_framework.jar` file, and copy it to your specified `destination` folder[cite: 4].

    - **On Linux/macOS**:
        Run the following commands from the framework's root directory.

        **Note:** You must adjust the `LIB_DIR` and `DESTINATION_DIR` variables to match your project's paths.

        ```bash
        #!/bin/bash
        
        # --- CONFIGURATION ---
        # Adjust these paths to your dependencies and target project
        LIB_DIR="../test/lib" 
        DESTINATION_DIR="../test/lib"
        # ---------------------

        SRC_DIR="./src/com/thekingjoker18/handmade_framework"
        BIN_DIR="./bin"
        JAR_NAME="handmade_framework.jar"

        # Clean and create bin directory
        echo "Cleaning old build..."
        rm -rf "$BIN_DIR"
        mkdir -p "$BIN_DIR"

        # Find all java files and compile them
        echo "Compiling source files..."
        find "$SRC_DIR" -name "*.java" > sources.txt
        javac --release 8 -d "$BIN_DIR" -cp "$LIB_DIR/*" @sources.txt
        rm sources.txt

        # Create the JAR file
        echo "Creating JAR file..."
        jar cvf "$JAR_NAME" -C "$BIN_DIR" .

        # Copy JAR to destination
        echo "Copying $JAR_NAME to $DESTINATION_DIR..."
        cp "$JAR_NAME" "$DESTINATION_DIR/"

        echo "Compilation complete."
        ```

5. **Add to Your Project**:
    Place the generated `handmade_framework.jar` file into the **`WEB-INF/lib`** directory of the web application project you are developing.

    **NB**: Don't forget to verify if your **`web.xml`** follows the given example on the 'main' repository for proper framework configuration.

---

## Deploying the Test Project

The included test project has its own deployment script, `deploiement.bat`, to automate building and deploying it to a Tomcat server.

1. **Compile the Framework First**:
    Before deploying the test project, you must first compile the framework (see steps above) and ensure the `handmade_framework.jar` is in the test project's `lib` folder.

2. **Configure Deployment Script**:
    - Navigate to the test project's root folder.
    - **Important:** Open `deploiement.bat` in a text editor.
    - You **must change the `webapps` variable** to match the `webapps` directory of your Tomcat (or other) server.
    - Example: `set "webapps=C:\Program Files\Apache-Tomcat-9.0\webapps"`

3. **Run the Deployment Script**:
    - Execute `deploiement.bat`.
    - The script will:
        1. Create a temporary staging directory.
        2. Copy your test project's web files (JSPs, CSS), `web.xml`, and all libraries (including the framework `.jar`) into it.
        3. Compile your test project's `.java` files (controllers, models, servlets).
        4. Bundle everything into a `.war` file.
        5. Copy the `.war` file directly to your server's `webapps` directory[cite: 2], where it will be automatically deployed.

---

## Usage Notes

- **Compilation Errors**: If you encounter duplicate class errors, remove any conflicting classes that do not match the package structure.
- **Annotations**: Use `@Url`, `@Get`, `@Post`, and `@Restapi` to define routes and HTTP methods.
- **Validation**: Apply `@NotNull`, `@Email`, `@Range`, and `@Length` to enforce data constraints.
- **File Uploads**: Use `FileUpload` for handling file uploads in controller methods.
- **Authentication**: Secure methods or classes with `@Authentified` and `@Role`.
- **ModelView**: Access the request attribute `formatter` (a `DecimalFormat`) in your JSPs to format decimal numbers using the US style (`#,###.##`).

---

## Bug Reports

For bug reports or feature requests, please open an issue on the project's repository.
