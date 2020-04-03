package kr.co.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import kr.co.domain.StatisticsVO;
import kr.co.mapper.StatisticsMapper;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

	private StatisticsMapper statisticsMapper;

	@Override
	public List<StatisticsVO> getStatisticsList(String statisticsStandard) {
		statisticsStandard = convertStandard(statisticsStandard);
		return statisticsMapper.getStatisticsList(statisticsStandard);
	}

	@Override
	public void getExcelDown(String statisticsStandard, HttpServletResponse response) {
		statisticsStandard = convertStandard(statisticsStandard);
		List<StatisticsVO> statisticsList = statisticsMapper.getStatisticsList(statisticsStandard);
		try {
			// Excel Down 시작
			Workbook workbook = new HSSFWorkbook();
			// 시트생성
			Sheet sheet = workbook.createSheet("게시판 통계");

			// 행, 열, 열번호
			Row row = null;
			Cell cell = null;
			int rowNo = 0;

			// 테이블 헤더용 스타일
			CellStyle headStyle = workbook.createCellStyle();

			// 가는 경계선을 가집니다.
			headStyle.setBorderTop(BorderStyle.THIN);
			headStyle.setBorderBottom(BorderStyle.THIN);
			headStyle.setBorderLeft(BorderStyle.THIN);
			headStyle.setBorderRight(BorderStyle.THIN);

			// 배경색은 노란색입니다.
			headStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
			headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// 데이터는 가운데 정렬합니다.
			headStyle.setAlignment(HorizontalAlignment.CENTER);

			// 데이터용 경계 스타일 테두리만 지정
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setBorderTop(BorderStyle.THIN);
			bodyStyle.setBorderBottom(BorderStyle.THIN);
			bodyStyle.setBorderLeft(BorderStyle.THIN);
			bodyStyle.setBorderRight(BorderStyle.THIN);

			// 헤더 생성
			row = sheet.createRow(rowNo++);

			cell = row.createCell(0);
			cell.setCellStyle(headStyle);
			cell.setCellValue("월");

			cell = row.createCell(1);
			cell.setCellStyle(headStyle);
			cell.setCellValue("개수");

			// 데이터 부분 생성
			for (StatisticsVO statistics : statisticsList) {
				row = sheet.createRow(rowNo++);
				cell = row.createCell(0);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue("" + statistics.getDate());
				cell = row.createCell(1);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue("" + statistics.getCount());
			}

			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=statistics.xls");

			// 엑셀 출력
			workbook.write(response.getOutputStream());
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String convertStandard(String statisticsStandard) {
		switch (statisticsStandard) {
		case "month":
			statisticsStandard = "%m";
			break;
		case "day":
			statisticsStandard = "%d";
			break;
		case "hour":
			statisticsStandard = "%H";
			break;
		}
		return statisticsStandard;
	}

}
