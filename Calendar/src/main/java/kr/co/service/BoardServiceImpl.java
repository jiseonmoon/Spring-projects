package kr.co.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.domain.BoardVO;
import kr.co.domain.CalendarDTO;
import kr.co.domain.Criteria;
import kr.co.domain.DateDTO;
import kr.co.domain.DiaryVO;
import kr.co.domain.HolidayDTO;
import kr.co.domain.UserVO;
import kr.co.mapper.BoardMapper;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

	private BoardMapper mapper;

	@Override
	public void register(BoardVO board) {
		System.out.println("register......" + board);
		mapper.insert(board);
	}

	@Override
	public List<BoardVO> getList(Criteria cri) {
		System.out.println("get List with criteria:" + cri);
		return mapper.getListWithPaging(cri);
	}

	@Override
	public BoardVO get(Long bno) {
		System.out.println("get......" + bno);
		return mapper.read(bno);
	}

	@Override
	public boolean modify(BoardVO board) {
		System.out.println("modify......" + board);
		return mapper.update(board) == 1;
	}

	@Override
	public boolean remove(Long bno) {
		System.out.println("remove......" + bno);
		return mapper.delete(bno) == 1;
	}

	@Override
	public int getTotal(Criteria cri) {
		System.out.println("get total count");
		return mapper.getTotalCount(cri);
	}

	public CalendarDTO createCalendarDTO(int year, int month) {
		if (year == -1 && month == -1) {
			year = LocalDate.now().getYear();
			month = LocalDate.now().getMonthValue();
		} else if (month == 0) {
			year = year - 1;
			month = 12;
		} else if (month == 13) {
			year = year + 1;
			month = 1;
		}
		CalendarDTO calendarDTO = new CalendarDTO(year, month);
		return calendarDTO;
	}

	public void updateDateList(CalendarDTO calendarDTO) {
		List<DateDTO> dateList = calendarDTO.getDateList();
		for (int i = 0; i < calendarDTO.getDayOfTheWeek(); i++) {
			dateList.add(null);
		}
		for (int i = 1; i < calendarDTO.getLastDayOfTheMonth() + 1; i++) {
			dateList.add(new DateDTO(LocalDate.of(calendarDTO.getYear(), calendarDTO.getMonth(), i)
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
		}
		for (int i = calendarDTO.getDayOfTheWeek()
				+ calendarDTO.getLastDayOfTheMonth(); i < kr.co.domain.CalendarSetting.lengthOfWeek
						* calendarDTO.getWeeksInTheMonth(); i++) {
			dateList.add(null);
		}
	}

	public Map<String, List<HolidayDTO>> readHolidayFile() {
		Map<String, List<HolidayDTO>> holidayMap = new HashMap<String, List<HolidayDTO>>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:/calendar/holidays.txt"));
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] splitByTab = line.split("\t");
				String holidayDate = splitByTab[0];
				String holidayName = splitByTab[1];
				String yesOrNo = splitByTab[2];
				HolidayDTO holidayDTO = new HolidayDTO(holidayName, yesOrNo);
				if (holidayMap.containsKey(holidayDate)) {
					holidayMap.get(holidayDate).add(holidayDTO);
				} else {
					List<HolidayDTO> holidayList = new ArrayList<HolidayDTO>();
					holidayList.add(holidayDTO);
					holidayMap.put(holidayDate, holidayList);
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return holidayMap;
	}

	public boolean isExistentUser(UserVO inputUserVO) {
		UserVO userVO = mapper.getUser(inputUserVO);
		if (userVO != null) {
			return true;
		} else {
			return false;
		}
	}

	public UserVO getUser(UserVO inputUserVO) {
		return mapper.getUser(inputUserVO);
	}

	public Map<String, List<DiaryVO>> getDiaryByYearAndMonth(int uno, int year, int month) {
		Map<String, Object> information = new HashMap<String, Object>();
		information.put("uno", uno);
		information.put("yearAndMonth", year + "-" + String.format("%02d", month));
		List<DiaryVO> diaryList = mapper.getDiaryByYearAndMonth(information);
		Map<String, List<DiaryVO>> diaryMap = new HashMap<String, List<DiaryVO>>();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < diaryList.size(); i++) {
			DiaryVO diaryVO = diaryList.get(i);
			if (diaryMap.containsKey(diaryVO.getScheduleDateToString())) {
				diaryMap.get(diaryVO.getScheduleDateToString()).add(diaryVO);
			} else {
				List<DiaryVO> scheduleList = new ArrayList<DiaryVO>();
				scheduleList.add(diaryVO);
				diaryMap.put(diaryVO.getScheduleDateToString(), scheduleList);
			}
//			if (diaryMap.containsKey(sdf.format(diaryVO.getScheduleDate()))) {
//				diaryMap.get(sdf.format(diaryVO.getScheduleDate())).add(diaryVO);
//			} else {
//				List<DiaryVO> scheduleList = new ArrayList<DiaryVO>();
//				scheduleList.add(diaryVO);
//				diaryMap.put(sdf.format(diaryVO.getScheduleDate()), scheduleList);
//			}
		}
		return diaryMap;
	}

	public List<DiaryVO> getDiaryByDay(int uno, String day) {
		Map<String, Object> information = new HashMap<String, Object>();
		information.put("uno", uno);
		information.put("day", day);
		return mapper.getDiaryByDay(information);
	}

	public int registerSchedule(DiaryVO diaryVO) {
		return mapper.insertSchedule(diaryVO);
	}

	public void writeHolidayFile() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:/calendar/holidays.txt"));
			String str = "2020-01-01\t공휴일 테스트\tN\n2020-01-01\t신정\tY\n2020-01-24\t설연휴\tY\n2020-01-25\t설연휴\tY\n2020-01-26\t설연휴\tY\n2020-01-27\t대체휴일\tY\n";
			bw.write(str);
			bw.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
