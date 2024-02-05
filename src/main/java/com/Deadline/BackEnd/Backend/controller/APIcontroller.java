package com.Deadline.BackEnd.Backend.controller;
import com.Deadline.BackEnd.Backend.Objects.createComment;
import com.Deadline.BackEnd.Backend.Objects.createPost;
import com.Deadline.BackEnd.Backend.Objects.login;
import com.Deadline.BackEnd.Backend.Objects.signin;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
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

    private String autoPayloadBuilder(ResultSet rs) throws SQLException {
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
            singleData.delete(0, singleData.length());
        }

        returnData.deleteCharAt(returnData.length()-1);
        if(rowCount == 1){
            returnData.deleteCharAt(0);
            return returnData.toString();
        } else if (rowCount == 0) {
            return "[]";
        }
        returnData.append("]");


        return returnData.toString();
    }

    private boolean IsSQLInjection(String value){
        try{
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM sqlinjection_test\n" +
                            "WHERE testid = " + value + " AND test_value = " + value + ";");
            if(rs.next()) return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
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

    @PostMapping("/guests1/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public String login(@RequestBody login info){
        if(IsSQLInjection(info.userName)) return "401 Unauthorized";
        String QUERY = "SELECT username, password, profile_name FROM user ".concat("WHERE username='" + info.userName + "';");
        String existUser = "null";
        String existPassword = "null";
        String displayName = "null";
        try{
            ResultSet rs = stmt.executeQuery(QUERY);
            if(!rs.next()) return "User name or password incorrect";
            existUser = rs.getString("username");
            existPassword = rs.getString("password");
            if(!Objects.equals(existPassword, info.password)) return "User name or password incorrect";
            displayName = rs.getString("displayName");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "400";
        }

        return "{\"status\": 200, \"displayName\": \"" + displayName + "\"}";
    }

    @PostMapping("/guests/signin")
    @CrossOrigin(origins = "http://localhost:3000")
    public String signin(@RequestBody signin info){
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
        BigInteger postid;
        int anonymous = 1;
        String create_at = String.valueOf(new Timestamp(System.currentTimeMillis()));
        int has_verify = 0;
        String topic = cp.Payload.topic;
        String detail = cp.Payload.detail;
        BigInteger owner_id = BigInteger.valueOf(1);
        BigInteger like_count = BigInteger.valueOf(1);
        BigInteger status_id = BigInteger.valueOf(1);


        String QUERY = "SELECT MAX(PostID) FROM post;";
        try{
            ResultSet rs = stmt.executeQuery(QUERY);
            rs.next();
            postid = BigInteger.valueOf(rs.getLong("MAX(PostID)"));
            postid = postid.add(BigInteger.valueOf(1));
            QUERY = "INSERT INTO post VALUES (" + postid + ", " + anonymous + ", '" + create_at + "', '" + detail + "', " + has_verify + ", " + like_count + ", " + ", '" + topic + "', '" + create_at + "', " + status_id + ", "  + owner_id + ");";
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

    @GetMapping("/comments")
    @CrossOrigin(origins = "http://localhost:3000")
    public String getComment(@RequestParam("commentId") int post_id){
        String sendBack;
        String QUERY =
                "SELECT commentid as 'CommentID', user.name as 'displayName', like_count as 'LikeAmount', is_verify as 'hasVerify', 0 as 'replyAmount', create_at as 'CreateDate', detail\n" +
                "FROM comment\n" +
                "INNER JOIN user ON user.uid = comment.ower_id\n" +
                "WHERE comment.post_id = " + post_id + ";";

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

    @PostMapping("/comments/create")
    @CrossOrigin(origins = "http://localhost:3000")
    public String createComment(@RequestBody createComment info){
        BigInteger commentid;
        int anonymous = 1;
        String create_at = String.valueOf(new Timestamp(System.currentTimeMillis()));
        String detail = info.Payload.detail;
        int is_verify = 0;
        String topic;
        BigInteger post_id = info.Payload.PostID;
        BigInteger post_status_id = BigInteger.valueOf(1);
        BigInteger owner_id = BigInteger.valueOf(1);
        BigInteger like_count = BigInteger.valueOf(1);
        String QUERY;
        try{
            QUERY = "SELECT MAX(commentid) FROM comment;";
            ResultSet rs = stmt.executeQuery(QUERY);
            rs.next();
            commentid = BigInteger.valueOf(rs.getLong("MAX(commentid)"));
            commentid = commentid.add(BigInteger.valueOf(1));

            QUERY = "SELECT Topic FROM Posts WHERE Posts.PostID = " + info.Payload.PostID + ";";
            rs = stmt.executeQuery(QUERY);
            rs.next();
            topic = rs.getString("Topic");

            QUERY = "INSERT INTO comment VALUES (" + commentid + ", " + anonymous + ", '" + create_at + "', '" + detail + "', " + is_verify + ", '" + topic + "', '" + create_at + "', " + post_id + ", " + post_status_id + ", " + owner_id + ", " + like_count + ");";
            stmt.executeUpdate(QUERY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "200";
        }


       return "201";
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