package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int addFile(MultipartFile file, Integer userId) {
        File fileToSave = new File();
        try {
            fileToSave.setFilename(file.getOriginalFilename());
            fileToSave.setContenttype(file.getContentType());
            fileToSave.setFilesize(String.valueOf(file.getSize()));
            fileToSave.setUserid(userId);
            fileToSave.setFiledata(file.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return fileMapper.insert(fileToSave);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public List<File> getFilesByUserId(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public int deleteFile(Integer fileId) {
        return fileMapper.deleteFile(fileId);
    }

}