package com.app.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UrlItemRepository extends MongoRepository<UrlItem, Long> {
		
		@Query("{originalUrl:'?0'}")
		Optional<UrlItem> findItemByOriginalUrl(String name);
		
//		@Query("{id:'?0'}")
		Optional<UrlItem> findItemById(String id);
		
		Optional<UrlItem> findItemByShortUrl(String id);
		
		List<UrlItem> findAll();
		
		UrlItem save(UrlItem u);
		
//		@Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
//		List<UrlItem> findAll(String category);
		

}
