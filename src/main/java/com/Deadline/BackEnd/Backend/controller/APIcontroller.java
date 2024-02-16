package com.Deadline.BackEnd.Backend.controller;
import com.Deadline.BackEnd.Backend.Objects.createComment;
import com.Deadline.BackEnd.Backend.Objects.createPost;
import com.Deadline.BackEnd.Backend.Objects.login;
import com.Deadline.BackEnd.Backend.Objects.signin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

@RestController
public class APIcontroller {

    static final String DB_URL = "jdbc:mysql://localhost:3306/albaz";
    static final String USER = "gunxroswar";
    static final String PASS = "Gxz171477940*";
    Connection conn = null;
    Statement stmt = null;

    private String autoPayloadBuilder(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        StringBuilder singleData = new StringBuilder();
        StringBuilder returnData = new StringBuilder();
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

        if (rowCount == 0) return "";

        returnData.deleteCharAt(returnData.length()-1);

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
    public ResponseEntity<String> login(@RequestBody login info){
        if(IsSQLInjection(info.userName)) return ResponseEntity.badRequest().body("Unauthorized");
        String QUERY = "SELECT username, password, profile_name FROM user ".concat("WHERE username='" + info.userName + "';");
        String existUser = "null";
        String existPassword = "null";
        String displayName = "null";
        try{
            ResultSet rs = stmt.executeQuery(QUERY);
            if(!rs.next()) return ResponseEntity.badRequest().body("User name or password incorrect");
            existUser = rs.getString("username");
            existPassword = rs.getString("password");
            if(!Objects.equals(existPassword, info.password)) return ResponseEntity.badRequest().body("User name or password incorrect");
            displayName = rs.getString("displayName");
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("400");
        }

        return new ResponseEntity<>("{\"displayName\": \"" + displayName + "\"}", HttpStatus.OK);
    }

    @PostMapping("/guests/signin")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> signin(@RequestBody signin info){
        String QUERY = "INSERT INTO User VALUES ('".concat(info.userName + "', '" + info.password + "', '" + info.displayName + "');");
        try{
            stmt.executeUpdate(QUERY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("200");
        }
        return new ResponseEntity<>("{\"displayName\": \"" + info.displayName + "\"}", HttpStatus.OK);
    }

//    @GetMapping("/pages")
//    @CrossOrigin(origins = "http://localhost:3000")
//    public ResponseEntity<String> getPage(@RequestParam("page") int id){
//        String sendBack;
//        String QUERY =
//                "SELECT id, user.profile_name , topic, detail , create_at, like_count, has_verify, '[]' as taglist, comment.commentCount\n" +
//                        "FROM ( post INNER JOIN user ON post.ower_id = user.uid )\n" +
//                        "JOIN ( \n" +
//                        "SELECT COUNT(comment.comment_id) as commentCount, post.id as commentOwner\n" +
//                        "FROM post \n" +
//                        "LEFT JOIN comment on post.id = comment.post_id  \n" +
//                        "GROUP BY post.id\n" +
//                        "ORDER BY post.create_at DESC LIMIT 10\n" +
//                        ") as comment ON comment.commentOwner = id\n" +
//                        "ORDER BY create_at DESC LIMIT 10;";
//        try{
//            ResultSet rs = stmt.executeQuery(QUERY);
//            sendBack = autoPayloadBuilder(rs);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("200");
//        }
//
//        return new ResponseEntity<>("[" + sendBack + "]", HttpStatus.OK);
//    }
//
//    @GetMapping("/comments")
//    @CrossOrigin(origins = "http://localhost:3000")
//    public ResponseEntity<String> getComment(@RequestParam("commentId") int post_id){
//        String sendBack;
//        String QUERY =
//                "SELECT comment_id as 'CommentID', user.profile_name as 'displayName', like_count as 'LikeAmount', is_verify as 'hasVerify', 0 as 'replyAmount', create_at as 'CreateDate', detail\n" +
//                "FROM comment\n" +
//                "INNER JOIN user ON user.uid = comment.ower_id\n" +
//                "WHERE comment.post_id = " + post_id + ";";
//
//        try{
//            ResultSet rs = stmt.executeQuery(QUERY);
//            sendBack = autoPayloadBuilder(rs);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("200");
//        }
//
//        return new ResponseEntity<>("[" + sendBack + "]", HttpStatus.OK);
//    }

    @PostMapping("/comments/create")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> createComment(@RequestBody createComment info){
        BigInteger commentid;
        int anonymous = 1;
        String create_at = String.valueOf(new Timestamp(System.currentTimeMillis()));
        String detail = info.detail;
        int is_verify = 0;
        String topic;
        BigInteger post_id = info.PostID;
        BigInteger post_status_id = BigInteger.valueOf(1);
        BigInteger owner_id = BigInteger.valueOf(1);
        BigInteger like_count = BigInteger.valueOf(1);
        String QUERY;
        try{
            QUERY = "SELECT MAX(comment_id) FROM comment;";
            ResultSet rs = stmt.executeQuery(QUERY);
            rs.next();
            commentid = BigInteger.valueOf(rs.getLong("MAX(comment_id)"));
            commentid = commentid.add(BigInteger.valueOf(1));

            QUERY = "SELECT topic FROM post WHERE post.id = " + info.PostID + ";";
            rs = stmt.executeQuery(QUERY);
            if(!rs.next()) return ResponseEntity.badRequest().body("User");
            topic = rs.getString("topic");

            QUERY = "INSERT INTO comment VALUES (" + commentid + ", " + anonymous + ", '" + create_at + "', '" + detail + "', " + is_verify + ", " + like_count + ", '" + topic + "', '" + create_at + "', " + post_id + ", " + post_status_id + ", " + owner_id + ");";
            stmt.executeUpdate(QUERY);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("200");
        }


       return new ResponseEntity<>("OK", HttpStatus.CREATED);
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