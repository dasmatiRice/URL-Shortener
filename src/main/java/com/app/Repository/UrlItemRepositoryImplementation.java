package com.app.Repository;

import org.springframework.beans.factory.annotation.Autowired;

public class UrlItemRepositoryImplementation {
	
	@Autowired
	UrlItemRepository urlRepository;
	
	public void findItemByOriginalUrl() {
		urlRepository.findItemByOriginalUrl("68504fdbc8a4668910dd462b");
	}
	
	public UrlItem saveUrlItem(UrlItem u) {
		UrlItem response= urlRepository.save(u);
		return response;
	}
	
}
