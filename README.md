### handmade_framework

ETU002556 RANAIVOSON NY Hoavisoa Misandratra

`Warning: In case there is an error in the compilation of the classes, please delete the duplicated classes that do not match if the package`

Historics of the pull requests:

## Sprint 0: FrontController.java and compilate.bat upload

## Sprint 1

AnnotationController.java, ControllerScanner.java, FrontController.java and compilate.bat upload

## Sprint 2

- Regroupement des annotations dans le package annotation Modification de FrontController
- Ajout de Mapping.java dans le package utils

## Sprint 3: Adding Refflect.java and a new function in FrontController

The new funciton is Object invokeMappedMethod(Mapping mapping)

## Sprint 4: Adding ModelView.java and a verification ...

for the returned value in processRequest of FrontController.java to verify its Class

## Sprint 5: Modification in FrontController.java

Adding checks errors for the package scan and the Mappings with same url in build, and for the returned object class and the unknown url in request

## Sprint 6: Adding annotation Param

Modifying Reflect.java (adding method getMethodByName) and Mapping.java (modifying method invokeMethod)

## Sprint 6: Modification on Mapping.java

Modifying the method invokeMethod so that when there is no annotation @param on the parameter of the method, it takes directly the parameter name and adding an exception when the value of the parameter is null

## Sprint 7: Adding ModelAttribute and ModelField annotations

Modifying the method invokeMethod in Mapping by refractorizing it with configParam, setSimpleParam, and setModelParam

## Sprint 8: Adding a new class MySession.java

- Modifying Reflect.java by adding the following method: checkFieldByType (to check if the object have the specified Type in its fields), getFieldValueByType, invokeGetterMethod and invokeControllerConstructor
- Modifying Mapping.java by modifying some of its method like invokeMethod, configParam, and setSimpleParam

## Sprint 9: Adding a new annotation Restapi.java

- Modifying Mapping.java by creating a new method "boolean checkIfMethodHaveRestapiAnnotation(HttpServletRequest request)", and separating the method "execute" in 2 parts: "execute_html" to execute the method without the annotation @Restapi and execute_json to execute the method when it has an annotation @Restapi
- Modifying ModelView.java by creating a new method "String toJson()" to have the json form of the ModelView by using the library Gson
- Modifying FrontController.java by creating a function "public String executeControllerMethod(Mapping mapping, HttpServletRequest request, HttpServletResponse response, String relativeURI)" to check if the method associated with the relativeURI has an annotation @Restapi, and to execute it and to define the response.contentType according to that

## Sprint 10: Adding new annotations Get.java and Post.java

- Modifying Get.java by changing its name to "Url.java" but keeping the field String value
- Creating two annotations @Get and @Post to help the users to precise the VERB (method) that they are going to use
- Modifying Mapping.java by adding a new field String verb to precise the VERB (method) defined by the user and modifying its method "String toString()" to show the said VERB
- Modifying FrontController.java by modifying its method "String executeControllerMethod(Mapping mapping, HttpServletRequest request, HttpServletResponse response, String relativeURI)" to check if the VERB used by the user matches with the VERB precised by the annotation @Get or @Post above the called method before executing the mapping, otherwise it throws an exception

## Modified Sprint 10 and Sprint 11: Creating MyException.java

- Creating "VerbAction.java" to put in it the method name and the verb mentioned in the user controller
- Modifying "Mapping.java" by replacing its fields String methodName and String verb with Set<VerbAction> ls_verbAction and VerbAction action where ls_verbAction is the list of methods related to the verbs and the url, and action is the used method for the current verb and url
- Creating "MyException.java" with the fields int error_code and String message to let the user add an error code for his thrown exception
- Creating "FrontControllerMethod.java" to separate the methods "void findMethodsAnnoted(Class<?> clazz, FrontController frontController)", "void initMethodList(FrontController frontController)", "String printMethodList(FrontController frontController)" and "String executeControllerMethod(Mapping mapping, HttpServletRequest request, HttpServletResponse response, String relativeURI, FrontController frontController)" from the class "FrontController.java"
- Modifying "FrontController.java" by moving the other methods except "void init(ServletConfig config) throws ServletException", "String processRequest(HttpServletRequest request, HttpServletResponse response)", "void doGet(HttpServletRequest req, HttpServletResponse res)" and "void doPost(HttpServletRequest req, HttpServletResponse res)", and adding an instance check for the Exception in catch (Exception e) of "doGet" and "doPost" in order to print the error code

## Sprint 12: Uploading files

- Modifying `FrontControllerMethod.class` by modifying its method `findMethodsAnnoted` so that the `name` provided by the annotation AnnotationController of the controller will be concatenate with the `value` ot the annotation Url of each of its methods
- Modifying `ModelView.class` by modifying its method `prepareModelView` by adding `../` before `url` when getting the RequestDispatcher to avoid that the framework will try to find the view in the AnnotationController name
- Creating `FileUpload.class` to handle a file by containing its `name` and its contents (in the form of `byte`)
- Modifying `FileUpload.class` by adding a static function `FileUpload getFileUploadedfromFilePart(Part filePart)` to handle the manipulation of the uploaded file
- Modifying `FileUpload.class` by adding a function `String getFileType()` to have the type of the uploaded file
- Modifying `FileUpload.class` by adding a function `String getContent()` to have the content of the uploaded file
- Modifying `FileUpload.class` by adding a function `void saveTo(String uploadFolder)` to save the uploaded file
- Modifying `Mapping.class` by modifying its function `void setSimpleParam(Object controller, int i, Parameter[] parameters, Object[] values, HttpServletRequest request)` to add an handler for `FileUpload` parameter when scanning and taking the `values` of the controller parameters
