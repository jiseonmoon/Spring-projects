package kr.co.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kr.co.domain.Setting;
import kr.co.domain.ReporterVO;
import kr.co.mapper.ReporterMapper;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReporterServiceImpl implements ReporterService {

	private ReporterMapper reporterMapper;

	@Override
	public void createReporter() {
		List<Double> activityList = createActivity();
		for (int i = 0; i < Setting.CNT_REPORTER; i++) {
			ReporterVO reporter = new ReporterVO();
			reporter.setRid(UUID.randomUUID().toString());
			reporter.setRactivity(activityList.get(i));
			reporterMapper.insertReporter(reporter);
		}
	}

	private List<Double> createActivity() {
		List<Double> activityList = new ArrayList<Double>();
		Random random = new Random();
		double sumActivity = 0;
		for (int i = 0; i < Setting.CNT_REPORTER; i++) {
			double activity = (random.nextGaussian() + 10) / 10;
			sumActivity += activity;
			activityList.add(activity);
		}
		// 오차 제거
		if (sumActivity != Setting.CNT_REPORTER) {
			double error = Setting.CNT_REPORTER - sumActivity;
			for (int i = 0; i < 10; i++) {
				double activity = activityList.get(i);
				activityList.set(i, activity + (error / 10));
			}
		}
		return activityList;
	}

	@Override
	public List<ReporterVO> selectReporterList() {
		List<ReporterVO> reporterList = reporterMapper.selectReporterList();
		return reporterList;
	}

}
