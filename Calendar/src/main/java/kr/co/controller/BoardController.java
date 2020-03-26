package kr.co.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.domain.BoardVO;
import kr.co.domain.CalendarDTO;
import kr.co.domain.Criteria;
import kr.co.domain.DiaryVO;
import kr.co.domain.HolidayDTO;
import kr.co.domain.PageDTO;
import kr.co.domain.UserVO;
import kr.co.service.BoardService;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private BoardService service;

	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		model.addAttribute("list", service.getList(cri));
		int total = service.getTotal(cri);
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}

	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes rttr) {
		service.register(board);
		rttr.addFlashAttribute("result", board.getBno());
		return "redirect:/board/list";
	}

	@GetMapping({ "/get", "/modify" })
	public void get(@RequestParam("bno") Long bno, Model model, @ModelAttribute("cri") Criteria cri) {
		model.addAttribute("board", service.get(bno));
	}

	@PostMapping("/modify")
	public String modify(BoardVO board, RedirectAttributes rttr, @ModelAttribute("cri") Criteria cri) {
		if (service.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		return "redirect:/board/list";
	}

	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno, RedirectAttributes rttr, @ModelAttribute("cri") Criteria cri) {
		if (service.remove(bno)) {
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		return "redirect:/board/list";
	}

	@GetMapping("/signUp")
	public void signUp() {
	}

	@GetMapping("/main")
	public void main() {
	}

	@PostMapping("/signIn")
	public String signIn(UserVO inputUserVO, Model model) {
		if (service.isExistentUser(inputUserVO)) {
			UserVO userVO = service.getUser(inputUserVO);
			model.addAttribute("uno", userVO.getUno());
			return "redirect:/board/calendar";
		} else {
			return "redirect:/board/main";
		}
	}

	@GetMapping("/calendar")
	public void calendar(@RequestParam("uno") int uno, @RequestParam(value = "year", defaultValue = "-1") int year,
			@RequestParam(value = "month", defaultValue = "-1") int month, Model model) {
		CalendarDTO calendarDTO = service.createCalendarDTO(year, month);
		service.updateDateList(calendarDTO);

		Map<String, List<HolidayDTO>> holidayMap = service.readHolidayFile();

		Map<String, List<DiaryVO>> diaryMap = service.getDiaryByYearAndMonth(uno, calendarDTO.getYear(),
				calendarDTO.getMonth());

		model.addAttribute("uno", uno);
		model.addAttribute("calendarDTO", calendarDTO);
		model.addAttribute("holidayMap", holidayMap);
		model.addAttribute("diaryMap", diaryMap);
	}

	@GetMapping(value = "/diary/{uno}/{day}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<List<DiaryVO>> diary(@PathVariable int uno, @PathVariable("day") String day) {
		return new ResponseEntity<>(service.getDiaryByDay(uno, day), HttpStatus.OK);
	}

	@PostMapping(value = "/diary/new", consumes = "application/json", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> diaryNew(@RequestBody DiaryVO diaryVO) {
		int insertCount = service.registerSchedule(diaryVO);
		return insertCount == 1 ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@GetMapping()
//	public void writeHolidayFile() {
//		service.writeHolidayFile();
//	}

}
