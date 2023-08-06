package com.example.music.Models;

public class LibraryModel {
    String filePath;
    String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LibraryModel(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
