<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Result Test</title>
    <link rel="stylesheet" href="/public/assets/css/water.css">
</head>
<body>
    <center>
        <h1>Result Test</h1>
        <p>This is a test of the Result Controller</p>
        <h4> Nom : <%= (String) request.getAttribute("nom") %> </h4>
        <h4> Age : <%= (int) request.getAttribute("age") %> </h4>
    </center>
</body>
</html>

<script type="text/javascript"> alert("Nom : <%= (String) request.getAttribute("nom") %> \n Age : <%= (int) request.getAttribute("age") %>"); </script>