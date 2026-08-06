package com.test.java.matchoperator;

import java.util.ArrayList;
import java.util.List;
import com.test.java.set.SetResult;

import com.test.java.entity.Player;

public class TableTennisMatch {

   public Player player1;
   public Player player2;
   
   private int rule; //   1(5판3선승) or 2(3판2선승)
   public int getRule() {
   return rule;
   }

   public int getPlayer1SetWins() { 
   return player1SetWins;
   }

   public int getPlayer2SetWins() {
   return player2SetWins;
   }
   public List<SetResult> getSets() {
       return setResults;
   }

   private int targetSets; //규칙에 따라 결정 (3판2선승→2, 5판3선승→3)
   private int player1SetWins = 0; //1번 선수가 이긴 세트 수
   private int player2SetWins = 0;//2번 선수가 이긴 세트 수
   private List<SetResult> setResults = new ArrayList<>(); //완료된 세트들의 기록
   
 
   boolean finished = false; //경기 전체가 끝났는지(세트 하나 끝난 것과는 다름)
   Player winner; //경기 전체 승자(경기 종료 전에는 null)
   
   public Player getPlayer1() {
      return player1;
   }

   public Player getPlayer2() {
      return player2;
   }

   public TableTennisMatch(Player player1, Player player2, int rule) {   // 생성자
      this.player1 = player1;
      this.player2 = player2;
      this.rule = rule;
   
   
      if (rule ==1) { //1 : 5판 3선승
         this.targetSets =3; //3세트 선취
      } else if (rule ==2) { //2: 3판 2선승
         this.targetSets =2; //2세트 선취
      } 
      
   }
   
   public void addPoint(Player player) {
       if (player == player1) {      //매개변수가 player1이면
           player1.addPoint();         //player1 1점획득
       } else if (player == player2) {   //매개변수가 player2면
           player2.addPoint();         //player2 1점획득
       } else {                  //매개변수가 1도아니고 2도 아니면
           throw new IllegalArgumentException("경기에 참여하지 않은 선수입니다.");   //예외처리
       }
       checkSetFinished(); //세트 종료 여부
   }
   
   public boolean isDeuce() {   //듀스인가요 ?
       return player1.getPoint() >= 10 && player1.getPoint() == player2.getPoint();
   }
   
   public boolean isMatchPoint() { //매치포인트인가요?
       int p1 = player1.getPoint();
       int p2 = player2.getPoint();

       boolean p1MatchPoint = (p1 + 1 >= 11) && Math.abs((p1 + 1) - p2) >= 2;
       boolean p2MatchPoint = (p2 + 1 >= 11) && Math.abs(p1 - (p2 + 1)) >= 2;

       return p1MatchPoint || p2MatchPoint;
   }
   
   public Boolean isFinished() { //경기가 완전히 종료되었는지
      return this.finished;
   }
   
   public Player getWinner() {
      return winner;   //승자 반환
   }
   
   public String getScoreText(Player player) {
       return String.format("%s : %d ", player.getName(),player.getPoint()); //매개변수로 들어온 player의 이름과 점수 출력
   }
   
   
   void checkSetFinished() { //한 세트의 경기가 끝난 경우
      boolean reachedEleven = player1.getPoint() >= 11 || player2.getPoint() >= 11;   //11점에 도달했는가 ? t/f
      boolean diffTwoOrMore = Math.abs(player1.getPoint() - player2.getPoint()) >= 2;   //2점차이인가? t/f
      
      if (reachedEleven && diffTwoOrMore) {   //둘다만족하면
         Player setWinner  = (player1.getPoint() > player2.getPoint()) ? player1 : player2;   //세트의 승자결정
         
         //1. Set 기록 생성 및 추가
           int currentSetNumber = setResults.size() + 1;
           SetResult setResult = new SetResult(currentSetNumber, player1.getPoint(), player2.getPoint(), setWinner.getName());
           setResults.add(setResult);

           // 2. 세트 승수 증가
           if (setWinner == player1) {
               player1SetWins++;
           } else {
               player2SetWins++;
           }

           // 3. 세트 점수 리셋 (다음 세트를 위해 0:0으로)
           player1.resetPoint();
           player2.resetPoint();

           // 4. 세트가 끝났으므로 2단계인 '경기 전체 종료 여부'를 검사
           checkMatchFinished();
           
      
      
      
      }
   }
   
   
   void checkMatchFinished() { //경기 전체가 끝난 경우 (목표 세트 수 달성 여부)
      
      if (player1SetWins == targetSets) {
         this.winner =player1;
         this.finished = true;
         
      } else if (player2SetWins == targetSets) {
         this.winner = player2;
         this.finished = true;
      }
      
      
      
      
      
      
      
      
   }
   
   public Player serve() {

         /*
          * 일반적으로 2점마다 서버가 교체 (0-1, 2-3, 4-5...) 듀스(10:10 이후)에는 1점마다 서버가 교체
          */
         int interval;

         Player currentServer = this.player1; // 첫 서브는 player1부터

         if (!isDeuceZone()) { // 둘다 10:10상황이 아니면
            interval = 2; // 2점마다 서브권 바뀌도록
         } else { // 듀스면(둘다 10:10상황이면)
            interval = 1; // 1점마다 서브가 바뀌도록
         }

         int totalPoints = this.player1.getPoint() + this.player2.getPoint(); // 둘을 더해서
         int quotient = totalPoints / interval; // 몫을 먼저 구하고
         boolean player1Serves = (quotient % 2 == 0); // 그 몫이 짝수면 player1

         if (!player1Serves) {
            currentServer = this.player2;
         }

         return currentServer;
      }
      
      public boolean isDeuceZone() {
         return this.player1.getPoint() >= 10 && this.player2.getPoint() >= 10;
      }
   
   
   
   
   
   
   

}
