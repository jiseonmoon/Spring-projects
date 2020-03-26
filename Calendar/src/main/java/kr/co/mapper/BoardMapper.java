package kr.co.mapper;

import java.util.List;
import java.util.Map;

import kr.co.domain.BoardVO;
import kr.co.domain.Criteria;
import kr.co.domain.DiaryVO;
import kr.co.domain.UserVO;

public interface BoardMapper {

	public List<BoardVO> getList();

	public void insert(BoardVO board);

	public BoardVO read(long bno);

	public int delete(long bno);

	public int update(BoardVO board);

	public List<BoardVO> getListWithPaging(Criteria cri);

	public int getTotalCount(Criteria cri);

	public UserVO getUser(UserVO inputUserVO);

	public List<DiaryVO> getDiaryByYearAndMonth(Map<String, Object> information);

	public List<DiaryVO> getDiaryByDay(Map<String, Object> information);

	public int insertSchedule(DiaryVO diaryVO);

}
