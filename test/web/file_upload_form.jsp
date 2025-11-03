<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>File Upload Form Test</title>
    <link rel="stylesheet" href="/public/assets/css/water.css">
</head>
<body>
    <center>
        <h1>File Upload Form Test</h1>
        <p>This is a test of the File Upload Form Controller</p>
        <form method="post" action="file_upload_form_test" enctype="multipart/form-data">
            <label for="file">Choisir un Fichier</label>
            <input type="file" id="file" name="file">
            <label for="name">Name: </label>
            <input type="text" id="name" name="name">
            <input type="submit" value="Upload">
        </form>
    </center>
</body>
</html>
