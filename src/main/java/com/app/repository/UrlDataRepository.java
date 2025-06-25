package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UrlDataRepository extends MongoRepository<UrlData, Long> {
		
		@Query("{originalUrl:'?0'}")
		Optional<UrlData> findItemByOriginalUrl(String name);
		
//		@Query("{id:'?0'}")
		Optional<UrlData> findItemById(String id);
		
		Optional<UrlData> findItemByShortUrl(String id);
		
		List<UrlData> findAll();
		
		UrlData save(UrlData u);
		
		int deleteByShortUrl(String shortUrl);
		
//		@Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
//		List<UrlItem> findAll(String category);
		

}
