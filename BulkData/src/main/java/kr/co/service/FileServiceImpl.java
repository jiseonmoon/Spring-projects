package kr.co.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import kr.co.domain.NewsVO;
import kr.co.domain.Setting;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

	@Override
	public void writeFile() {
		Map<String, String> titlesAndContents = webCrawling();
		try {
			BufferedWriter bwTitles = new BufferedWriter(new FileWriter("C:/news/titles.txt"));
			BufferedWriter bwContents = new BufferedWriter(new FileWriter("C:/news/contents.txt"));
			bwTitles.write(titlesAndContents.get("titles"));
			bwContents.write(titlesAndContents.get("contents"));
			bwTitles.close();
			bwContents.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> webCrawling() {
		Map<String, String> titlesAndContents = new HashMap<String, String>();
		String url = "https://news.naver.com/main/read.nhn?mode=LS2D&mid=shm&sid1=105&sid2=230&oid=003&aid=";
		StringBuilder titles = new StringBuilder();
		StringBuilder contents = new StringBuilder();
		int count = 0;
		try {
			// 기사 내용은 300개를 수집
			for (int i = 0; i < 300; i++) {
				Document doc = Jsoup.connect(url + String.format("%010d", i + 1)).get();
				Map<String, String> news = getNews(doc);
				if (news.get("title").equals("")) {
					count++;
				} else {
					// 기사 제목은 100개를 수집
					if (i < 100 + count) {
						titles.append(news.get("title"));
						titles.append("\n");
					}
					contents.append(news.get("content"));
					contents.append("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		titlesAndContents.put("titles", titles.toString());
		titlesAndContents.put("contents", contents.toString());
		return titlesAndContents;
	}

	public Map<String, String> getNews(Document doc) {
		Map<String, String> news = new HashMap<String, String>();
		String title = doc.select("#articleTitle").text();
		String content = doc.select("#articleBodyContents").text();
		news.put("title", title);
		news.put("content", content);
		return news;
	}

	@Override
	public List<NewsVO> createNewsList() {
		List<String> titleList = readFile("title");
		List<String> paragraphList = readFile("content");
		List<NewsVO> newsList = new ArrayList<NewsVO>();
		titleList = updateTitleList(titleList);
		List<String> contentList = createContentList(paragraphList);
		for (int i = 0; i < Setting.CNT_NEWS; i++) {
			NewsVO news = new NewsVO();
			news.setNtitle(titleList.get(i));
			news.setNcontent(contentList.get(i));
			newsList.add(news);
		}
		return newsList;
	}

	public List<String> readFile(String titleOrContent) {
		List<String> list = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:/news/" + titleOrContent + "s.txt"));
			String line = "";
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<String> updateTitleList(List<String> oldTitleList) {
		List<String> newTitleList = new ArrayList<String>();
		Random rand = new Random();
		for (int i = 0; i < Setting.CNT_NEWS; i++) {
			String title = oldTitleList.get(rand.nextInt(oldTitleList.size()));
			newTitleList.add(title);
		}
		return newTitleList;
	}

	public List<String> createContentList(List<String> paragraphList) {
		List<String> sentenceList = getSentenceList(paragraphList);
		List<String> contentList = getContentList(sentenceList);
		return contentList;
	}

	// 문단을 "."으로 split해서 문장을 만듦
	public List<String> getSentenceList(List<String> paragraphList) {
		List<String> sentenceList = new ArrayList<String>();
		for (String paragraph : paragraphList) {
			String[] sentences = paragraph.split("\\.");
			for (String sentence : sentences) {
				sentenceList.add(sentence.trim());
			}
		}
		return sentenceList;
	}

	public List<String> getContentList(List<String> sentenceList) {
		List<String> contentList = new ArrayList<String>();
		Random rand = new Random();
		for (int i = 0; i < Setting.CNT_NEWS; i++) {
			int cnt_paragraph = rand.nextInt(3) + 3;
			// 문단을 Random으로 재구성해서 뉴스를 만듦
			StringBuilder news = new StringBuilder();
			for (int j = 0; j < cnt_paragraph; j++) {
				int cnt_sentence = rand.nextInt(3) + 2;
				// 문장을 Random으로 재구성해서 문단을 만듦
				StringBuilder paragraph = new StringBuilder();
				for (int k = 0; k < cnt_sentence; k++) {
					String sentence = sentenceList.get(rand.nextInt(sentenceList.size()));
					paragraph.append(sentence);
					paragraph.append(". ");
				}
				news.append(paragraph);
				news.append("\n");
			}
			contentList.add(news.toString());
		}
		return contentList;
	}

}
