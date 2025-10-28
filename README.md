# Handmade Framework

**Author**: RANAIVOSON NY Hoavisoa Misandratra (ETU002556)

A lightweight **Java-based web framework** designed to handle servlet-based applications with custom annotations for routing, file uploads, data validation, and authentication management. It emphasizes extensibility and ease of use within servlet-compatible environments.

---

## Table of Contents

- [Overview](#overview)
- [Requirements](#requirements)
- [Features](#features)
- [Project Timeline (Sprints)](#project-timeline-sprints)
- [Setup Instructions](#setup-instructions)
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

---

## Setup Instructions

1. **Clone the Repository**:

    ```bash
    git clone <repository-url>
    ```

2. **Install Java 8+**:
    Ensure Java 8 or higher is installed.

3. **Set Up a Servlet-Compatible Server**:
    Deploy the application on a server like Apache Tomcat.

4. **Add Dependencies**:
    Include the Gson library for JSON support. Ensure all dependencies (like `servlet-api.jar`) are available in the library folder referenced by the compile script (e.g., `../test/lib` as seen in `compilate.bat` [cite: 2]).

5. **Compile the Framework**:

    - **On Windows**:
        1. Navigate to the `development` folder (where `compilate.bat` is located).
        2. Run the `compilate.bat` script[cite: 2].
        3. This will compile the Java source files from `src` into the `bin` folder [cite: 2], create the `handmade_framework.jar` file [cite: 2], and clean up temporary files[cite: 2].

    - **On Linux/macOS**:
        You can run the following equivalent shell commands from the framework's root directory. (Note: You may need to adjust the `LIB_DIR` variable to point to your dependencies).

        ```bash
        #!/bin/bash
        
        # Define variables
        SRC_DIR="./src"
        BIN_DIR="./bin"
        LIB_DIR="../test/lib" # Adjust this path to your dependencies
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

        echo "Compilation complete: $JAR_NAME created."
        ```

6. **Deploy and Run**:
    1. Take the `handmade_framework.jar` file generated in the previous step.
    2. Place this `.jar` file into the **`WEB-INF/lib`** directory of the web application project you are developing.
    3. Deploy your web application to your servlet container (e.g., Tomcat) and run it.

    **NB**: Don't forget to verify if your **`web.xml`** follows the given example on the 'main' repository for proper framework configuration.

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
