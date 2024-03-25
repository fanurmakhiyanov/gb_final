package ru.fanur.service.impl;

import ru.fanur.dto.response.FileDtoOut;
import ru.fanur.exception.FileNotFoundException;
import ru.fanur.exception.UploadFileException;
import ru.fanur.mapper.FileMapper;
import ru.fanur.model.File;
import ru.fanur.repository.FileRepository;
import ru.fanur.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final FileRepository repository;
    private final FileMapper fileMapper;

    @Override
    public File getFile(String filename) throws FileNotFoundException {
        log.info("get file {}", filename);
        return repository.findByName(filename)
                .orElseThrow(() -> new FileNotFoundException("Error upload file"));
    }

    @Override
    public void addFile(String filename, MultipartFile file) throws UploadFileException {
        log.info("save new file {}", filename);
        try {
            var bytes = file.getBytes();
            var newFile = File.builder()
                    .content(bytes)
                    .name(filename)
                    .type(file.getContentType())
                    .build();
            repository.save(newFile);
        } catch (IOException e) {
            log.error("Error reading");
            throw new UploadFileException("Failed load file");
        }
    }

    @Override
    public void removeFile(String filename) throws FileNotFoundException {
        log.info("delete file {}", filename);
        var file = repository.findByName(filename)
                .orElseThrow(() -> new FileNotFoundException("Error delete file"));
        repository.deleteById(file.getId());
    }

    @Override
    public void putFile(String oldName, String newName) throws FileNotFoundException {
        log.info("replace old filename={} on new filename={}", oldName, newName);
        var file = repository.findByName(oldName)
                .orElseThrow(() -> new FileNotFoundException("Error upload file"));
        file.setName(newName);
        repository.save(file);
    }

    @Override
    public List<FileDtoOut> getFiles(Integer limit) {
        log.info("getting all files in the amount={}", limit);
        return repository.findAll(Pageable.ofSize(limit))
                .toList().stream()
                .map(fileMapper::toDtoOut)
                .collect(Collectors.toList());
    }
}