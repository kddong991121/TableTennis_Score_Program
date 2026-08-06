package com.test.java.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.test.java.matchoperator.TableTennisMatch;

public class PlayerFileRepository {
    private final String path;

    public PlayerFileRepository(String path) {
        this.path = path;

        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); // 상위 폴더가 없으면 생성
        }
    }
    
    public boolean containsName(String[] names, String target){
    	for (String n : names) {
            if (n.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public void save(TableTennisMatch match, boolean isDouble) {
        String[] p1Names = match.getPlayer1().getName().split(",");
        String[] p2Names = match.getPlayer2().getName().split(",");
        String[] winnerNames = match.getWinner().getName().split(",");

        boolean[] p1Found = new boolean[p1Names.length];
        boolean[] p2Found = new boolean[p2Names.length];

        List<String> stats = findAll();

        for (int i = 0; i < stats.size(); i++) {
            String line = stats.get(i);
            String[] split = normalizeRecord(line);

            if (containsName(p1Names, split[0]) || containsName(p2Names, split[0])) {
                int winIndex = isDouble ? 3 : 1;
                int loseIndex = isDouble ? 4 : 2;
                if (containsName(winnerNames, split[0])) {
                    split[winIndex] = String.valueOf(Integer.parseInt(split[winIndex]) + 1);
                } else {
                    split[loseIndex] = String.valueOf(Integer.parseInt(split[loseIndex]) + 1);
                }

                for (int j = 0; j < p1Names.length; j++) {
                    if (p1Names[j].equals(split[0])) {
                        p1Found[j] = true;
                    }
                }
                for (int j = 0; j < p2Names.length; j++) {
                    if (p2Names[j].equals(split[0])) {
                        p2Found[j] = true;
                    }
                }

                line = String.join("|", split);
                stats.set(i, line);
            }
        }

        for (int j = 0; j < p1Names.length; j++) {
            if (!p1Found[j]) {
                String newLine = createRecord(p1Names[j], containsName(winnerNames, p1Names[j]), isDouble);
                stats.add(newLine);
            }
        }

        for (int j = 0; j < p2Names.length; j++) {
            if (!p2Found[j]) {
                String newLine = createRecord(p2Names[j], containsName(winnerNames, p2Names[j]), isDouble);
                stats.add(newLine);
            }
        }

        writeAll(stats);
    }
    
    public List<String> findAll() {
        List<String> records = new ArrayList<String>();

        File file = new File(path);
        if(file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(path))){
                String line = "";

                while ((line = br.readLine()) != null) {
                    records.add(line);        
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return records;
    }

    public void writeAll(List<String> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (String line : list) {
                bw.write(line);
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] normalizeRecord(String line) {
        String[] split = line.split("\\|");

        if (split.length >= 5) {
            return split;
        }

        return new String[] {
            split[0],
            split[1],
            split[2],
            "0",
            "0"
        };
    }

    private String createRecord(
            String name,
            boolean won,
            boolean isDouble) {

        if (isDouble) {
            return name + "|0|0|" + (won ? "1|0" : "0|1");
        }

        return name + "|" + (won ? "1|0|0|0" : "0|1|0|0");
    }

	public void clear() {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, false))) {

	    } catch (IOException e) {
	        System.out.println("파일 초기화 오류입니다.");
	        e.printStackTrace();
	    }
	}

}
