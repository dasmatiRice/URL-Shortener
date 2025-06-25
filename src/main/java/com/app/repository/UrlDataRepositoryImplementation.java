package com.app.repository;

import org.springframework.beans.factory.annotation.Autowired;

public class UrlDataRepositoryImplementation {
	
	@Autowired
	UrlDataRepository urlRepository;
	
	public void findItemByOriginalUrl() {
		urlRepository.findItemByOriginalUrl("68504fdbc8a4668910dd462b");
	}
	
	public UrlData saveUrlItem(UrlData u) {
		UrlData response= urlRepository.save(u);
		return response;
	}
	
}
