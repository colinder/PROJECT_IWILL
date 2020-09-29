package com.project.service.will;

import java.util.List;

import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.dao.will.WillDAO;
import com.project.model.will.WillEntity;
import com.project.util.HexToString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.apache.tomcat.jni.File;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class WillService {

    @Autowired
    WillDAO willDao;

    @Autowired
    JavaMailSender mailSender;

    FileService fs = new FileService();
    
    
    public List<WillEntity> findAll(){
        return willDao.findAll();
    }


    public Object save(WillEntity newWill){ // 새로운 유언장 저장

        return willDao.save(newWill);
    }

    public List<WillEntity> getWillByUid(String uid){
        return willDao.findAllByUid(uid);
    }

    public WillEntity findTopWill(){
        return willDao.findTop();
    }


    public void register(WillEntity will){
        willDao.save(will);
    }

    public String sendTransaction(String image,String video,String text,String userKey) throws Exception{

        JSONObject ob = new JSONObject();
        
        ob.put("image", image);
        ob.put("video", video);
        ob.put("text", text);

        //senddata to hex
		String inputString = "0x"+ob.toString();
        
        JSONObject sendobj = new JSONObject();
        sendobj.put("jsonrpc", "2.0");
        sendobj.put("method", "eth_sendTransaction");
        JSONArray params = new JSONArray();
        JSONObject paramsobj = new JSONObject();
        paramsobj.put("from", userKey);
        paramsobj.put("to", userKey);
        paramsobj.put("data", "0x"+HexToString.stringtohex(inputString));
        params.put(paramsobj);
        sendobj.put("params", params);
		sendobj.put("id", 100);

		String result = fs.sendPost("http://localhost:8545/",sendobj.toString());
		//보낸 결과값
        
        JSONObject sendtransobj = new JSONObject(result);
		
        return sendtransobj.getString("result");
    }

    public JSONObject getFilehashByTranskey(String transkey) throws Exception{
        
        
		JSONObject sendobj = new JSONObject();
        sendobj.put("jsonrpc", "2.0");
        sendobj.put("method", "eth_getTransactionByHash");
		JSONArray params = new JSONArray();
		params.put(transkey);
        sendobj.put("params", params);
        sendobj.put("id", 100);

        String result = fs.sendPost("http://localhost:8545/",sendobj.toString());

        String input = new JSONObject(result).getJSONObject("result").getString("input");
		input = input.substring(2,input.length());
		
		String jsonhash = HexToString.hextostring(input);
		jsonhash = jsonhash.substring(2,jsonhash.length());

        return new JSONObject(jsonhash);
    } 

    public boolean compareTosha256(JSONObject path,JSONObject hash) throws Exception{

        System.out.println(path.toString());
        System.out.println(hash.toString());
        boolean resultflag = false;
        boolean textflag = false;
        boolean viedoflag = false;
        boolean imageflag = false;

        if(!path.isNull("textpath")){
            final String texthash = fs.extractFileHashSHA256(path.getString("textpath"));

            if(texthash.equals(hash.getString("text"))){
                textflag = true;
            }
        }
        else{
            textflag = true;
        }

        if(!path.isNull("imagepath")){
            final String imagehash = fs.extractFileHashSHA256(path.getString("imagepath"));

            if(imagehash.equals(hash.getString("image"))){
                imageflag = true;
            }
        }
        else{
            imageflag = true;
        }

        if(!path.isNull("video")){
            final String videohash = fs.extractFileHashSHA256(path.getString("videopath"));

            if(videohash.equals(hash.getString("video"))){
                viedoflag = true;
            }
        }
        else{
            viedoflag = true;
        }
        

        if(imageflag && viedoflag && textflag){
            resultflag = true;
        }
        

        return resultflag;
    }
    public WillEntity updateWill(int wid){
        return willDao.getWillByWid(wid);
    }

    //예약 날짜에 이메일 보내기
    public void sendEmail(String email){ 
        System.out.println("sendEmail : "+email);

        String setfrom = "admin@gamil.com";
        String tomail = email; // 받는 사람 이메일
        String content =
        
        System.getProperty("line.separator")+ //한줄씩 줄간격을 두기위해 작성
        System.getProperty("line.separator")+
        "안녕하세요 회원님 저희 홈페이지를 찾아주셔서 감사합니다"
        +System.getProperty("line.separator")+
        System.getProperty("line.separator")+
        System.getProperty("line.separator")+
        "받으신 인증번호를 홈페이지에 입력해 주시면 다음으로 넘어갑니다."; // 내용
        
        try {
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,true, "UTF-8");

            messageHelper.setFrom(setfrom); // 보내는사람 생략하면 정상작동을 안함
            messageHelper.setTo(tomail); // 받는사람 이메일
            messageHelper.setSubject("I WILL"); // 메일제목은 생략이 가능하다
            messageHelper.setText(content); // 메일 내용
            mailSender.send(message);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
