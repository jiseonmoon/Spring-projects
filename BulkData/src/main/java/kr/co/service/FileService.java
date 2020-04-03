package kr.co.service;

import java.util.List;

import kr.co.domain.NewsVO;

public interface FileService {

	public void writeFile();

	public List<NewsVO> createNewsList();

}
