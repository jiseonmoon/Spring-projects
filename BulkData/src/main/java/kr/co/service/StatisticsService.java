package kr.co.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import kr.co.domain.StatisticsVO;

public interface StatisticsService {

	public List<StatisticsVO> getStatisticsList(String statisticsStandard);

	public void getExcelDown(String statisticsStandard, HttpServletResponse response);

}
