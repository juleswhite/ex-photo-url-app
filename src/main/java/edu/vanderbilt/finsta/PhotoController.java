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

	private Map<String, List<PhotoUrl>> photosByTag = new HashMap<>();
	
	private void addPhoto(PhotoUrl photo, String tag) {
		List<PhotoUrl> photos = photosByTag.getOrDefault(tag, new ArrayList<PhotoUrl>());
		photos.add(photo);
		photosByTag.put(tag, photos);
	}
	
	@RequestMapping("/photo")
	public boolean addPhoto(String url, String tag) {
		PhotoUrl photo = new PhotoUrl();
		photo.setUrl(url);
		photo.setTime(System.currentTimeMillis());
		addPhoto(photo,tag);
		return true;
	}
	
	@RequestMapping("/photo/{tag}")
	public List<PhotoUrl> getPhotosByTag(@PathVariable String tag){
		return photosByTag.getOrDefault(tag, new ArrayList<>());
	}
	
	@RequestMapping("/tag")
	public Set<String> getTags() {
		return photosByTag.keySet();
	}
	
	@RequestMapping("/photo/search")
	public List<PhotoUrl> searchPhotosByTagPrefix(String tagPrefix){
		List<String> matchingTags = photosByTag
				.keySet().stream()
				.filter(t -> t.startsWith(tagPrefix))
				.collect(Collectors.toList());
		
		return matchingTags.stream()
				.flatMap(t -> photosByTag.get(t).stream())
				.collect(Collectors.toList());
	}

	@RequestMapping("/photo/{tag}/stream")
	public ModelAndView photoStream(@PathVariable String tag) {
		ModelAndView mv = new ModelAndView("stream");
		mv.addObject("photos", getPhotosByTag(tag));
		mv.addObject("tag", tag);
		return mv;
	}
	
	 @RequestMapping(value="/photo/search/stream", produces=MediaType.TEXT_HTML_VALUE)
     public String boom(String tagPrefix) throws URISyntaxException {     
		 List<PhotoUrl> photos = searchPhotosByTagPrefix(tagPrefix);
         
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
	
}
