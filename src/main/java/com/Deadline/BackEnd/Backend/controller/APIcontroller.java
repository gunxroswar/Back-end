package com.Deadline.BackEnd.Backend.controller;
import com.Deadline.BackEnd.Backend.Objects.createPost;
import com.Deadline.BackEnd.Backend.Objects.login;
import com.Deadline.BackEnd.Backend.Objects.signup;
import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

@RestController
public class APIcontroller {

    static final String DB_URL = "jdbc:mysql://localhost:3306/backend_database";
    static final String USER = "root";
    static final String PASS = "admin1234";
    Connection conn = null;
    Statement stmt = null;

    public String autoPayloadBuilder(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        StringBuilder singleData = new StringBuilder();
        StringBuilder returnData = new StringBuilder();
        returnData.append("[");
        int rowCount = 0;
        while(rs.next()){
            rowCount++;
            singleData.append("{");
            for(int i = 1 ; i <= metaData.getColumnCount(); i++){
                singleData.append("\"" + metaData.getColumnName(i) + "\": \"" + rs.getString(i) + "\",");
            }
            singleData.deleteCharAt(singleData.length()-1);
            singleData.append("}");
            returnData.append(singleData + ",");
            singleData.delete(0, singleData.length()-1);
        }

        returnData.deleteCharAt(returnData.length()-1);
        if(rowCount == 1){
            returnData.deleteCharAt(0);
            return returnData.toString();
        }
        returnData.append("]");


        return returnData.toString();
    }

    public APIcontroller() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        stmt = conn.createStatement();
    }

    @GetMapping("/API/test")
    //@CrossOrigin(origins = "http://localhost:3000")
    public String test(){
        return "API Ok";
    }

    @PostMapping("/guests/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public String login(@RequestBody login info){
        String QUERY = "SELECT username, password, displayName FROM User ".concat("WHERE username='" + info.userName + "';");
        String existUser = null;
        String existPassword = null;
        String displayName = null;
        try{
            ResultSet rs = stmt.executeQuery(QUERY);
            while(rs.next()){
                existUser = rs.getString("username");
                existPassword = rs.getString("password");
                displayName = rs.getString("displayName");
            }
            if(existUser == null) return "400";
            else if(!Objects.equals(existPassword, info.password)) return "400";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "400";
        }

        return "{\"status\": 200, \"displayName\": \"" + displayName + "\"}";
    }

    @PostMapping("/guests/signin")
    @CrossOrigin(origins = "http://localhost:3000")
    public String signin(@RequestBody signup info){
        //String QUERY = "SELECT username, password FROM User;";
        String QUERY = "INSERT INTO User VALUES ('".concat(info.userName + "', '" + info.password + "', '" + info.displayName + "');");
        try{
            stmt.executeUpdate(QUERY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "200";
        }
        return "{\"status\": 200, \"displayName\": \"" + info.displayName + "\"}";
    }

    @PostMapping("/posts/create")
    @CrossOrigin(origins = "http://localhost:3000")
    public String createPost(@RequestBody createPost cp){
        int maxPostID = 0;
        int postOwner = 0;
        String topic = cp.Payload.topic;
        String timeStamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        String detail = cp.Payload.detail;
        int likeCount = 0;
        int annoymous = 0;
        int hasVerify = 0;
        int postStatus = 0;

        String MAXIndexQUERY = "SELECT MAX(PostID) FROM Posts;";
        try{
            ResultSet rs = stmt.executeQuery(MAXIndexQUERY);
            while(rs.next()){
                maxPostID = rs.getInt("MAX(PostID)")+1;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "200";
        }

        String QUERY = "INSERT INTO Posts VALUES (".concat(maxPostID + ", " + postOwner + ", '" + topic + "', '" + timeStamp + "', '" + detail + "', " + likeCount + ", " + annoymous + ", " + hasVerify + ", " + postStatus + ");");
        try{
            stmt.executeUpdate(QUERY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "200";
        }


        return "OK";
    }

    @GetMapping("/posts")
    @CrossOrigin(origins = "http://localhost:3000")
    public String getPost(@RequestParam("postId") int id){
        String sendBack;
        String QUERY = "SELECT Topic, Detail , TimeStamp, LikeCount FROM Posts WHERE postId = ".concat(id + ";");
        try{
            ResultSet rs = stmt.executeQuery(QUERY);
            sendBack = autoPayloadBuilder(rs);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "200";
        }

        return sendBack;
    }

    @GetMapping("/pages")
    @CrossOrigin(origins = "http://localhost:3000")
    public String getPage(@RequestParam("page") int id){
        String sendBack;
        String QUERY = "SELECT PostID, user.username , Topic, Detail , TimeStamp, LikeCount, hasVerify \n" +
                "FROM Posts \n" +
                "INNER JOIN user ON PostOwner = user.uid\n" +
                "ORDER BY TimeStamp DESC LIMIT 10;";
        try{
            ResultSet rs = stmt.executeQuery(QUERY);
            sendBack = autoPayloadBuilder(rs);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "200";
        }

        return sendBack;
    }

    /*
    @PostMapping("/users/logout")
    @CrossOrigin(origins = "http://localhost:3000")
    public String logout(){
        return "OK";
    }

    @GetMapping("/posts/filterTag")
    @CrossOrigin(origins = "http://localhost:3000")
    public String filterTag(@RequestParam("tagID") int id){
        System.out.println(id);
        return "OK";
    }

    @GetMapping("/posts")
    @CrossOrigin(origins = "http://localhost:3000")
    public String getPage(@RequestParam("page") int id){
        System.out.println(id);
        return "OK";
    }

    @PostMapping("/posts/sorting/date")
    @CrossOrigin(origins = "http://localhost:3000")
    public String sortingPostBydate(){
        return "OK";
    }

    @PostMapping("/comments")
    @CrossOrigin(origins = "http://localhost:3000")
    public String getComment(@RequestParam("commentId") int id){
        return "OK";
    }

    @PostMapping("/comments")
    @CrossOrigin(origins = "http://localhost:3000")
    public String createComment(int id){
        return "OK";
    }

    @PostMapping("/comments")
    @CrossOrigin(origins = "http://localhost:3000")
    public String verifyComment(@RequestParam("verify") int id){

        return "OK";
    }

    @PostMapping("/comments")
    @CrossOrigin(origins = "http://localhost:3000")
    public String unverifyComment(@RequestParam("unverify") int id){

        return "OK";
    }
    */
}