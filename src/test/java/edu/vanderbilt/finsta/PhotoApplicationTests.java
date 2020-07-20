package edu.vanderbilt.finsta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@WebMvcTest(PhotoController.class)
public class PhotoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testPhoto() throws Exception {
        String result = mockMvc.perform(get("/photo?url=http://www.magnum.io/img/magnum.png&tag=vandy"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Assert.isTrue(result.equals("true"));
    }

    @Test
    public void testGetPhotoByTag() throws Exception {
        String result = mockMvc.perform(get("/photo/vandy"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Assert.isTrue(result.endsWith("\"url\":\"http://www.magnum.io/img/magnum.png\"}]"));
    }

    @Test
    public void testGetTags() throws Exception {
        String result = mockMvc.perform(get("/tag"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Assert.isTrue(result.endsWith("[\"vandy\"]"));
    }

    @Test
    public void testSearchPhotosByTagPrefix() throws Exception {
        String result = mockMvc.perform(get("/photo/search?tagPrefix=vandy"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Assert.isTrue(result.endsWith("\"url\":\"http://www.magnum.io/img/magnum.png\"}]"));
    }

//    @Test
//    public void testBoom() throws Exception {
//        String result = mockMvc.perform(get("/photo/search/stream"))
//            .andExpect(status().isOk())
//            .andReturn().getResponse().getContentAsString();
//        System.out.println("#####################################");
//        System.out.println(result);
//        Assert.isTrue(result.endsWith());
//    }

    @Test
    public void testPhotoStream() throws Exception {
        String result = mockMvc.perform(get("/photo/vandy/stream?tag=vandy"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Assert.isTrue(result.contains("<img src=\"http://www.magnum.io/img/magnum.png\"></img>"));
    }

//    @Test
//    public void testGetProfilePic() throws Exception {
//        String result = mockMvc.perform(get("/profile"))
//            .andExpect(status().isOk())
//            .andReturn().getResponse().getContentAsString();
//        System.out.println("#####################################");
//        System.out.println(result);
//        Assert.isTrue(result.contains());
//    }
}
