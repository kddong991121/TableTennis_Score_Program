package com.test.java.entity;

public class Player {
	private String name = "";	//이름 
	private int point = 0;		//점수
	
	public Player(String name) {	//생성자
		this.name = name;
	}
	
	public String getName() {				//이름가져오기
		return this.name;
	}
	
	public int getPoint() {				//점수 가져오기
		return this.point;
	}
	
	public void addPoint() {				//점수 1점 추가
		point++;
	}
	
	public void resetPoint() {				//세트끝나면 포인트 0으로 초기화
		this.point = 0;
	}
}
