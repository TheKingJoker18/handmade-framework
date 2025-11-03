<%@page import="utils.MySession"%>
<%@page import="model.Account"%>
<% MySession mysession = (MySession) request.getAttribute("mysession");
if (mysession.get("account") == null) {
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Session Form Test</title>
</head>
<body>
    <center>
        <h1>Session Form Test</h1>
        <p>This is a test of the Session Form Controller</p>
        <form action="session_login_test" method="post">
            <h3>Login</h3>
                <input type="hidden" name="account.id" id="account.id" value="69">
                <p> <label for="account.email">Email</label>     <input type="email" name="account.email" id="account.email"> </p>
                <p> <label for="account.password">Password</label>     <input type="password" name="account.password" id="account.password"> </p>
            <p> <input type="submit" value="Valider"> </p>
        </form>
    </center>
</body>
</html>

<% } else { %>
<script type="text/javascript">
    alert("You have already a connected account");
    window.location.href = "session_home_test";
</script>
<% } %>