# Handmade Framework

**Author**: RANAIVOSON NY Hoavisoa Misandratra (ETU002556)

A lightweight Java-based web framework designed to handle servlet-based applications with custom annotations, file uploads, data validation, and authentication management.

---

## Table of Contents
- [Overview](#overview)
- [Requirements](#requirements)
- [Features](#features)
- [Project Timeline (Sprints)](#project-timeline-sprints)
- [Setup Instructions](#setup-instructions)
- [Usage Notes](#usage-notes)
- [Contributing](#contributing)

---

## Overview
The Handmade Framework is a custom-built Java web framework that simplifies the development of servlet-based applications. It provides features like custom annotations for routing, file uploads, form validation, and authentication/authorization management. The framework is designed to be extensible and easy to integrate into servlet-compatible servers.

**Note**: If compilation errors occur due to duplicated classes, ensure that only the correct classes matching the package structure are retained.

---

## Requirements
To use the Handmade Framework, you need:
- **Java**: Version 8 or higher
- **Servlet-Compatible Server**: A server capable of running servlet applications (e.g., Apache Tomcat, Jetty)
- **Dependencies**: Gson library for JSON serialization (included in Sprint 9)

---

## Features
- **Custom Annotations**: Define controllers, routes, parameters, and authentication with annotations like `@Url`, `@Get`, `@Post`, `@Restapi`, `@Param`, `@ModelAttribute`, `@Authentified`, and `@Role`.
- **File Upload Handling**: Upload and manage files with the `FileUpload` class.
- **Data Validation**: Validate form inputs using annotations like `@NotNull`, `@Email`, `@Range`, and `@Length`.
- **Authentication & Authorization**: Secure methods and classes with `@Authentified` and `@Role` annotations.
- **REST API Support**: Handle JSON responses with `@Restapi` annotation.
- **Session Management**: Use `MySession` for session-based operations.
- **Error Handling**: Custom exceptions with `MyException` for detailed error reporting.

---

## Project Timeline (Sprints)

### Sprint 0: Initial Setup
- Uploaded `FrontController.java` and `compilate.bat`.

### Sprint 1: Core Controller Logic
- Added `AnnotationController.java`, `ControllerScanner.java`, and updated `FrontController.java`.

### Sprint 2: Annotation Organization
- Grouped annotations in the `annotation` package.
- Added `Mapping.java` to the `utils` package.
- Modified `FrontController.java`.

### Sprint 3: Reflection Enhancements
- Added `Refflect.java`.
- Introduced `invokeMappedMethod` in `FrontController.java`.

### Sprint 4: Model-View Support
- Added `ModelView.java`.
- Enhanced `processRequest` in `FrontController.java` to verify return types.

### Sprint 5: Error Handling
- Improved `FrontController.java` with package scanning and URL mapping checks.

### Sprint 6: Parameter Annotations
- Added `@Param` annotation.
- Modified `Reflect.java` (added `getMethodByName`).
- Updated `Mapping.java` to handle parameters without `@Param` and null value exceptions.

### Sprint 7: Model Annotations
- Added `@ModelAttribute` and `@ModelField` annotations.
- Refactored `invokeMethod` in `Mapping.java` with `configParam`, `setSimpleParam`, and `setModelParam`.

### Sprint 8: Session Management
- Added `MySession.java`.
- Enhanced `Reflect.java` with methods: `checkFieldByType`, `getFieldValueByType`, `invokeGetterMethod`, and `invokeControllerConstructor`.
- Updated `Mapping.java` methods: `invokeMethod`, `configParam`, and `setSimpleParam`.

### Sprint 9: REST API Support
- Added `@Restapi` annotation.
- Modified `Mapping.java` to support JSON responses with `checkIfMethodHaveRestapiAnnotation` and split `execute` into `execute_html` and `execute_json`.
- Updated `ModelView.java` with `toJson()` using Gson.
- Enhanced `FrontController.java` with `executeControllerMethod`.

### Sprint 10: HTTP Verb Annotations
- Renamed `Get.java` to `Url.java`.
- Added `@Get` and `@Post` annotations for HTTP verb specification.
- Updated `Mapping.java` with `verb` field and modified `toString()`.
- Modified `FrontController.java` to validate HTTP verbs.

### Sprint 11: Exception and Method Refactoring
- Created `VerbAction.java` for verb-method mapping.
- Replaced `Mapping.java` fields with `Set<VerbAction> ls_verbAction` and `VerbAction action`.
- Added `MyException.java` for custom error codes.
- Created `FrontControllerMethod.java` to separate method-related logic from `FrontController.java`.
- Updated `FrontController.java` with exception handling for `doGet` and `doPost`.

### Sprint 12: File Uploads
- Modified `FrontControllerMethod.java` to concatenate controller and URL annotations.
- Updated `ModelView.java` to handle view dispatching.
- Added `FileUpload.java` for file handling with methods: `getFileUploadedfromFilePart`, `getFileType`, `getContent`, and `saveTo`.
- Updated `Mapping.java` to handle `FileUpload` parameters.

### Sprint 13: Data Validation
- Enhanced `Mapping.java` to validate parameter types (`java.sql.Date`, `String`) and throw `IllegalStateException` for unsupported types.
- Added validation annotations: `@NotNull`, `@Email`, `@Range`, `@Length`.
- Created `Validator.java` for field validation with methods: `check`, `notNullCheck`, `emailCheck`, `rangeCheck`, and `lengthCheck`.
- Updated `Mapping.java` to use `Validator` in `setModelParam`.

### Sprint 14: Form Validation
- Added constructor to `MyException.java` for exception causes.
- Created `MessageValue.java` and `FormValidation.java` for form error management.
- Updated `Mapping.java` to handle form validation errors, resend users to forms, and avoid multiple forwards.

### Sprint 15: Method-Level Authentication
- Added `@Authentified` and `@Role` annotations for method-level security.
- Modified `Mapping.java` with `verifyMethodPermission` to check authentication and roles.

### Sprint 16: Class-Level Authentication
- Modified `@Authentified` and `@Role` to support class-level annotations.
- Added `verifyClassPermission` in `Mapping.java` for class-level security checks.

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
   Include the Gson library for JSON support (used in Sprint 9).
5. **Compile the Application**:
   Use the provided `compilate.bat` script or compile manually with:
   ```bash
   javac -cp .;<path-to-servlet-api>;<path-to-gson> *.java
   ```
6. **Deploy and Run**:
   Deploy the compiled application to your servlet container and access it via the configured URL.

---

## Usage Notes
- **Compilation Errors**: If you encounter duplicate class errors, remove any conflicting classes that do not match the package structure.
- **Annotations**: Use `@Url`, `@Get`, `@Post`, and `@Restapi` to define routes and HTTP methods.
- **Validation**: Apply `@NotNull`, `@Email`, `@Range`, and `@Length` to enforce data constraints.
- **File Uploads**: Use `FileUpload` for handling file uploads in controller methods.
- **Authentication**: Secure methods or classes with `@Authentified` and `@Role`.

---

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit (`git commit -m 'Add feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

For bug reports or feature requests, please open an issue.

---