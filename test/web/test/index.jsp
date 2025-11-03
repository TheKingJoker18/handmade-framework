<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Index</title>
</head>
<body>
    <center>
        <h1>Index</h1>
        <p>This is a test of the project index (root: "/")</p>
        <h4> <%= request.getAttribute("message") %> </h4>
    </center>
</body>
</html>

<script type="text/javascript"> alert("<%= request.getAttribute("message") %>"); </script>