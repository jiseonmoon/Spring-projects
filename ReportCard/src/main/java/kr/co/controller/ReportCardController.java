package kr.co.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.service.ReportCardService;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/reportCard/*")
@AllArgsConstructor
public class ReportCardController {

	private ReportCardService service;

	@GetMapping("/main")
	public void main(@RequestParam(value = "criterion", defaultValue = "sum") String criterion, Model model) {
		Map<String, Object> map = service.createReportCard();

		Map<String, Map<String, Object>> studentMap = (Map<String, Map<String, Object>>) map.get("studentMap");
		Map<String, Map<String, Object>> subjectMap = (Map<String, Map<String, Object>>) map.get("subjectMap");
		List<String> subjectList = (List<String>) map.get("subjectList");

		List<Map.Entry<String, Map<String, Object>>> entryList = service.sortStudentMap(studentMap, criterion);

		model.addAttribute("entryList", entryList);
		model.addAttribute("subjectMap", subjectMap);
		model.addAttribute("subjectList", subjectList);
		model.addAttribute("criterion", criterion);
	}

}
