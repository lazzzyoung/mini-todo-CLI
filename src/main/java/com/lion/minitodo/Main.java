package com.lion.minitodo;

import com.lion.minitodo.dao.JdbcTodoDAO;
import com.lion.minitodo.dao.JdbcUserDAO;
import com.lion.minitodo.dao.TodoDAO;
import com.lion.minitodo.dao.UserDAO;
import com.lion.minitodo.dto.TodoDTO;
import com.lion.minitodo.dto.UserDTO;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final UserDAO userDAO = new JdbcUserDAO();
    private static final TodoDAO todoDAO = new JdbcTodoDAO();

    private static Integer sessionUserId = null; // 로그인된 사용자 id

    public static void main(String[] args) {
        while (true) {
            if (sessionUserId == null) showAuthMenu();
            else showTodoMenu();
        }
    }


    private static void showAuthMenu() {
        System.out.println("\n==== Mini Todo CLI ====");
        System.out.println("1) 회원가입  2) 로그인  3) 종료");
        System.out.print("선택: ");
        String input = sc.nextLine().trim();

        switch (input) {
            case "1" -> register();
            case "2" -> login();
            case "3" -> {
                System.out.println("종료합니다.");
                System.exit(0);
            }
            default -> System.out.println("잘못된 선택입니다.");
        }
    }

    private static void register() {
        try {
            System.out.print("이메일: ");
            String email = sc.nextLine().trim();
            if (email.isBlank()) {
                System.out.println("이메일은 비워둘 수 없습니다.");
                return;
            }
            if (userDAO.existsByEmail(email)) {
                System.out.println("이미 존재하는 이메일입니다.");
                return;
            }

            System.out.print("비밀번호: ");
            String pw = sc.nextLine();
            if (pw.isBlank()) {
                System.out.println("비밀번호는 비워둘 수 없습니다.");
                return;
            }

            UserDTO u = new UserDTO();
            u.setEmail(email);
            u.setPassword(pw); // 추후 해싱기능 추가해야함
            int newId = userDAO.create(u);

            if (newId > 0) System.out.println("회원가입 완료! user_id=" + newId);
            else System.out.println("회원가입 실패");
        } catch (Exception e) {
            System.out.println("회원가입 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void login() {
        try {
            System.out.print("이메일: ");
            String email = sc.nextLine().trim();
            System.out.print("비밀번호: ");
            String pw = sc.nextLine();

            UserDTO u = userDAO.findByEmail(email);
            if (u == null || !pw.equals(u.getPassword())) {
                System.out.println("로그인 실패: 이메일 또는 비밀번호가 올바르지 않습니다.");
                return;
            }
            sessionUserId = u.getUserId();
            System.out.println("로그인 성공! 환영합니다, " + u.getEmail());
        } catch (Exception e) {
            System.out.println("로그인 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void showTodoMenu() {
        System.out.println("\n==== Todo Menu (user_id=" + sessionUserId + ") ====");
        System.out.println("1) 할 일 추가");
        System.out.println("2) 할 일 목록 보기");
        System.out.println("3) 할 일 완료/미완료 토글");
        System.out.println("4) 할 일 삭제");
        System.out.println("5) 로그아웃");
        System.out.print("선택: ");
        String input = sc.nextLine().trim();

        switch (input) {
            case "1" -> addTodo();
            case "2" -> listTodos();
            case "3" -> toggleDone();
            case "4" -> deleteTodo();
            case "5" -> {
                sessionUserId = null;
                System.out.println("로그아웃되었습니다.");
            }
            default -> System.out.println("잘못된 선택입니다.");
        }
    }

    private static void addTodo() {
        try {
            System.out.print("제목: ");
            String title = sc.nextLine();
            if (title.isBlank()) {
                System.out.println("제목은 비워둘 수 없습니다.");
                return;
            }

            System.out.print("내용(선택): ");
            String content = sc.nextLine();

            System.out.print("마감일(YYYY-MM-DD, 없으면 엔터): ");
            String due = sc.nextLine().trim();
            if (due.isBlank()) due = null;

            TodoDTO t = new TodoDTO();
            t.setUserId(sessionUserId);
            t.setTitle(title);
            t.setContent(content);
            t.setDue(due);
            t.setDone(false);

            int newId = todoDAO.create(t);
            System.out.println(newId > 0 ? "할 일 추가됨 (todo_id=" + newId + ")" : "추가 실패");
        } catch (Exception e) {
            System.out.println("할 일 추가 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listTodos() {
        try {
            List<TodoDTO> list = todoDAO.findByUser(sessionUserId);
            if (list.isEmpty()) {
                System.out.println("등록된 할 일이 없습니다.");
                return;
            }
            System.out.println("\n[목록] (마감일 오름차순)");
            for (TodoDTO t : list) {
                System.out.printf(" - [%d] 제목: %s | 내용: %s | 마감: %s | 완료: %s%n",
                        t.getTodoId(),
                        nullToEmpty(t.getTitle()),
                        nullToDash(t.getContent()),
                        nullToDash(t.getDue()),
                        t.isDone() ? "Y" : "N"
                );
            }
        } catch (Exception e) {
            System.out.println("목록 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void toggleDone() {
        try {
            System.out.print("토글할 todo_id: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            // 현재 상태 확인
            TodoDTO found = todoDAO.findById(id);
            if (found == null || found.getUserId() != sessionUserId) {
                System.out.println("존재하지 않거나 내 할 일이 아닙니다.");
                return;
            }
            boolean next = !found.isDone();
            int updated = todoDAO.markDone(id, next);
            System.out.println(updated > 0 ? ("상태 변경 완료: " + (next ? "완료" : "미완료")) : "변경 실패");
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력해주세요.");
        } catch (Exception e) {
            System.out.println("상태 변경 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void deleteTodo() {
        try {
            System.out.print("삭제할 todo_id: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            // 소유자 확인
            TodoDTO found = todoDAO.findById(id);
            if (found == null || found.getUserId() != sessionUserId) {
                System.out.println("존재하지 않거나 내 할 일이 아닙니다.");
                return;
            }
            int deleted = todoDAO.delete(id);
            System.out.println(deleted > 0 ? "삭제 완료" : "삭제 실패");
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력해주세요.");
        } catch (Exception e) {
            System.out.println("삭제 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* ========== 유틸 ========== */
    private static String nullToDash(String s) { return (s == null || s.isBlank()) ? "-" : s; }
    private static String nullToEmpty(String s) { return s == null ? "" : s; }
}
