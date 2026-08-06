package com.test.java.set;

public class SetResult {
	
	private int setNumber = 0; //몇 번째 세트인지
	
	private int player1Point = 0; //1번 선수 세트 최종 점수
	private int player2Point = 0; //2번 선수 세트 최종 점수
	private String setWinnerName = ""; //이 세트의 승자 이름 저장
	
	
	
	public SetResult(int setNumber, int player1Point, int player2Point, String setWinnerName) {
		super(); //생성자
		this.setNumber = setNumber;
		this.player1Point = player1Point;
		this.player2Point = player2Point;
		this.setWinnerName = setWinnerName;
	}



	public int getSetNumber() { //몇 번째 세트인지 가져오기
		return setNumber; 
	}



	public int getPlayer1Point() {//1번 선수 세트 최종 점수 가젹오기
		return player1Point;
	}



	public int getPlayer2Point() {//2번 선수 세트 최종 점수 가젹오기
		return player2Point;
	}



	public String getSetWinnerName() { //이 세트의 승자 이름 가져오기
		return setWinnerName;
	}
	
	

}
