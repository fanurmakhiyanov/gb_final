package ru.fanur.controller;

import ru.fanur.dto.response.FileDtoOut;
import ru.fanur.dto.request.LoginPasswordHash;
import ru.fanur.exception.FileNotFoundException;
import ru.fanur.exception.UploadFileException;
import ru.fanur.service.FileStorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
public class CloudDiskController {
    private final FileStorageService storage;

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> getFile(@RequestParam @NotBlank String filename) throws FileNotFoundException {
        log.info("endpoint: GET /file");
        var file = storage.getFile(filename);
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(file.getType()))
                .body(new ByteArrayResource(file.getContent()));
    }

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveFile(@RequestParam @NotBlank String filename,
                                         @RequestParam("file") MultipartFile file) throws UploadFileException
    {
        log.info("endpoint: POST /file");
        storage.addFile(filename, file);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestParam @NotBlank String filename) throws FileNotFoundException {
        log.info("endpoint: DELETE /file");
        storage.removeFile(filename);
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping("/file")
    public ResponseEntity<Void> updateFile(@RequestParam("filename") @NotBlank String oldFilename,
                                           @RequestBody @Valid LoginPasswordHash loginPasswordHash) throws FileNotFoundException
    {
        log.info("endpoint: PUT /file");
        storage.putFile(oldFilename, loginPasswordHash.filename());
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/list")
    public List<FileDtoOut> getFiles(@RequestParam @Positive Integer limit) {
        log.info("endpoint: GET /list");
        return storage.getFiles(limit);
    }
}