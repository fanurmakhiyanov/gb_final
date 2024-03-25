package ru.fanur.service;

import ru.fanur.dto.response.FileDtoOut;
import ru.fanur.exception.FileNotFoundException;
import ru.fanur.exception.UploadFileException;
import ru.fanur.model.File;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface FileStorageService {
    File getFile(String filename) throws FileNotFoundException;
    void addFile(String filename, MultipartFile file) throws UploadFileException;
    void removeFile(String filename) throws FileNotFoundException;
    void putFile(String oldName, String newName) throws FileNotFoundException;
    List<FileDtoOut> getFiles(Integer limit);
}