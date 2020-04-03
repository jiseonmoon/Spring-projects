package kr.co.mapper;

import java.util.List;

import kr.co.domain.NewsVO;
import kr.co.domain.StatisticsVO;

public interface StatisticsMapper {

	public List<StatisticsVO> getStatisticsList(String statisticsStandard);

	public List<NewsVO> selectNewsList();

}
