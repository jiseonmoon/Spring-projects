package kr.co.service;

import java.util.List;
import java.util.Map;

import kr.co.domain.BoardVO;
import kr.co.domain.CalendarDTO;
import kr.co.domain.Criteria;
import kr.co.domain.DiaryVO;
import kr.co.domain.HolidayDTO;
import kr.co.domain.UserVO;

public interface BoardService {

	public void register(BoardVO board);

	public BoardVO get(Long bno);

	public boolean modify(BoardVO board);

	public boolean remove(Long bno);

	public List<BoardVO> getList(Criteria cri);

	public int getTotal(Criteria cri);

	public CalendarDTO createCalendarDTO(int year, int month);

	public void updateDateList(CalendarDTO calendarDTO);

	public Map<String, List<HolidayDTO>> readHolidayFile();

	public boolean isExistentUser(UserVO inputUserVO);

	public UserVO getUser(UserVO inputUserVO);

	public Map<String, List<DiaryVO>> getDiaryByYearAndMonth(int uno, int year, int month);

	public List<DiaryVO> getDiaryByDay(int uno, String day);

	public int registerSchedule(DiaryVO diaryVO);

	public void writeHolidayFile();

}
