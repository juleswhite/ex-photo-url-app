package edu.vanderbilt.finsta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PhotoController {

	
	/**
        
        Step 1. 

        Create a method call addPhoto that receives requests to /photo
        and has url and tag arguments. The method should create a new
        PhotoUrl, set the Url and time for the PhotoUrl, and then add
        the PhotoUrl to a list photos associated with the specified
        tag. Finally, the method should return true if the operation
        was successful.
        
        Run the application by right-clicking on the PhotoApplication and
        selecting Run Ass->Java Application. The logs for the application
        should be printed to the Console in Eclipse. If you do not see the
        Console, open it by going to the Window menu and selecting 
        Show View->Console. 

        When you are done, you should be able to open the link below
        in your browser and see "true" printed:

        http://localhost:8080/photo?url=http://www.magnum.io/img/magnum.png&tag=vandy
      

        **/

	
	/**
        
        Step 2. 
        
        Make sure that you stop the application by pressing the red stop
        sign in the Console. After you make the changes for each subsequent
        step, run the app and then stop it before the next step. 
        
        If you see an error about Tomcat or ports, it is because you failed
        to stop the application before trying to rerun it.

        Create a method called getPhotosByTag that receives requests to
        /photo/{tag}, where {tag} is replaced by an arbitrary string.
        The method should take a tag argument. The method should return
        the list of photos that have been associated with the specified
        tag.

        When you are done, you should be able to open the link below
        in your browser and see something similar to what is shown
        below the link.

        http://localhost:8080/photo/vandy

        [{"id":"766da867-ca7c-4dc0-89fe-7528fd1cddd0",
          "time":1565353765112,
          "url":"http://www.magnum.io/img/magnum.png"}]

        **/

	/**
        
        Step 3. 

        Create a method called getTags() that receives requests to /tag
        and takes no arguments. The method should return the current 
        list of tags in use. 

         When you are done, you should be able to open the link below
        in your browser and see something similar to what is shown
        below the link.    
        
        http://localhost:8080/tag

        ["vandy"]

        **/	

	/**
        
        Step 4. 

        Create a method called searchPhotosByTagPrefix that receives
        requests to /photo/search and takes a tagPrefix argument. The
        method should return the list of all photos where the tag for
        the photo starts with the provided prefix. 

        **/
	
        /**
    
        Step 5. 
        
        Uncomment the code in the method below.

        This example uses raw string manipulation to render an HTML response.
        
        Modify the code to wrap each image in a "<div></div>" element and also
        include the time that the photo was taken inside the div.
        
        This code is highly insecure.
        **/
//	@RequestMapping(value="/photo/search/stream", produces=MediaType.TEXT_HTML_VALUE)
//        public String boom(String tagPrefix) throws URISyntaxException {     
//		 List<PhotoUrl> photos = searchPhotosByTagPrefix(tagPrefix);
//         
//		 String result = "<html><body>";
//		 
//		 for(PhotoUrl p : photos) {
//			 result += "<img src='" + p.getUrl() + "'></img><br/>";
//		 }
//		 
//		 result += "</body></html>";
//		 
//		 return result;
//        }
	
	
	/**
    
        Step 6. 
        
        Uncomment the code in the method below.

        This example uses a Thymeleaf template to render an HTML response.

        Open the src/main/resources/templates/stream.html file and modify
        this Thymeleaf template (look near the bottom) to also print out
        the time associated with each PhotoUrl.     

        You should be able to go to http://localhost:8080/photo/vandy/stream
        to see the photo stream html.

        Try to use the examples that already exist in the code. If you need
        additional help, you can refer to the Thymeleaf template docs:
        https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#introducing-thymeleaf

        This code is highly insecure.

        **/
//	@RequestMapping("/photo/{tag}/stream")
//	public ModelAndView photoStream(@PathVariable String tag) {
//		ModelAndView mv = new ModelAndView("stream");
//		mv.addObject("photos", getPhotosByTag(tag));
//		mv.addObject("tag", tag);
//		return mv;
//	}
	
	
        /**
	
    
        Step 7. 

        This example shows how to read a binary file and return it to a client.

        Try to create your own method with a different name that
        serves up files from some directory using the correct content type.

        This code is highly insecure.
        
        **/
	@RequestMapping(value="/profile", produces=MediaType.IMAGE_JPEG_VALUE)
	public byte[] getProfilePic(String user) throws IOException {
		 return Files.readAllBytes(Paths.get("src/main/resources/static/"+user));
	}
	
}
