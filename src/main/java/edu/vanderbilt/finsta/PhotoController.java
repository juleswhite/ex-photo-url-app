package edu.vanderbilt.finsta;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PhotoController {
	
	private final PhotoUrlRepository photoRepository;
	
	private final EntityManager entityManager;
	
	@Autowired
	public PhotoController(PhotoUrlRepository photoRepo, EntityManager entityManager) {
		this.photoRepository = photoRepo;
		this.entityManager = entityManager;
	}
	
	@RequestMapping("/photo")
	public boolean addPhoto(String url, String tag, Principal user) {
		PhotoUrl photo = new PhotoUrl();
		photo.setTag(tag);
		photo.setUser(user.getName());
		photo.setUrl(url);
		photo.setTime(System.currentTimeMillis());
		
		photoRepository.save(photo);
		
		return true;
	}
	
	@RequestMapping("/photo/{tag}")
	public List<PhotoUrl> getPhotosByTag(@PathVariable String tag, Principal user){
		
		// We only allow people to view photos that they created
		// photo.tag = tag AND photo.user = user
		return photoRepository.findAllByTagAndUser(tag, user.getName());
	}
	
	@RequestMapping("/tag")
	public Set<String> getTags() {
		return photoRepository.findAll().stream().map(p -> p.getTag()).collect(Collectors.toSet());
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/photo/search")
	public List<PhotoUrl> searchPhotosByTagPrefix(String tagPrefix, Principal user){
		
		String q = "select pu from PhotoUrl pu where"+
				 		 " pu.user = '" + user.getName() + "'"+
                         " and pu.tag like '" + tagPrefix +"%'";
		                
		return (List<PhotoUrl>) 
				entityManager
				.createQuery(q)
							.getResultList();
	}

	@RequestMapping("/photo/{tag}/stream")
	public ModelAndView photoStream(@PathVariable String tag, Principal user) {
		ModelAndView mv = new ModelAndView("stream");
		mv.addObject("photos", getPhotosByTag(tag, user));
		mv.addObject("tag", tag);
		return mv;
	}
	
	 @RequestMapping(value="/photo/search/stream", produces=MediaType.TEXT_HTML_VALUE)
     public String boom(String tagPrefix, Principal user) throws URISyntaxException {     
		 List<PhotoUrl> photos = searchPhotosByTagPrefix(tagPrefix, user);
         
		 String result = "<html><body>";
		 
		 for(PhotoUrl p : photos) {
			 result += "<img src='" + p.getUrl() + "'></img><br/>";
		 }
		 
		 result += "</body></html>";
		 
		 return result;
     }
	 
	 @RequestMapping(value="/profile", produces=MediaType.IMAGE_JPEG_VALUE)
	 public byte[] getProfilePic(String user) throws IOException {
		 return Files.readAllBytes(Paths.get("src/main/resources/static/"+user));
	 }
	 
	 @RequestMapping(value="/", produces=MediaType.TEXT_HTML_VALUE)
     public String homepage() {     
		 return "<html><body><a href='/photo/search/stream?tagPrefix='>Photo Stream</a></body></html>";
     }
	
}
