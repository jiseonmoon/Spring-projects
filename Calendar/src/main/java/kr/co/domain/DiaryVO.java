package kr.co.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class DiaryVO {

	private int dno;
	private int uno;
	private Date scheduleDate;
	private String scheduleName;

	public String getScheduleDateToString() {
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd");
		return sdfr.format(scheduleDate);
	}

}
