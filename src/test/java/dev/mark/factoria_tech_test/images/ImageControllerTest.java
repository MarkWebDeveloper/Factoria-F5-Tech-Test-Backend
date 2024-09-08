package dev.mark.factoria_tech_test.images;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.mark.factoria_tech_test.users.UserRepository;

@WebMvcTest(controllers = ImageController.class)
@AutoConfigureMockMvc(addFilters = false) // disable security
@TestPropertySource(properties = {
    "DATASOURCE_PASSWORD=1234",
	"DATASOURCE_USERNAME=name",
	"DEFAULT_ADMIN_NAME=name",
	"DEFAULT_ADMIN_PASSWORD=1234"
})
public class ImageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ImageService imageService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    IStorageService storageService;

    @Mock
    MultipartFile file;

    @Test
    void test_getImages_ShouldGetCurrentUserImages_ReturnOK() throws JsonProcessingException, Exception {

        List<Image> images = new ArrayList<>();
        Image landscape = Image.builder().id(1L).imageName("landscape.jpg").imageTitle("Landscape").build();
        Image cat = Image.builder().id(1L).imageName("cat.jpg").imageTitle("Cat").build();
        images.add(landscape);
        images.add(cat);

        when(imageService.getCurrentUserImages()).thenReturn(images);
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/any/images/getCurrentUserImages")
                .accept(MediaType.APPLICATION_JSON)
                .content("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), containsString("Landscape"));
        assertThat(response.getContentAsString(), containsString("Cat"));
        assertThat(response.getContentAsString(), equalTo(mapper.writeValueAsString(images)));
    }

    @Test
    void test_uploadImage_ShouldSaveImage_ReturnOK() throws JsonProcessingException, Exception {

        Image landscape = Image.builder().id(1L).imageName("landscape.jpg").imageTitle("Landscape").build();

        String imageTitle = "Landscape";
        String uniqueImageName = "landscape.jpg";
        when(storageService.createUniqueName(file)).thenReturn(uniqueImageName);

        when(imageService.saveImage(file, imageTitle, uniqueImageName)).thenReturn(landscape);

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", file.getOriginalFilename(), file.getContentType(), file.getInputStream());
        mockMvc.perform(MockMvcRequestBuilders.multipart("http://localhost:8080/api/v1/any/images/uploadImage")
                        .file(mockMultipartFile)
                        .param("imageTitle", imageTitle))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
