package dev.mark.factoria_tech_test.images;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface IStorageService {

    void init();

    Path resolvePath(String filename);

    void saveImage(MultipartFile file, String imageName);

	Resource loadAsResource(String filename);

    public boolean delete(String filename) throws IOException;

    public String createUniqueName(MultipartFile file);

}
