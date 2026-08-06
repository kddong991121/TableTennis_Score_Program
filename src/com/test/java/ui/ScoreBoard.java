package com.test.java.ui;
import com.test.java.matchoperator.TableTennisMatch;
import java.util.List;
import com.test.java.set.SetResult;

public class ScoreBoard {
   

   
   //현재 점수 출력
    public void printCurrentScore (TableTennisMatch match) {
         
         
         System.out.println("=========================================");
         
         // 1. 경기 방식 및 현재 세트 스코어 표시 
         String ruleText = (match.getRule() == 1) ? "5판3선승" : "3판2선승";
         System.out.printf("[%s] 세트 스코어 | %s %d : %s %d\r\n",
               ruleText,
               match.player1.getName(),
               match.getPlayer1SetWins(),
               match.player2.getName(),
               match.getPlayer2SetWins());
         
         
         if (match.isDeuce()) {
            printDeuce();
            System.out.printf("| %s %d  :  %s %d 서브 : %s\r\n"
                  ,match.player1.getName()
                  ,match.player1.getPoint()
                  ,match.player2.getName()
                  ,match.player2.getPoint()
                  ,match.serve().getName());
         } else if (match.isMatchPoint()) {
            printMatchPoint();
            System.out.printf("| %s %d  :  %s %d 서브 : %s\r\n"
                  ,match.player1.getName()
                  ,match.player1.getPoint()
                  ,match.player2.getName()
                  ,match.player2.getPoint()
                  ,match.serve().getName());
         } else {
         System.out.printf("[현재 점수]| %s %d  :  %s %d 서브 : %s\r\n"
               ,match.player1.getName()
               ,match.player1.getPoint()
               ,match.player2.getName()
               ,match.player2.getPoint(),
               match.serve().getName());
         }
         
         
      }
      
   
   //Deuce 상태 출력
   public void printDeuce() {
      System.out.print("[Deuce]");
}
   // MatchPoint 상태 출력
   public void printMatchPoint() {
      System.out.print("[Match Point]");
}
   
   //경기 종료 결과 출력
   public void printGameResult(TableTennisMatch match) {
      
      System.out.println("=====================");
      System.out.println("   경기 결과      ");
      System.out.println("=====================");
      
      System.out.printf("Game Winner: %s\r\n", match.getWinner().getName());
        System.out.printf("최종 세트 스코어 : %s %d  :  %s %d\r\n",
                match.player1.getName(),
                match.getPlayer1SetWins(),  // 👈 0 대신 최종 세트 승수 출력
                match.player2.getName(),
                match.getPlayer2SetWins()); // 👈 0 대신 최종 세트 승수 출력
        
        System.out.println("---------------------");
        System.out.println("[세트별 상세 점수]");
        for (SetResult s : match.getSets()) {
            System.out.printf(" %d세트: %d : %d (승자: %s)\r\n",
                    s.getSetNumber(),
                    s.getPlayer1Point(),
                    s.getPlayer2Point(),
                    s.getSetWinnerName());
        }
   }
   
   //저장된 경기 기록 출력 
   public void printRecords(List<String> records) {
      
      System.out.println("=====================");
      System.out.println("   경기 기록 목록      ");
      System.out.println("=====================");
      
      if (records == null) {
         System.out.println( "저장된 경기 기록이 없습니다.");
      } else {
         // 순번과 함께 저장된 경기 기록 출력
         for (int i = 0; i < records.size(); i++) {
            System.out.println((i + 1) + ". " + records.get(i));
         }
      }
      
      
   }
      
   //오류 메시지 출력
   public void printError(String message) {
      System.out.println("[오류]" + message);
   }
   
   
}
