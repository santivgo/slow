/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.slow.dal;
import java.sql.*;

/**
 *
 * @author sant
 */
public class Conexao {
    public static Connection conector(){
        java.sql.Connection connect = null;
        String url = "jdbc:mysql://localhost:3306/slowdb";
        String user = "root";
        String password = "99586090";

        
        try {
            connect = DriverManager.getConnection(url, user, password);
            return connect;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
