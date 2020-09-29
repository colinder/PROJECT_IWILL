package com.project.controller.will;

import java.rmi.server.UID;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import com.project.dao.will.WillDAO;
import com.project.dao.will.WilllogDAO;
import com.project.model.will.WillEntity;
import com.project.model.will.WillLogEntity;
import com.project.model.will.dto.WillCreateDTO;
import com.project.service.will.FileService;
import com.project.service.will.WillLogService;
import com.project.service.will.WillService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@CrossOrigin(origins = { "*" },maxAge = 6000)

@RestController
public class WillController {
    
    @Autowired
    WillService willservice;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    FileService fileservice;

    @Autowired
    WillLogService willlogservice;
    

    @PostMapping("/will/register")
    @ApiOperation(value = "유언장 작성", notes = "유언장 작성 기능입니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiver", value = "받을사람", required = true, dataType = "string"),
            @ApiImplicitParam(name = "content", value = "유언장 내용", required = true, dataType = "string"),
            @ApiImplicitParam(name = "receiver", value = "받을사람", required = true, dataType = "string"),
            @ApiImplicitParam(name = "receiver", value = "받을사람", required = true, dataType = "string"),
            @ApiImplicitParam(name = "receiver", value = "받을사람", required = true, dataType = "string"),
    })
    public ResponseEntity<Object> register(WillCreateDTO willdto) throws Exception{

        final String UID = willdto.getUid();
        final String TITLE = willdto.getTitle();
        final String TRANSKEY; // json send transaction key
        final String TEXTPATH; // text file path
        final String VIDEOPATH; //  video file path
        final String IMAGEPATH; // image file path
        final String DATE;

        Date date = new Date();
        SimpleDateFormat sdp = new SimpleDateFormat("YYYY.MM.dd-HH.mm");
        DATE = sdp.format(date);


        HashMap<String,String> videohm = new HashMap<>();
        HashMap<String,String> imagehm = new HashMap<>();
        HashMap<String,String> texthm = new HashMap<>();

        String userKey = "0x8f600e28D0694F06A28C2edD74F2f3Bb9e865EcC";


        if(!willdto.getVideo().isEmpty()){
            videohm =  fileservice.upload(willdto.getVideo(),UID,DATE);
            
        }
        if(!willdto.getImage().isEmpty()){
            imagehm = fileservice.upload(willdto.getImage(),UID,DATE);
            
        }
        if(!(willdto.getContent().equals(""))){
            texthm = fileservice.uploadtxt(willdto.getContent(),UID,DATE);
        }
        

        VIDEOPATH = videohm.get("path");
        IMAGEPATH = imagehm.get("path");
        TEXTPATH = texthm.get("path");

        TRANSKEY = willservice.sendTransaction(imagehm.get("hash"), videohm.get("hash"), texthm.get("hash"),userKey);

        JSONObject filepath = new JSONObject();
        filepath.put("textpath", TEXTPATH);
        filepath.put("videopath", VIDEOPATH);
        filepath.put("imagepath", IMAGEPATH);

        WillEntity will = new WillEntity();
        will.setUid(UID);
        will.setTitle(TITLE);
        will.setTransactionhash(TRANSKEY);
        will.setFilepath(filepath.toString());
        will.setCreatedate(DATE);
        will.setReceive(willdto.getReceive());
        will.setSenddate(willdto.getSenddate());
        will.setWitness(willdto.getWitness());

        willservice.register(will);

        
        //방금 저장한 will 가져와서 넣기
        willlogservice.register(willservice.findTopWill());

        // willservice.findTopWidii();


        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/will/update")
    @ApiOperation(value = "유언장 수정", notes = "유언장 수정 기능입니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiver", value = "받을사람", required = true, dataType = "string"),
            @ApiImplicitParam(name = "content", value = "유언장 내용", required = true, dataType = "string"),
            @ApiImplicitParam(name = "receiver", value = "받을사람", required = true, dataType = "string"),
            @ApiImplicitParam(name = "receiver", value = "받을사람", required = true, dataType = "string"),
            @ApiImplicitParam(name = "receiver", value = "받을사람", required = true, dataType = "string"),
    })
    public ResponseEntity<Object> willupdate(WillCreateDTO willdto) throws Exception{


       

        final String UID = willdto.getUid();
        final String TITLE = willdto.getTitle();
        final String TRANSKEY; // json send transaction key
        final String TEXTPATH; // text file path
        final String VIDEOPATH; //  video file path
        final String IMAGEPATH; // image file path
        final String DATE;

        Date date = new Date();
        SimpleDateFormat sdp = new SimpleDateFormat("YYYY.MM.dd-HH.mm");
        DATE = sdp.format(date);


        HashMap<String,String> videohm = new HashMap<>();
        HashMap<String,String> imagehm = new HashMap<>();
        HashMap<String,String> texthm = new HashMap<>();

        String userKey = "0x8f600e28D0694F06A28C2edD74F2f3Bb9e865EcC";


        if(!willdto.getVideo().isEmpty()){
            videohm =  fileservice.upload(willdto.getVideo(),UID,DATE);
            
        }
        if(!willdto.getImage().isEmpty()){
            imagehm = fileservice.upload(willdto.getImage(),UID,DATE);
            
        }
        if(!(willdto.getContent().equals(""))){
            texthm = fileservice.uploadtxt(willdto.getContent(),UID,DATE);
        }
        

        VIDEOPATH = videohm.get("path");
        IMAGEPATH = imagehm.get("path");
        TEXTPATH = texthm.get("path");

        TRANSKEY = willservice.sendTransaction(imagehm.get("hash"), videohm.get("hash"), texthm.get("hash"),userKey);

        JSONObject filepath = new JSONObject();
        filepath.put("textpath", TEXTPATH);
        filepath.put("videopath", VIDEOPATH);
        filepath.put("imagepath", IMAGEPATH);

        WillEntity will = new WillEntity();
        will.setWid(willdto.getWid());
        will.setUid(UID);
        will.setTitle(TITLE);
        will.setTransactionhash(TRANSKEY);
        will.setFilepath(filepath.toString());
        will.setCreatedate(DATE);
        will.setReceive(willdto.getReceive());
        will.setSenddate(willdto.getSenddate());
        will.setWitness(willdto.getWitness());

        willservice.register(will);

        
        //방금 저장한 will 가져와서 넣기
        willlogservice.register(willservice.findTopWill());

        // willservice.findTopWidii();


        return new ResponseEntity<>( "success", HttpStatus.OK);
    }

   
    @GetMapping("/will/all")
    public Object getAllWills(){
        List<WillEntity> wills = willservice.findAll();
        return new ResponseEntity<List<WillEntity>>(wills, HttpStatus.OK);
    }
    
     //Read wills by id 
     @GetMapping(value = "/will/user/{userId}")
     public Object getReviewByUserId(@PathVariable String userId) throws Exception{


        List<WillEntity> wills = willservice.getWillByUid(userId);


        JSONArray result = new JSONArray();

        for(WillEntity will : wills){
            System.out.println("==========================");
            System.out.println(will);
            JSONObject pathobj = new JSONObject(will.getFilepath());
        
            String transkey = will.getTransactionhash();
            System.out.println("key : " + transkey);
            JSONObject hashobj =  willservice.getFilehashByTranskey(transkey);

            boolean flag = willservice.compareTosha256(pathobj, hashobj);
            JSONArray jsonarr = new JSONArray();
            jsonarr.put(will);
            jsonarr.put(pathobj);
            
            System.out.println(flag);
            System.out.println(jsonarr.toString());
            
            if(flag){
                result.put(jsonarr);
            }
            System.out.println("==========================");
        }
        System.out.println(result.toString());
        
        return new ResponseEntity<>(result.toList(), HttpStatus.OK);

     }
     

    //  @GetMapping(value = "/will/top")
    //  public Object getRecentReviewByUserId(){
    //     WillEntity will = willservice.findTopWill();
    //      return new ResponseEntity<Object>(will, HttpStatus.OK);
    //  }

    


}
