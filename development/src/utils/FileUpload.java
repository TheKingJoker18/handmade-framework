package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.http.Part;

public class FileUpload {
    String name;
    byte[] bytes;

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return this.bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public FileUpload() {}
    public FileUpload(String name, byte[] bytes) {
        this.setName(name);
        this.setBytes(bytes);
    }

    public static FileUpload getFileUploadedfromFilePart(Part filePart) throws IOException {
        // Obtenir le nom du fichier téléchargé
        String fileName = filePart.getSubmittedFileName();
        
        // Lire le contenu du fichier dans un tableau de bytes
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;
        
        // Ouvrir le flux de données du fichier
        InputStream inputStream = filePart.getInputStream();
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);  // Écrire les données dans le buffer
        }
        
        // Convertir le contenu du fichier en un tableau de bytes
        byte[] fileBytes = buffer.toByteArray();
        
        // Créer et retourner une instance de FileUploaded avec le nom et les bytes du fichier
        return new FileUpload(fileName, fileBytes);
    }

    public String getFileType() {
        // Détection du type de fichier en fonction de son extension ou de son contenu
        String fileName = this.getName().toLowerCase();

        if (fileName.endsWith(".txt") || fileName.endsWith(".csv") || fileName.endsWith(".json") || fileName.endsWith(".md")) {
            return "text";  // Si le fichier est un fichier texte
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
            return "image"; // Si le fichier est une image
        }
        // Ajouter d'autres types de fichiers ici selon les besoins
        return "unknown";  // Si le type est inconnu
    }

    public String getContent() throws IOException {
        // Vérification de l'extension ou du type MIME pour déterminer le type du fichier
        String fileType = this.getFileType(); // méthode pour obtenir le type du fichier (texte, image, etc.)

        if ("text".equals(fileType)) {
            // Si le fichier est textuel, on le retourne en tant que String
            return new String(this.bytes, StandardCharsets.UTF_8);
        } else if ("image".equals(fileType)) {
            // Si le fichier est une image, on retourne une version encodée en base64
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(this.bytes);
        } else {
            // Retourne un message générique ou une autre représentation pour les fichiers non pris en charge
            return "File not supported";
        }
    }

    public void saveTo(String uploadFolder) throws IOException {
        if (this.bytes == null || this.name == null || this.name.isEmpty()) {
            throw new IOException("No file to save.");
        }

        // Créer le dossier s'il n'existe pas
        File directory = new File(uploadFolder);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Impossible to create the destination directory.");
            }
        }

        // Créer le fichier avec le chemin complet
        File file = new File(directory, this.name);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(this.bytes);
        }
    }

}
