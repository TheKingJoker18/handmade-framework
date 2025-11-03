<%@page import="model.Account"%>
<%@page import="model.Department"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Object Result Test</title>
</head>
<body>
    <center>
        <h1>Object Result Test</h1>
        <p>This is a test of the Object Result Controller</p>
        <h4> <%= ((Account) request.getAttribute("account")).toString() %>, <br/> age : <%= (int) request.getAttribute("age") %>, <br/> <%= ((Department)request.getAttribute("department")).toString() %> </h4>
    </center>
</body>
</html>

<script type="text/javascript"> alert("<%= ((Account) request.getAttribute("account")).toString() %>,\n age : <%= (int) request.getAttribute("age") %>,\n <%= ((Department)request.getAttribute("department")).toString() %>"); </script>