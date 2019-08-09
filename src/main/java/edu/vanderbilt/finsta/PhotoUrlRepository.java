package edu.vanderbilt.finsta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoUrlRepository extends JpaRepository<PhotoUrl, Long> {

	List<PhotoUrl> findAllByTagAndUser(String tag, String user);
	
}
