package kr.co.service;

import java.util.List;
import java.util.Map;

public interface ReportCardService {

	public Map<String, Object> createReportCard();

	public List<Map.Entry<String, Map<String, Object>>> sortStudentMap(Map<String, Map<String, Object>> studentMap,
			String criterion);

}
