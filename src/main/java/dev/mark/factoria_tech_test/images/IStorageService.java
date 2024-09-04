package dev.mark.factoria_tech_test.images;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface IStorageService {

    void init() throws StorageException;

    Path createPath(String filename);

    void saveImage(MultipartFile file, @NonNull String imageTitle) throws StorageException;

	Resource loadAsResource(String filename) throws StorageException;

    public boolean delete(String filename);

}
