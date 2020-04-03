package kr.co.domain;

import java.util.Date;

import lombok.Data;

@Data
public class NewsVO {

	private int nno;
	private String ntitle;
	private String ncontent;
	private int rno;
	private Date ndate;

}
