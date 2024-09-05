package dev.mark.factoria_tech_test.images;

import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface IStorageService {

    void init();

    List<Image> getCurrentUserImages();

    Path resolvePath(String filename);

    void saveImage(MultipartFile file, @NonNull String imageTitle);

	Resource loadAsResource(String filename);

    public boolean delete(String filename);

}
