package com.test.java.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.test.java.matchoperator.TableTennisMatch;
import com.test.java.set.SetResult;

public class MatchFileRepository {

	private final String filePath;

	public MatchFileRepository(String filePath) { // 파일 경로를 외부에서 받음
		this.filePath = filePath;

		File file = new File(filePath);
		File parentDir = file.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs(); // 상위 폴더가 없으면 생성
		}
	}

	public void save(TableTennisMatch match, boolean isDouble) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
			writer.write(formatMatchResult(match, isDouble));
			writer.newLine();
		} catch (IOException e) {
			System.out.println("파일 쓰기 오류입니다.");
			e.printStackTrace();
		}
	}

	public List<String> findAll() {
		List<String> records = new ArrayList<>();
		File file = new File(filePath);
		if (!file.exists()) {
			return records; // 파일이 없으면 빈 리스트 반환
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				records.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}

	public String formatMatchResult(TableTennisMatch match, boolean isDouble) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String nowTime1 = sdf1.format(now);

		String matchTypeText = isDouble ? "DOUBLES" : "SINGLES";
		String ruleText = (match.getRule() == 1) ? "5판 3선승" : "3판 2선승";

		StringBuilder setScores = new StringBuilder();
		List<SetResult> setResults = match.getSets();

		for (int i = 0; i < setResults.size(); i++) {
			SetResult s = setResults.get(i);
			setScores.append(s.getSetNumber()).append("세트(").append(s.getPlayer1Point()).append(":")
					.append(s.getPlayer2Point()).append(")");

			if (i < setResults.size() - 1) {
				setScores.append(",");
			}
		}

		String txt = nowTime1 + "|" + matchTypeText + "|" + match.getPlayer1().getName() + "|"
				+ match.getPlayer2().getName() + "|" + ruleText + "|" + match.getPlayer1SetWins() + "|"
				+ match.getPlayer2SetWins() + "|" + setScores.toString() + "|" + match.getWinner().getName();

		return txt;
	}

	public void matchStatistics() {
	       
	       List<String> records = findAll();
	       
	       int total = 0;
	        int singles = 0; 
	        int doubles = 0;
	        int setRuleFive = 0;
	        int setRuleThree = 0;
	        
	        if (records !=null) { 
	           
	           total = records.size(); //전체 경기 수
	           for (String record :records) {
	              if (record.contains("DOUBLES")) { 
	                 doubles++; //복식 경기 수 추가
	              } else {
	                 singles++; //단식 경기 수 추가
	              }
	              
	              if (record.contains("5판 3선승")) {
	                 setRuleFive++; //5판 3선승 수 추가
	              } else {
	                 setRuleThree++; //3판 2선승 수 추가
	              }
	           
	           }
	           
	       System.out.printf("전체 경기 수: %d회\r\n", total);
	       System.out.printf("단식: %d회 | 복식: %d회\r\n", singles, doubles );
	       System.out.printf("5판 3선승: %d회 | 3판 2선승: %d회", setRuleFive, setRuleThree );
	           
	           
	        } else {
	           System.out.println("경기 기록이 없습니다. ");
	        }
	       
	       
	    }
	
	public void clear() {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {

	    } catch (IOException e) {
	        System.out.println("파일 초기화 오류입니다.");
	        e.printStackTrace();
	    }
	}

}
