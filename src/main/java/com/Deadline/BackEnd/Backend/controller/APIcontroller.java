package com.Deadline.BackEnd.Backend.controller;
import com.Deadline.BackEnd.Backend.Objects.createComment;
import com.Deadline.BackEnd.Backend.Objects.createPost;
import com.Deadline.BackEnd.Backend.Objects.login;
import com.Deadline.BackEnd.Backend.Objects.signup;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

@RestController
public class APIcontroller {

    static final String DB_URL = "jdbc:mysql://localhost:3306/backend_database";
    static final String USER = "root";
    static final String PASS = "Kw050x\\>RaoM/WJO";
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
            rs.next();
            existUser = rs.getString("username");
            existPassword = rs.getString("password");
            displayName = rs.getString("displayName");

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
        String update_at = create_at;
        BigInteger post_status_id = BigInteger.valueOf(1);
        BigInteger owner_id = BigInteger.valueOf(1);
        BigInteger like_count = BigInteger.valueOf(1);
        BigInteger status_id = BigInteger.valueOf(1);


        String QUERY = "SELECT MAX(PostID) FROM post;";
        try{
            ResultSet rs = stmt.executeQuery(QUERY);
            rs.next();
            postid = BigInteger.valueOf(rs.getLong("MAX(PostID)"));
            postid = postid.add(BigInteger.valueOf(1));
            QUERY = "INSERT INTO post VALUES (" + postid + ", " + anonymous + ", '" + create_at + "', '" + detail + "', " + has_verify + ", '" + topic + "', '" + update_at + "', " + post_status_id + ", " + owner_id + ", " + like_count + ", "  + status_id + ");";
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
        String QUERY = "SELECT Topic, Detail , TimeStamp, LikeCount FROM Posts WHERE postId = ".concat(id + ";");
        String topic = "";
        String detail = "";
        String timeStamp = "";
        String likeCount = "";
        try{
            ResultSet rs = stmt.executeQuery(QUERY);
            while(rs.next()){
                topic = rs.getString("Topic");
                detail = rs.getString("Detail");
                timeStamp = rs.getString("TimeStamp");
                likeCount = rs.getString("LikeCount");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "200";
        }

        String sendBack = "{" +
                "\"Topic\": \"" + topic + "\"," +
                "\"Detail\": \"" + detail + "\"," +
                "\"TimeStamp\": \"" + timeStamp + "\"," +
                "\"LikeCount\": \"" + likeCount + "\"" +
                "}";

        return sendBack;
    }

    @GetMapping("/pages")
    @CrossOrigin(origins = "http://localhost:3000")
    public String getPage(@RequestParam("page") int id){
        String QUERY = "SELECT PostID, PostOwner, Topic, Detail , TimeStamp, LikeCount, hasVerify FROM Posts ORDER BY TimeStamp DESC LIMIT 10;";
        String postID = "";
        String postOwner = "";
        String topic = "";
        String detail = "";
        String timeStamp = "";
        String likeCount = "";
        String hasVerify = "";
        String crafter = "";
        StringBuilder sendBack = new StringBuilder("[");
        try{
            ResultSet rs = stmt.executeQuery(QUERY);
            while(rs.next()){
                postID = rs.getString("PostID");
                postOwner = rs.getString("PostOwner");
                topic = rs.getString("Topic");
                detail = rs.getString("Detail");
                timeStamp = rs.getString("TimeStamp");
                likeCount = rs.getString("LikeCount");
                hasVerify = rs.getString("hasVerify");

                crafter = "{" +
                        "\"postID\": \"" + postID + "\"," +
                        "\"postOwner\": \"" + postOwner + "\"," +
                        "\"Topic\": \"" + topic + "\"," +
                        "\"Detail\": \"" + detail + "\"," +
                        "\"TimeStamp\": \"" + timeStamp + "\"," +
                        "\"LikeCount\": \"" + likeCount + "\"," +
                        "\"taglist\": \"" + "[]" + "\"," +
                        "\"hasVerify\": \"" + hasVerify + "\"" +
                        "}";

                sendBack.append(crafter).append(",");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "200";
        }

        sendBack.deleteCharAt(sendBack.length() - 1);
        sendBack.append("]");

        return sendBack.toString();
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
        String update_at = create_at;
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

            QUERY = "INSERT INTO comment VALUES (" + commentid + ", " + anonymous + ", '" + create_at + "', '" + detail + "', " + is_verify + ", '" + topic + "', '" + update_at + "', " + post_id + ", " + post_status_id + ", " + owner_id + ", " + like_count + ");";
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