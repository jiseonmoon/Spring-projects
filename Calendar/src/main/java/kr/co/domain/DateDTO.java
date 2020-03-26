package kr.co.domain;

import lombok.Getter;

@Getter
public class DateDTO {

	private String date;
	private String year;
	private String month;
	private String day;

	public DateDTO(String date) {
		this.date = date;
		year = date.substring(0, 4);
		month = date.substring(5, 7);
		day = date.substring(8, 10);
	}

}
