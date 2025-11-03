<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Form Test</title>
    <link rel="stylesheet" href="/public/assets/css/water.css">
</head>
<body>
    <center>
        <h1>Form Test</h1>
        <p>This is a test of the Form Controller</p>
        <form action="form_test" method="post">
            <p> <label for="nom">Nom</label>     <input type="text" name="nom" id="nom"> </p>
            <p> <label for="age">Age</label>     <input type="number" name="age" id="age"> </p>
            <p> <input type="submit" value="Valider"> </p>
        </form>
    </center>
</body>
</html>