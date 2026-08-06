package com.test.java.app;

import java.util.List;
import java.util.Scanner;

import com.test.java.ui.ScoreBoard;
import com.test.java.admin.AdminService;
import com.test.java.entity.Player;
import com.test.java.matchoperator.TableTennisMatch;
import com.test.java.repository.MatchFileRepository;
import com.test.java.repository.PlayerFileRepository;

public class App {
	private static final Scanner sc = new Scanner(System.in);
	private static Player p1, p2;
	private static TableTennisMatch match;
	private static ScoreBoard sb = new ScoreBoard();
	private static MatchFileRepository matchRepo;
	private static PlayerFileRepository playerRepo;

	public static void main(String[] args) {
		String match_path = "./data/match_record.txt";
		String player_path = "./data/player_record.txt";
		matchRepo = new MatchFileRepository(match_path);
		playerRepo = new PlayerFileRepository(player_path);
		run();
		sc.close();
		System.out.println("프로그램 종료");

	}

	private static void run() {
		boolean loop = true;

		while (loop) {
			printMainMenu();

			switch (inputMenuNumber()) {
			case 1:
				startNewMatch();
				break;
			case 2:
				showMatchRecord();
				break;
			case 3:
				showPlayerStats();
				break;
			case 4:
				AdminService admin = new AdminService(sc,matchRepo,playerRepo);
				admin.run();
				break;
			case 5:
				loop = false;
				break;
			default:
				break;
			}

			System.out.println();
		}
	}

	public static void printMainMenu() {
		System.out.println("===== 탁구 스코어 프로그램 =====");
		System.out.println("1. 새 경기 시작");
		System.out.println("2. 경기 기록 조회");
		System.out.println("3. 선수 전적 조회");
		System.out.println("4. 관리자 모드");
		System.out.println("5. 종료");
		System.out.print("선택: ");

	}

	private static void startNewMatch() {
		System.out.println("새 경기 시작");
		boolean isDouble = isDouble();

		if (isDouble) {
			while (true) {
				System.out.println("1번 선수 이름 입력: ");
				String name1 = sc.nextLine();
				System.out.println("2번 선수 이름 입력: ");
				String name2 = sc.nextLine();
				System.out.println("3번 선수 이름 입력: ");
				String name3 = sc.nextLine();
				System.out.println("4번 선수 이름 입력: ");
				String name4 = sc.nextLine();

				if (name1.isEmpty() || name2.isEmpty() || name3.isEmpty() || name4.isEmpty()) {
					System.out.println("이름을 다시 입력하세요");
					continue;
				}

				p1 = new Player(name1 + "," + name2);
				p2 = new Player(name3 + "," + name4);
				break;
			}
		} else {
			inputPlayerName();
		}
		int rule = inputRule();

		match = new TableTennisMatch(p1, p2, rule);

		// 경기 진행

		while (match.isFinished() == false) {
			sb.printCurrentScore(match); // 현재 점수

			switch (inputPointWinner()) {
				case 1:
					match.addPoint(p1);
					break;
				case 2:
					match.addPoint(p2);
					break;
				default:
					break;
			}

		}

		if (match.isFinished()) {
			sb.printGameResult(match);
			matchRepo.save(match, isDouble);
			playerRepo.save(match, isDouble);
		}

	}
	
	 private static void showMatchRecord() {      
	      while (true) {
	         // System.out.print("1. 선수 이름으로 경기 검색 2. 경기 정렬 검색 : ");
	         System.out.println();
	         System.out.println("===== 경기 기록 조회 =====");
	         System.out.println("1. 선수 이름으로 경기 검색");
	         System.out.println("2. 경기 정렬 검색");
	         System.out.println("3. 경기 통계 확인");
	         System.out.println("0. 뒤로 가기");
	         System.out.print("선택: ");
	         int num = Integer.parseInt(sc.nextLine());
	         
	         switch (num) {
	            case 0:
	               // 메인 메뉴로 
	               System.out.println();
	               return;
	            case 1:
	               // 선수 이름으로 경기 검색
	               searchRecord();
	               System.out.println();
	               break;
	            case 2:
	               // 경기 정렬 검색
	               sortMatchRecord();
	               System.out.println();
	               System.out.println();
	               break;
	            case 3:
	                // 경기 통계 확인
	               matchRepo.matchStatistics();
	               System.out.println();
	                break;
	            default:
	               break;
	         }
	      }
	   }
	
	 private static void searchRecord() {
			System.out.println();
			boolean find = false;
			List<String> records = matchRepo.findAll();
			System.out.print("검색할 선수 입력: ");
			String name = sc.nextLine().trim();

			try {
				for (int i = 0; i < records.size(); i++) {
					String line = records.get(i);
					String[] split = line.split("\\|");
					
					if (containsPlayerName(split, name)) {
						System.out.println(line);
						find = true;
					}
				}

				if (!find) {
					System.out.println("해당 선수가 없습니다.");
				}

			} catch (Exception e) {
			}
		}
	 
	private static void sortMatchRecord() {
		List<String> records = matchRepo.findAll();

		if (!records.isEmpty()) {
			while (true) {
				try {
					System.out.println();
					System.out.println("===== 정렬 선택 =====");
					System.out.println("1. 오름차 정렬");
					System.out.println("2. 내림차 정렬");
					System.out.print("선택: ");
					int sort = Integer.parseInt(sc.nextLine());

					if (sort == 1) { // 오름차
						for (String line : records) {
							System.out.println(line);
						}
						return;
					} else if (sort == 2) {
						// 내림차
						for (int i = records.size()-1; i >=0; i--) {
							String line = records.get(i);
							System.out.println(line);
						}
						return;
					} else {
						System.out.println("1,2만 입력하세요.");
					}

				} catch (Exception e) {
				}
			}
		} else {
			System.out.println("경기 기록이 없습니다.");
		}

	}


