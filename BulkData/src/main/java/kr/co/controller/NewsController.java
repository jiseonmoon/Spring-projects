package kr.co.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.domain.Criteria;
import kr.co.domain.NewsVO;
import kr.co.domain.PageDTO;
import kr.co.domain.ReporterVO;
import kr.co.service.NewsService;
import kr.co.service.FileService;
import kr.co.service.ReporterService;
import kr.co.service.StatisticsService;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/news/*")
@AllArgsConstructor
public class NewsController {

	private ReporterService reporterService;
	private FileService fileService;
	private NewsService newsService;
	private StatisticsService statisticsService;

//	@GetMapping("/createReporter")
//	public void createReporter() {
//		reporterService.createReporter();
//	}

//	@GetMapping("/createFile")
//	public void createFile() {
//		fileService.writeFile();
//		System.out.println("파일 입력 종료");
//	}

	@GetMapping("/insert")
	public void insert() {
		List<ReporterVO> reporterList = reporterService.selectReporterList();
		List<NewsVO> newsList = fileService.createNewsList();
		newsService.mergeReporterAndNews(reporterList, newsList);
		newsService.setListNdate(newsList);
		// ---입력 속도 비교 시작---
//		System.out.println("start");
//		long startTime = System.currentTimeMillis();
//		newsService.insertNewsOneByOne(newsList);
//		newsService.insertNewsAtOnce(newsList);
//		long finishTime = System.currentTimeMillis();
//		System.out.println("finish");
//		System.out.println("시간 : " + (finishTime - startTime));
		// ---입력 속도 비교 종료---
//		newsService.autoInsertNewsOneByOne(newsList);
		newsService.autoInsertNewsAtOnce(newsList);
		System.out.println("finish");
	}

	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		model.addAttribute("newsList", newsService.selectNewsList(cri));
		int total = newsService.getTotal(cri);
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}

	@GetMapping("/statistics")
	public void statistics(
			@RequestParam(value = "statisticsStandard", defaultValue = "month") String statisticsStandard,
			Model model) {
		model.addAttribute("statisticsStandard", statisticsStandard);
		model.addAttribute("statisticsList", statisticsService.getStatisticsList(statisticsStandard));
	}

	@PostMapping("/excelDown")
	public void excelDown(String statisticsStandard, HttpServletResponse response) {
		statisticsService.getExcelDown(statisticsStandard, response);
	}

	@GetMapping("/timerTest")
	public void timerTest() {
		newsService.timerTest();
	}

}
