package ru.fanur.mapper;

import ru.fanur.dto.response.FileDtoOut;
import ru.fanur.model.File;

public interface FileMapper {
    FileDtoOut toDtoOut(File file);
}