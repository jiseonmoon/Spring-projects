package kr.co.mapper;

import java.util.List;

import kr.co.domain.ReporterVO;

public interface ReporterMapper {

	public void insertReporter(ReporterVO reporter);

	public List<ReporterVO> selectReporterList();

}