	private static void showPlayerStats() {
		System.out.println();
		List<String> stats = playerRepo.findAll();

		if (!stats.isEmpty()) {
			while (true) {
				System.out.println();
				boolean playerExists = false;
				System.out.print("검색할 선수 이름 입력(0: 메인메뉴): ");

				try {
					String name = sc.nextLine();

					if (name.equals("0"))
						return;

					for (int i = 0; i < stats.size(); i++) {
						String line = stats.get(i);
						String[] split = line.split("\\|");

						if (split[0].equals(name)) {
							playerExists = true;
							int singlesWin = Integer.parseInt(split[1]);
							int singlesLose = Integer.parseInt(split[2]);
							int doublesWin = split.length >= 5 ? Integer.parseInt(split[3]) : 0;
							int doublesLose = split.length >= 5 ? Integer.parseInt(split[4]) : 0;
							int win = singlesWin + doublesWin;
							int lose = singlesLose + doublesLose;
							int total = win + lose;
							double singlesWr = calculateWinRate(singlesWin, singlesLose);
							double doublesWr = calculateWinRate(doublesWin, doublesLose);
							double totalWr = calculateWinRate(win, lose);
							System.out.printf("이름: %s\t단식: %d승 %d패\t복식: %d승 %d패\t총 경기: %d\t승률: %.1f%%\n",
									split[0], singlesWin, singlesLose, doublesWin, doublesLose, total, totalWr);
							System.out.printf("\t단식 승률: %.1f%%\t복식 승률: %.1f%%\t전체 승률: %.1f%%\n",
									singlesWr, doublesWr, totalWr);

							break;
						}
					}
					if (!playerExists) {
						throw new Exception();
					}
				} catch (Exception e) {
					System.out.println("해당 선수가 없습니다.");
				}
				System.out.println("");
			}
		} else {
			System.out.println("선수 전적 기록이 없습니다.");
		}
		System.out.println();
	}
	
	private static void inputPlayerName() {
		String pn1, pn2;

		while (true) {
			System.out.print("1번 선수 이름 입력: ");
			pn1 = sc.nextLine().trim();

			System.out.print("2번 선수 이름 입력: ");
			pn2 = sc.nextLine().trim();

			// 한 명이라도 비어 있으면 다시 입력
			if (pn1.isEmpty() || pn2.isEmpty()) {
				System.out.println("두 선수의 이름을 모두 입력하세요.");
				continue;
			}

			// 둘 다 입력된 경우에만 선수 생성
			p1 = new Player(pn1);
			p2 = new Player(pn2);
			break;
		}

	}

	private static boolean isDouble() {
		String gameType;

		while (true) {
			System.out.print("1.단식게임 || 2.복식게임 (1/2): ");
			gameType = sc.nextLine().trim();

			if (gameType.equals("1")) {
				return false;
			} else if (gameType.equals("2")) {
				return true;
			} else {
				System.out.println("1 또는 2를 입력해주세요.");
				continue;
			}

		}

	}

	private static int inputMenuNumber() {
		while (true) {

			try {
				int input = Integer.parseInt(sc.nextLine());

				if (input == 1 || input == 2 || input == 3 || input == 4 || input == 5) {
					return input;
				}
			} catch (NumberFormatException e) {
			}

			System.out.println("1 ~ 4만 입력하세요.");
		}
	}

	private static int inputPointWinner() {
		while (true) {
			System.out.print("포인트를 얻은 선수 번호 입력(1 또는 2): ");
			String input = sc.nextLine();

			try {
				int playerNum = Integer.parseInt(input);

				if (playerNum == 1 || playerNum == 2) {
					return playerNum;
				}
			} catch (NumberFormatException e) {
			}

			System.out.println("1 또는 2만 입력하세요.");
		}
	}

	private static int inputRule() {
		while (true) {
			System.out.print("경기 규칙을 선택하세요\r\n1: 5판 3선승 / 2: 3판 2선승 ");
			String input = sc.nextLine().trim();
			try {
				int rule = Integer.parseInt(input);
				if (rule == 1 || rule == 2) {
					return rule;
				}
			} catch (NumberFormatException e) {
			}

			System.out.println("1 또는 2만 입력하세요.");
		}

	}
	private static double calculateWinRate(int win, int lose) {
	    int total = win + lose;

	    if (total == 0) {
	        return 0.0;
	    }

	    return (double) win / total * 100;
	}
	
	private static boolean containsPlayerName(String[] fields, String name) {
		if (fields.length < 3) {
			return false;
		}

		// 새 형식: 날짜|SINGLES 또는 DOUBLES|팀1|팀2|...
		// 이전 단식 형식: 날짜|선수1|선수2|...
		int firstTeamIndex = (fields[1].equals("SINGLES") || fields[1].equals("DOUBLES")) ? 2 : 1;
		if (fields.length <= firstTeamIndex + 1) {
			return false;
		}

		return playerRepo.containsName(fields[firstTeamIndex].split(","), name)
				|| playerRepo.containsName(fields[firstTeamIndex + 1].split(","), name);
	}
}
