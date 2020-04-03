package kr.co.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Service;

import kr.co.domain.Criteria;
import kr.co.domain.NewsVO;
import kr.co.domain.ReporterVO;
import kr.co.domain.Setting;
import kr.co.mapper.NewsMapper;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NewsServiceIml implements NewsService {

	private NewsMapper newsMapper;

	private LocalDate getLocalDate() {
		LocalDate localDate = LocalDate.of(Setting.YEAR, 1, 1);
		return localDate;
	}

	@Override
	public void mergeReporterAndNews(List<ReporterVO> reporterList, List<NewsVO> newsList) {
		// 기자별로 활동도를 이용해서 작성하는 뉴스 개수를 구함
		Map<Integer, Integer> reporterCntNewsMap = createReporterCntNewsMap(reporterList, newsList);
		// 기자별로 작성하는 뉴스 개수만큼 뉴스 할당
		int count = 0;
		for (int i = 0; i < reporterList.size(); i++) {
			int rno = reporterList.get(i).getRno();
			int cntNews = reporterCntNewsMap.get(rno);
			for (int j = 0; j < cntNews; j++) {
				newsList.get(count).setRno(rno);
				count++;
			}
		}
		// 뒤섞음
		Collections.shuffle(newsList);
	}

	public Map<Integer, Integer> createReporterCntNewsMap(List<ReporterVO> reporterList, List<NewsVO> newsList) {
		Map<Integer, Integer> reporterCntNewsMap = new HashMap<Integer, Integer>();
		int sumCntNews = 0;
		for (ReporterVO reporter : reporterList) {
			double activity = reporter.getRactivity();
			int cntNews = (int) Math.floor(newsList.size() * activity / 100);
			sumCntNews += cntNews;
			reporterCntNewsMap.put(reporter.getRno(), cntNews);
		}
		// 오차만큼 newsList에서 newsVO를 제거
		if (sumCntNews != newsList.size()) {
			int error = newsList.size() - sumCntNews;
			updateNewsList(newsList, error);
		}
		return reporterCntNewsMap;
	}

	public void updateNewsList(List<NewsVO> newsList, int error) {
		for (int i = 0; i < error; i++) {
			newsList.remove(i);
		}
	}

	@Override
	public void setListNdate(List<NewsVO> newsList) {
		// 뉴스 개수 계산
		List<Integer> monthlyCntNewsList = createMonthlyCntNewsList(newsList.size());
		List<List<Integer>> dailyCntNewsList = createDailyCntNewsList(monthlyCntNewsList);
		List<List<List<Integer>>> hourlyCntNewsList = createHourlyCntNewsList(dailyCntNewsList);
		List<Date> dateList = createDateList(hourlyCntNewsList);
		// newsList와 DateList 병합
		mergeNewsAndDate(newsList, dateList);
	}

	public List<Integer> createMonthlyCntNewsList(int newsListSize) {
		List<Integer> monthlyCntNewsList = new ArrayList<Integer>();
		double[] monthlyPercentageArray = createMonthlyPercentageArray();
		int sumCntNews = 0;
		for (int i = 0; i < monthlyPercentageArray.length; i++) {
			double monthlyPercentage = monthlyPercentageArray[i];
			int cntNews = (int) Math.floor(newsListSize * monthlyPercentage / 100);
			sumCntNews += cntNews;
			monthlyCntNewsList.add(cntNews);
		}
		// 오차 제거
		if (sumCntNews != newsListSize) {
			int error = newsListSize - sumCntNews;
			// 오차만큼 무작위 월에 1씩 더함
			for (int i = 0; i < error; i++) {
				Random rand = new Random();
				int randomMonth = rand.nextInt(11) + 1;
				monthlyCntNewsList.set(randomMonth, monthlyCntNewsList.get(randomMonth) + 1);
			}
		}
		return monthlyCntNewsList;
	}

	public List<List<Integer>> createDailyCntNewsList(List<Integer> monthlyCntNewsList) {
		List<List<Integer>> dailyCntNewsList = new ArrayList<List<Integer>>();
		LocalDate localDate = getLocalDate();
		while (localDate.getYear() < Setting.YEAR + 1) {
			int monthlyCntNews = monthlyCntNewsList.get(localDate.getMonthValue() - 1);
			List<Double> percentageOfMonthList = new ArrayList<Double>();
			List<Integer> cntNewsOfDayList = new ArrayList<Integer>();
			int lengthOfMonth = localDate.lengthOfMonth();
			int quotient = lengthOfMonth / 7;
			int remainder = lengthOfMonth % 7;
			double sumPercentageOfMonth = 0;
			sumPercentageOfMonth = 100 * quotient;
			// ---퍼센티지 작업 시작---
			for (int i = 0; i < quotient; i++) {
				// 주마다 새로운 퍼센티지 적용
				double[] dailyPercentageArray = createDailyPercentageArray();
				for (int j = 1; j < 8; j++) {
					LocalDate tempLocalDate = LocalDate.of(localDate.getYear(), localDate.getMonth(), (i * 7) + j);
					int dayOfWeekValue = tempLocalDate.getDayOfWeek().getValue();
					double percentageOfDay = dailyPercentageArray[dayOfWeekValue - 1];
					percentageOfMonthList.add(percentageOfDay);
				}
			}
			if (remainder > 0) {
				double[] dailyPercentageArray = createDailyPercentageArray();
				for (int i = remainder - 1; i > -1; i--) {
					LocalDate tempLocalDate = LocalDate.of(localDate.getYear(), localDate.getMonth(),
							lengthOfMonth - i);
					int dayOfWeekValue = tempLocalDate.getDayOfWeek().getValue();
					double percentageOfDay = dailyPercentageArray[dayOfWeekValue - 1];
					percentageOfMonthList.add(percentageOfDay);
					sumPercentageOfMonth += percentageOfDay;
				}
			}
			// 백분율을 계산해서 percentageOfMonthList의 요소들을 변경
			for (int i = 0; i < percentageOfMonthList.size(); i++) {
				percentageOfMonthList.set(i, percentageOfMonthList.get(i) / sumPercentageOfMonth * 100);
			}
			// ---퍼센티지 작업 종료--- percentageOfMonthList 완성
			// ---개수 작업 시작---
			int sumCntNews = 0;
			// percentageOfMonthList를 이용해서 월마다 일별로 뉴스 개수를 구함
			for (int i = 0; i < percentageOfMonthList.size(); i++) {
				double percentageOfDay = percentageOfMonthList.get(i);
				int cntNews = (int) Math.floor(percentageOfDay * monthlyCntNews / 100);
				sumCntNews += cntNews;
				cntNewsOfDayList.add(cntNews);
			}
			// 오차 제거
			if (sumCntNews != monthlyCntNews) {
				int error = monthlyCntNews - sumCntNews;
				// 오차만큼 무작위 일에 1씩 더함
				for (int i = 0; i < error; i++) {
					Random rand = new Random();
					int randomDay = rand.nextInt(lengthOfMonth - 1) + 1;
					cntNewsOfDayList.set(randomDay, cntNewsOfDayList.get(randomDay) + 1);
				}
			}
			// ---개수 작업 종료--- cntNewsOfDayList 완성
			dailyCntNewsList.add(cntNewsOfDayList);
			localDate = localDate.plusMonths(1);
		}
		return dailyCntNewsList;
	}

	public List<List<List<Integer>>> createHourlyCntNewsList(List<List<Integer>> dailyCntNewsList) {
		List<List<List<Integer>>> hourlyCntNewsList = new ArrayList<List<List<Integer>>>();
		// 월
		for (int i = 0; i < dailyCntNewsList.size(); i++) {
			List<List<Integer>> groupByDayList = new ArrayList<List<Integer>>();
			List<Integer> cntNewsOfDayList = dailyCntNewsList.get(i);
			// 일
			for (int j = 0; j < cntNewsOfDayList.size(); j++) {
				List<Integer> cntNewsOfHourList = new ArrayList<Integer>();
				int cntNewsOfDay = cntNewsOfDayList.get(j);
				double[] hourlyPercentageArray = createHourlyPercentageArray();
				int sumCntNews = 0;
				// 시간
				for (int k = 0; k < hourlyPercentageArray.length; k++) {
					double hourlyPercentage = hourlyPercentageArray[k];
					int cntNews = (int) Math.floor(cntNewsOfDay * hourlyPercentage / 100);
					sumCntNews += cntNews;
					cntNewsOfHourList.add(cntNews);
				}
				if (sumCntNews != cntNewsOfDay) {
					int error = cntNewsOfDay - sumCntNews;
					// 오차만큼 무작위 시에 1씩 더함
					for (int l = 0; l < error; l++) {
						Random rand = new Random();
						int randomHour = rand.nextInt(24);
						cntNewsOfHourList.set(randomHour, cntNewsOfHourList.get(randomHour) + 1);
					}
				}
				groupByDayList.add(cntNewsOfHourList);
			}
			hourlyCntNewsList.add(groupByDayList);
		}
		return hourlyCntNewsList;
	}

	public List<Date> createDateList(List<List<List<Integer>>> hourlyCntNewsList) {
		List<Date> dateList = new ArrayList<Date>();
		for (int i = 0; i < hourlyCntNewsList.size(); i++) {// hourlyCntNewsList.size() = 12
			List<List<Integer>> groupByDayList = hourlyCntNewsList.get(i);
			int month = i + 1;
			for (int j = 0; j < groupByDayList.size(); j++) {// lengthOfMonth
				List<Integer> cntNewsOfDayList = groupByDayList.get(j);
				int day = j + 1;
				for (int k = 0; k < cntNewsOfDayList.size(); k++) {// cntNewsOfDayList.size() = 24
					int cntNews = cntNewsOfDayList.get(k);
					if (cntNews > 0) {
						int hour = k;
						Date date = Date.from(LocalDate.of(Setting.YEAR, month, day).atTime(hour, 0, 0)
								.atZone(ZoneId.systemDefault()).toInstant());
						long milliseconds = date.getTime();
						int termWrite = 3600000 / cntNews;
						for (int l = 0; l < cntNews; l++) {
							dateList.add(new Date(milliseconds + termWrite * l));
						}
					}
				}
			}
		}
		return dateList;
	}

	public void mergeNewsAndDate(List<NewsVO> newsList, List<Date> dateList) {
		if (newsList.size() == dateList.size()) {
			for (int i = 0; i < newsList.size(); i++) {
				NewsVO news = newsList.get(i);
				Date date = dateList.get(i);
				news.setNdate(date);
			}
		}
	}

	public double[] createMonthlyPercentageArray() {
		double[] monthlyPercentageArray = { 8.00, 6.00, 10.00, 10.00, 9.00, 9.00, 10.00, 8.00, 8.00, 8.00, 8.00, 6.00 };
		Random rand = new Random();
		double sumMonthlyPercentage = 0;
		for (int i = 0; i < monthlyPercentageArray.length; i++) {
			double monthlyPercentage = monthlyPercentageArray[i] + rand.nextInt(1) - rand.nextInt(1) + rand.nextDouble()
					- rand.nextDouble();
			monthlyPercentageArray[i] = monthlyPercentage;
			sumMonthlyPercentage += monthlyPercentage;
		}
		// 오차만큼 무작위 월에 더하거나 뺌
		if (sumMonthlyPercentage > 100.00) {
			double error = sumMonthlyPercentage - 100.00;
			int randomMonth = rand.nextInt(11) + 1;
			monthlyPercentageArray[randomMonth] = monthlyPercentageArray[randomMonth] - error;
		} else if (sumMonthlyPercentage < 100.00) {
			double error = 100.00 - sumMonthlyPercentage;
			int randomMonth = rand.nextInt(11) + 1;
			monthlyPercentageArray[randomMonth] = monthlyPercentageArray[randomMonth] + error;
		}
		return monthlyPercentageArray;
	}

	public double[] createDailyPercentageArray() {
		double[] dailyPercentageArray = { 15.66, 15.64, 15.46, 15.37, 14.82, 11.63, 11.41 };
		Random rand = new Random();
		double sumDailyPercentage = 0;
		for (int i = 0; i < dailyPercentageArray.length; i++) {
			double dailyPercentage = dailyPercentageArray[i] + rand.nextInt(2) - rand.nextInt(2) + rand.nextDouble()
					- rand.nextDouble();
			dailyPercentageArray[i] = dailyPercentage;
			sumDailyPercentage += dailyPercentage;
		}
		// 오차 제거
		if (sumDailyPercentage > 100.00) {
			double error = sumDailyPercentage - 100.00;
			for (int i = 0; i < dailyPercentageArray.length; i++) {
				dailyPercentageArray[i] = dailyPercentageArray[i] - (error / dailyPercentageArray.length);
			}
		} else if (sumDailyPercentage < 100.00) {
			double error = 100.00 - sumDailyPercentage;
			for (int i = 0; i < dailyPercentageArray.length; i++) {
				dailyPercentageArray[i] = dailyPercentageArray[i] + (error / dailyPercentageArray.length);
			}
		}
		return dailyPercentageArray;
	}

	public double[] createHourlyPercentageArray() {
		double[] hourlyPercentageArray = { 3.93, 2.49, 1.50, 0.99, 0.74, 0.70, 0.98, 1.70, 3.13, 4.84, 5.61, 5.92, 5.42,
				6.02, 6.02, 6.16, 6.24, 6.13, 5.36, 5.02, 5.28, 5.49, 5.35, 4.98 };
		Random rand = new Random();
		double sumHourlyPercentage = 0;
		for (int i = 0; i < hourlyPercentageArray.length; i++) {
			double hourlyPercentage = hourlyPercentageArray[i] + (rand.nextDouble() / 10) - (rand.nextDouble() / 10);
			hourlyPercentageArray[i] = hourlyPercentage;
			sumHourlyPercentage += hourlyPercentage;
		}
		// 오차 제거
		if (sumHourlyPercentage > 100.00) {
			double error = sumHourlyPercentage - 100.00;
			for (int i = 0; i < hourlyPercentageArray.length; i++) {
				hourlyPercentageArray[i] = hourlyPercentageArray[i] - (error / hourlyPercentageArray.length);
			}
		} else if (sumHourlyPercentage < 100.00) {
			double error = 100.00 - sumHourlyPercentage;
			for (int i = 0; i < hourlyPercentageArray.length; i++) {
				hourlyPercentageArray[i] = hourlyPercentageArray[i] + (error / hourlyPercentageArray.length);
			}
		}
		return hourlyPercentageArray;
	}

	@Override
	public void insertNewsOneByOne(List<NewsVO> newsList) {
		for (int i = 0; i < newsList.size(); i++) {
			newsMapper.insertNewsOneByOne(newsList.get(i));
		}
	}

	@Override
	public void insertNewsAtOnce(List<NewsVO> newsList) {
		List<List<NewsVO>> divisionByBatch = getdivisionByBatch(newsList);
		// execute
		for (List<NewsVO> batch : divisionByBatch) {
			newsMapper.insertNewsAtOnce(batch);
		}
	}

	@Override
	public void autoInsertNewsOneByOne(List<NewsVO> newsList) {
		for (NewsVO news : newsList) {
			Timer timer = new Timer();
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					newsMapper.insertNewsOneByOne(news);
				}
			};
			timer.schedule(timerTask, news.getNdate());
		}
	}

	@Override
	public void autoInsertNewsAtOnce(List<NewsVO> newsList) {
		List<List<NewsVO>> divisionByBatch = getdivisionByBatch(newsList);
		for (List<NewsVO> batch : divisionByBatch) {
			Timer timer = new Timer();
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					newsMapper.insertNewsAtOnce(batch);
				}
			};
			timer.schedule(timerTask, batch.get(batch.size() - 1).getNdate());
		}
	}

	public List<List<NewsVO>> getdivisionByBatch(List<NewsVO> newsList) {
		int batchSize = 10000;
		int quotient = newsList.size() / batchSize;
		int remainder = newsList.size() % batchSize;
		List<List<NewsVO>> divisionByBatch = new ArrayList<List<NewsVO>>();
		for (int i = 0; i < quotient; i++) {
			List<NewsVO> extractedNewsList = newsList.subList(i * batchSize, (i + 1) * batchSize);
			divisionByBatch.add(extractedNewsList);
		}
		if (remainder > 0) {
			List<NewsVO> extractedNewsList = newsList.subList(quotient * batchSize, quotient * batchSize + remainder);
			divisionByBatch.add(extractedNewsList);
		}
		return divisionByBatch;
	}

	@Override
	public List<NewsVO> selectNewsList(Criteria cri) {
		return newsMapper.selectNewsList(cri);
	}

	@Override
	public int getTotal(Criteria cri) {
		return newsMapper.getTotalCount(cri);
	}

	@Override
	public void timerTest() {
		// 테스트용 newsVO 생성
		NewsVO news = new NewsVO();
		news.setNtitle("title");
		news.setNcontent("content");
		news.setRno(7);
		Date date = new Date();
		date.setYear(2020 - 1900);
		date.setMonth(3 - 1);
		date.setDate(25);
		date.setHours(15);
		date.setMinutes(59);
		news.setNdate(date);
		// 실행
		System.out.println(date);
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				newsMapper.insertNewsOneByOne(news);
				System.out.println("success");
			}
		};
		timer.schedule(timerTask, date);
	}

}
