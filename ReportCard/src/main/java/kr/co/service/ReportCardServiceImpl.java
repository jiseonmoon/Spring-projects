package kr.co.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReportCardServiceImpl implements ReportCardService {

	public Map<String, Map<String, Object>> createStudentMap() {
		Map<String, Map<String, Object>> studentMap = new HashMap<String, Map<String, Object>>();
		return studentMap;
	}

	public Map<String, Map<String, Object>> createSubjectMap() {
		Map<String, Map<String, Object>> subjectMap = new HashMap<String, Map<String, Object>>();
		return subjectMap;
	}

	public List<String> createSubjectList() {
		List<String> subjectList = new ArrayList<String>();
		subjectList.add("korean");
		subjectList.add("english");
		subjectList.add("math");
		return subjectList;
	}

	public void splitAndPutScore(Map<String, Map<String, Object>> studentMap, String subject, String line) {
		String[] splitByTab = line.split("\t");
		String name = splitByTab[0];
		int score = Integer.parseInt(splitByTab[1]);
		if (studentMap.containsKey(name)) {
			studentMap.get(name).put(subject, score);
		} else {
			studentMap.put(name, new HashMap<String, Object>());
			studentMap.get(name).put(subject, score);
		}
	}

	public void readCSVFile(Map<String, Map<String, Object>> studentMap, String subject) {
		try {
			CSVReader cr = new CSVReader(new FileReader("C:/reportCard/" + subject + ".dat"));
			String[] next;
			while ((next = cr.readNext()) != null) {
				splitAndPutScore(studentMap, subject, next[0]);
			}
			cr.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void readFile(Map<String, Map<String, Object>> studentMap, String subject) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:/reportCard/" + subject + ".dat"));
			String line = "";
			while ((line = br.readLine()) != null) {
				splitAndPutScore(studentMap, subject, line);
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateStudentSumAndAvg(Map<String, Map<String, Object>> studentMap, List<String> subjectList) {
		for (Map<String, Object> scoreMap : studentMap.values()) {
			int score = 0;
			for (int i = 0; i < subjectList.size(); i++) {
				score += (int) scoreMap.get(subjectList.get(i));
			}
			scoreMap.put("sum", score);
			scoreMap.put("avg", (float) score / subjectList.size());
		}
	}

	public void updateSubjectSumAndAvg(Map<String, Map<String, Object>> studentMap,
			Map<String, Map<String, Object>> subjectMap, List<String> subjectList) {
		Map<String, Object> sumMap = new HashMap<String, Object>();
		Map<String, Object> avgMap = new HashMap<String, Object>();
		for (int i = 0; i < subjectList.size(); i++) {
			int scoreSumInt = 0;
			float scoreSumFloat = 0;// 평균의 총점(float 타입)을 저장할 변수
			for (Map<String, Object> scoreMap : studentMap.values()) {
				if (subjectList.get(i).equals("avg")) {// 평균의 총점 처리
					scoreSumFloat += (float) scoreMap.get(subjectList.get(i));
				} else {
					scoreSumInt += (int) scoreMap.get(subjectList.get(i));
				}
			}
			if (subjectList.get(i).equals("avg")) {// 평균의 총점과 평균의 평균 처리
				sumMap.put(subjectList.get(i), scoreSumFloat);
				avgMap.put(subjectList.get(i), (float) scoreSumFloat / studentMap.size());
			} else {
				sumMap.put(subjectList.get(i), scoreSumInt);
				avgMap.put(subjectList.get(i), (float) scoreSumInt / studentMap.size());
			}
		}
		subjectMap.put("sum", sumMap);
		subjectMap.put("avg", avgMap);
	}

	public void rank(Map<String, Map<String, Object>> studentMap, String criterion) {
		List<Map.Entry<String, Map<String, Object>>> entryList = new ArrayList<Map.Entry<String, Map<String, Object>>>(
				studentMap.entrySet());
		Collections.sort(entryList, new Comparator<Map.Entry<String, Map<String, Object>>>() {
			@Override
			public int compare(Entry<String, Map<String, Object>> o1, Entry<String, Map<String, Object>> o2) {
				// TODO Auto-generated method stub
				Integer score1 = (Integer) o1.getValue().get(criterion);
				Integer score2 = (Integer) o2.getValue().get(criterion);
				return score2.compareTo(score1);
			}
		});
		if (criterion.equals("sum")) {// sum 등수 매길 때 avg 등수도 매기기
			entryList.get(0).getValue().put(criterion + "Rank", "+");
			entryList.get(9).getValue().put(criterion + "Rank", "-");
			entryList.get(0).getValue().put("avgRank", "+");
			entryList.get(9).getValue().put("avgRank", "-");
		} else {
			entryList.get(0).getValue().put(criterion + "Rank", "+");
			entryList.get(9).getValue().put(criterion + "Rank", "-");
		}
	}

	public Map<String, Object> createReportCard() {
		Map<String, Map<String, Object>> studentMap = createStudentMap();
		Map<String, Map<String, Object>> subjectMap = createSubjectMap();
		List<String> subjectList = createSubjectList();

		// 파일 읽기
		for (int i = 0; i < subjectList.size(); i++) {
//			readCSVFile(studentMap, subjectList.get(i));
			readFile(studentMap, subjectList.get(i));
		}
		// 학생별 합계와 평균
		updateStudentSumAndAvg(studentMap, subjectList);
		// 등수 매기기
		subjectList.add("sum");
		for (int i = 0; i < subjectList.size(); i++) {
			rank(studentMap, subjectList.get(i));
		}
		// 과목별 총점과 평균
		subjectList.add("avg");
		updateSubjectSumAndAvg(studentMap, subjectMap, subjectList);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentMap", studentMap);
		map.put("subjectMap", subjectMap);
		map.put("subjectList", subjectList);
		return map;
	}

	public List<Map.Entry<String, Map<String, Object>>> sortStudentMap(Map<String, Map<String, Object>> studentMap,
			String criterion) {
		List<Map.Entry<String, Map<String, Object>>> entryList = new ArrayList<Map.Entry<String, Map<String, Object>>>(
				studentMap.entrySet());
		Collections.sort(entryList, new Comparator<Map.Entry<String, Map<String, Object>>>() {
			@Override
			public int compare(Entry<String, Map<String, Object>> o1, Entry<String, Map<String, Object>> o2) {
				// TODO Auto-generated method stub
				Integer score1 = (Integer) o1.getValue().get(criterion);
				Integer score2 = (Integer) o2.getValue().get(criterion);
				return score2.compareTo(score1);
			}
		});
		return entryList;
	}

}
