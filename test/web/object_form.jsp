<%@page import="utils.FormValidation"%>
<%@page import="utils.MessageValue"%>
<%
    FormValidation formValidation = (FormValidation) request.getAttribute("formValidation");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Object Form Test</title>
</head>
<body>
    <center>
        <h1>Object Form Test</h1>
        <p>This is a test of the Object Form Controller</p>
        <form action="../user_controller/object_result_test" method="get">
            <input type="hidden" name="form_url" value="/user_controller/object_form_test">
            <h3>Account Model</h3>
                <p>
                <% if (formValidation != null && formValidation.hasErrors()) { %>
                    <span style="color: red;"><%= (formValidation.getErrorMessage("account.id") == null) ? "" : formValidation.getErrorMessage("account.id") %></span><br/>
                <% } %>
                    <label for="account.id">ID</label>
                    <input type="number" name="account.id" id="account.id" 
                        <%= (formValidation != null && formValidation.getOldValue("account.id") != null) ? "value=\"" + formValidation.getOldValue("account.id") + "\"" : "" %>
                    >
                </p>

                <p>
                <% if (formValidation != null && formValidation.hasErrors()) { %>
                    <span style="color: red;"><%= (formValidation.getErrorMessage("account.email") == null) ? "" : formValidation.getErrorMessage("account.email") %></span><br/>
                <% } %>
                    <label for="account.email">Email</label>
                    <input type="email" name="account.email" id="account.email" 
                        <%= (formValidation != null && formValidation.getOldValue("account.email") != null) ? "value=\"" + formValidation.getOldValue("account.email") + "\"" : "" %>
                    >
                </p>

                <p>
                <% if (formValidation != null && formValidation.hasErrors()) { %>
                    <span style="color: red;"><%= (formValidation.getErrorMessage("account.password") == null) ? "" : formValidation.getErrorMessage("account.password") %></span><br/>
                <% } %>
                    <label for="account.password">Password</label>
                    <input type="password" name="account.password" id="account.password" 
                        <%= (formValidation != null && formValidation.getOldValue("account.password") != null) ? "value=\"" + formValidation.getOldValue("account.password") + "\"" : "" %>
                    >
                </p>

            <h3>Independant Stuffs</h3>
                <p>
                    <label for="age">Age</label>
                    <input type="number" name="age" id="age"
                        <%= (formValidation != null && formValidation.getOldValue("age") != null) ? "value=\"" + formValidation.getOldValue("age") + "\"" : "" %>
                    >
                </p>
            <h3>Department Model</h3>
                <p>
                    <label for="department.name">Name</label>
                    <input type="text" name="department.name" id="department.name"
                        <%= (formValidation != null && formValidation.getOldValue("department.name") != null) ? "value=\"" + formValidation.getOldValue("department.name") + "\"" : "" %>
                    >
                </p>
                <p>
                    <label for="department.money">Money</label>
                    <input type="number" name="department.money" id="department.money"
                        <%= (formValidation != null && formValidation.getOldValue("department.money") != null) ? "value=\"" + formValidation.getOldValue("department.money") + "\"" : "" %>
                    >
                </p>
            <p> <input type="submit" value="Valider">
            </p>
        </form>
    </center>
</body>
</html>