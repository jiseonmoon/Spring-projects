package com.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.test.domain.BoardVO;
import com.test.domain.Criteria;

public interface BoardMapper {

//	@Select("SELECT * FROM tbl_board WHERE bno > 0")
	public List<BoardVO> getList();

	public List<BoardVO> getListWithPaging(Criteria cri);

	public void insert(BoardVO board);

	public void insertSelectKey(BoardVO board);

	public BoardVO read(long bno);

	public int delete(long bno);

	public int update(BoardVO board);

	public int getTotalCount(Criteria cri);

}
