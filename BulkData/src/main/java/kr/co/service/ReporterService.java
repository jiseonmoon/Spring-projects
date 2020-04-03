package kr.co.service;

import java.util.List;

import kr.co.domain.ReporterVO;

public interface ReporterService {

	public void createReporter();

	public List<ReporterVO> selectReporterList();

}
