<%@page import="utils.MySession"%>
<%@page import="model.Account"%>
<% MySession mysession = (MySession) request.getAttribute("mysession");
if (mysession.get("account") != null) {
    Account account = (Account) mysession.get("account");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Session Home Test</title>
</head>
<body>
    <center>
        <h1>Session Home Test</h1>
        <p>This is a test of the Session Home Controller</p>
        <p>Welcome dear person with the email <b>"<%= account.getEmail() %>"</b> </p>
        <p> <a href="session_disconnect_test"> <button>Disconnect</button> </a> </p>
        <form action="session_add_test" method="post">
            <h3>Adding new Project</h3>
                <p> <label for="projectName">Project name: </label>     <input type="text" name="projectName" id="projectName"> </p>
            <p> <input type="submit" value="Valider"> </p>
        </form>
        <p> <a href="session_result_test"> <button>View Project List</button> </a> </p>
    </center>
</body>
</html>

<% } else { %>
<script type="text/javascript">
    alert("Please enter a valid account to enter this site");
    window.location.href = "session_form_test";
</script>
<% } %>