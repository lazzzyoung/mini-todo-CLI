package com.lion.minitodo;

import com.lion.minitodo.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        System.out.println("DB 연결 테스트");
        try(
                Connection conn = DBUtil.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT NOW() AS now, DATABASE() AS db")
                ) {
            if(rs.next()){
                System.out.println("연결 성공");
                System.out.println("현재 시간: " + rs.getString("now"));
                System.out.println("DB 구조: " + rs.getString("db"));
            }
        } catch (Exception e){
            System.out.println("연결 실패");
            e.printStackTrace();
        }
    }
}
