package com.rubbypaper.demo;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import com.rubbypaper.demo.CBFilterSimulation.ColorBlindType;

import jakarta.servlet.http.HttpServletRequest;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Controller
public class CBFilterMain {
	
    static String imagePath ;        
    static int colorBlindTypeIndex ; 
    
	@PostMapping("/filter")
	public String Methodnum(HttpServletRequest request)
	{	
		String tes = "";
		int type = 0;
		
		//이미지 파일 값을 받는 곳
		imagePath = tes;   
		
		//적색맹,녹색맹,청색맹 선택한 값 받아 오는 곳
       colorBlindTypeIndex = Integer.parseInt(request.getParameter("Colorblindnesstype")); 
       
       

       // 입력된 색맹 유형에 해당하는 열거형 상수를 얻습니다.
       ColorBlindType colorBlindType = getColorBlindTypeByIndex(colorBlindTypeIndex);

       try {
           // 이미지 파일을 읽어옵니다.
           File imageFile = new File(imagePath);

           // GIF 파일인지 확인
           if (imageFile.getName().toLowerCase().endsWith(".gif")) {
               // GIF 파일 디코딩
               GifDecoder decoder = new GifDecoder();
               decoder.read(new FileInputStream(imageFile));

               List<BufferedImage> frames = new ArrayList<>();

               for (int i = 0; i < decoder.getFrameCount(); i++) {
                   BufferedImage frame = decoder.getFrame(i);
                   BufferedImage simulatedFrame = CBFilterSimulation.simulateColorBlindness(frame, colorBlindType);
                   frames.add(simulatedFrame);
               }

               // 새로운 GIF 이미지 생성
               String outputImagePath = "src/main/resources/webapp/resouces/img/convert" + imageFile.getName();
               createGifFromFrames(frames, outputImagePath);

               System.out.println("GIF 이미지 변환이 완료되었습니다.");
           } else {
               // GIF가 아닌 경우 일반 이미지 변환
               BufferedImage originalImage = ImageIO.read(imageFile);

               // 색맹 시뮬레이션을 적용한 이미지 생성
               BufferedImage simulatedImage = CBFilterSimulation.simulateColorBlindness(originalImage, colorBlindType);

               // 이미지 저장
               String outputImagePath = "src/main/resources/webapp/resources/img/convert" + imageFile.getName();
               ImageIO.write(simulatedImage, "png", new File(outputImagePath));

               System.out.println("이미지 변환이 완료되었습니다.");
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
       
       //* 해결 해야 하는 부분 : 밑에 ColorblindType 부분과 createGifFromFrames 부분을 이 메소드에다가 집어 넣기*
       
       if(1 == colorBlindTypeIndex)
       {
    	   System.out.println("=========값 받아 오기 성공 ====");
    	  return ColorBlindType.PROTANOPIA;
       }
	return colorBlindType + "index";
		
	}
  

    // 숫자로 입력된 색맹 유형을 열거형 상수로 변환
    private static ColorBlindType getColorBlindTypeByIndex(int index) {
        switch (index) {
            case 1:            	
                return ColorBlindType.PROTANOPIA; //적색맹
            case 2:
                return ColorBlindType.DEUTERANOPIA; //녹색맹
            case 3:
                return ColorBlindType.TRITANOPIA; //청색맹
            default:
                return ColorBlindType.PROTANOPIA; // 기본값은 적색맹
        }
    }

    // 디코딩된 프레임들을 이용하여 GIF 이미지 생성
    private static void createGifFromFrames(List<BufferedImage> frames, String outputPath) {
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();

        // 출력 경로 설정
        encoder.start(outputPath);
        encoder.setRepeat(0); // 반복 설정 (0은 무한 반복)

        for (BufferedImage frame : frames) {
            encoder.setDelay(100); // 각 프레임 간의 딜레이 (100ms)
            encoder.addFrame(frame);
        }

        encoder.finish();
    }
    

  
	
}
