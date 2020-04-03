package kr.co.mapper;

import java.util.List;

import kr.co.domain.Criteria;
import kr.co.domain.NewsVO;

public interface NewsMapper {

	public void insertNewsOneByOne(NewsVO news);

	public void insertNewsAtOnce(List<NewsVO> newsList);

	public List<NewsVO> selectNewsList(Criteria cri);

	public int getTotalCount(Criteria cri);

}
