package com.test.java.admin;

import java.util.Scanner;

import com.test.java.repository.MatchFileRepository;
import com.test.java.repository.PlayerFileRepository;

public class AdminService {
	private static final String ADMIN_ID = "admin";
	private static final String ADMIN_PW = "1234";

	private Scanner sc;

	private MatchFileRepository matchRepo;
	private PlayerFileRepository playerRepo;

	public AdminService(Scanner sc, MatchFileRepository matchRepo, PlayerFileRepository playerRepo) {
		this.sc = sc;
		this.matchRepo = matchRepo;
		this.playerRepo = playerRepo;
	}

	public void run() {
		boolean loop = true;
		login();
		while (loop) {

			printAdminMenu();

			switch (inputAdminMenuNumber()) {
			case 1:
				int target = chooseResetTarget();
				if (target == 1) {
					resetMatchRecord();
				} else {
					resetPlayerRecord();
				}
				break;
			case 2:
				loop = false;
				break;
			default:
				break;
			}

			System.out.println();
		}
	}

	public void login() {
		System.out.println("===== 관리자모드 =====");
		while (true) {
			System.out.print("아이디 : ");
			String id = sc.nextLine().trim();
			if (!id.equals(ADMIN_ID)) {
				System.out.println("아이디가 틀렸습니다.");
				continue;
			}
			break;
		}
		while (true) {
			System.out.print("비밀번호 : ");
			String pw = sc.nextLine().trim();
			if (!pw.equals(ADMIN_PW)) {
				System.out.println("비밀번호가 틀렸습니다.");
				continue;
			}
			break;
		}
		System.out.println("로그인 성공 . ");
	}

	public void printAdminMenu() {
		System.out.println("=====관리자메뉴=====");
		System.out.println("1. 기록 초기화");
		System.out.println("2. 관리자모드 종료");

	}

	public int inputAdminMenuNumber() {
		while (true) {

			try {
				int input = Integer.parseInt(sc.nextLine());

				if (input == 1 || input == 2) {
					return input;
				}
			} catch (NumberFormatException e) {
			}

			System.out.println("1 ~ 2만 입력하세요.");
		}

	}

	public void resetMatchRecord() {
	    if (inputConfirm("경기 기록을 정말 삭제하시겠습니까?")) {
	        matchRepo.clear();  
	        System.out.println("삭제되었습니다.");
	    }
	}

	public void resetPlayerRecord() {
	    if (inputConfirm("개인 기록을 정말 삭제하시겠습니까?")) {
	        playerRepo.clear();  
	        System.out.println("삭제되었습니다.");
	    }
	}

	public boolean inputConfirm(String message) {
		
		while (true) {
			System.out.print(message + " (y/n) : ");
			String input = sc.nextLine().trim().toLowerCase();
			if (input.equals("y")) {
				return true;
			} else if (input.equals("n")) {
				return false;
			} else {
				System.out.println("오류");
			}
		}
	}

	private int chooseResetTarget() {
		System.out.println("=====관리자메뉴=====");
		System.out.println("1. 경기 기록 초기화");
		System.out.println("2. 개인 기록 초기화");
		while (true) {

			try {
				int input = Integer.parseInt(sc.nextLine());

				if (input == 1 || input == 2) {
					return input;
				}
			} catch (NumberFormatException e) {
			}

			System.out.println("1 ~ 2만 입력하세요.");
		}

	}
}
