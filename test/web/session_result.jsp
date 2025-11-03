<%@page import="utils.MySession"%>
<%@page import="model.Account"%>
<%@page import="java.util.Vector"%>
<% MySession mysession = (MySession) request.getAttribute("mysession");
if (mysession.get("account") != null) {
    Account account = (Account) mysession.get("account");
    Vector<String> ls_projectNames = (Vector<String>) mysession.get("ls_projectNames");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Session Result Test</title>
</head>
<body>
    <center>
        <h1>Session Result Test</h1>
        <p>This is a test of the Session Result Controller</p>
        <p>Welcome dear person with the email <b>"<%= account.getEmail() %>"</b> </p>
        <table border="1" >
            <thead>
                <tr>
                    <th>Project ID</th>
                    <th>Project Name</th>
                </tr>
            </thead>
            <tbody>
            <% if (ls_projectNames != null) {
                for (int i = 0; i < ls_projectNames.size(); i++) { %>
                <tr>
                    <td><%= (i + 1) %></td>
                    <td><%= ls_projectNames.get(i) %></td>
                </tr>
            <% } 
            } %>
            </tbody>
        </table>
        <p> <a href="session_home_test"> <button>Return Home</button> </a> </p>
    </center>
</body>
</html>

<% } else { %>
<script type="text/javascript">
    alert("Please enter a valid account to enter this site");
    window.location.href = "session_form_test";
</script>
<% } %>