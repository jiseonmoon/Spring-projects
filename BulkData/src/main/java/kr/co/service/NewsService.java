package kr.co.service;

import java.util.List;

import kr.co.domain.NewsVO;
import kr.co.domain.ReporterVO;
import kr.co.domain.Criteria;

public interface NewsService {

	public void mergeReporterAndNews(List<ReporterVO> reporterList, List<NewsVO> newsList);

	public void setListNdate(List<NewsVO> newsList);

	public void insertNewsOneByOne(List<NewsVO> newsList);

	public void insertNewsAtOnce(List<NewsVO> newsList);

	public void autoInsertNewsOneByOne(List<NewsVO> newsList);

	public void autoInsertNewsAtOnce(List<NewsVO> newsList);

	public List<NewsVO> selectNewsList(Criteria cri);

	public int getTotal(Criteria cri);

	public void timerTest();

}
