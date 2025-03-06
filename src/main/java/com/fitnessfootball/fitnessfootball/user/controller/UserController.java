package com.fitnessfootball.fitnessfootball.user.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fitnessfootball.fitnessfootball.dto.BoardDto;
import com.fitnessfootball.fitnessfootball.dto.ManagerDto;
import com.fitnessfootball.fitnessfootball.dto.ProductDto;
import com.fitnessfootball.fitnessfootball.dto.ProductImageDto;
import com.fitnessfootball.fitnessfootball.dto.UserDto;
import com.fitnessfootball.fitnessfootball.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("fitnessfootball")
public class UserController {

    @Autowired
    private UserService userService;

    //관리자 페이지
    @RequestMapping("/admin/loginPage")
    public String staffLoginPage(){

        return "/user/managementPage";
    }

    @RequestMapping("/admin/managerRegisterPage")
    public String staffRegisterPage(){

        return "/user/managerRegisterPage";
    }

    @RequestMapping("/admin/registerProcess")
    public String managerRegisterProcess(ManagerDto managerDto){

        userService.registerManager(managerDto);

        return "/user/productAddPage";

    }

    @RequestMapping("/admin/productAddPage")
    public String productAddPage(HttpSession session, Model model){

        ManagerDto manager = (ManagerDto) session.getAttribute("httpUser");
        
        model.addAttribute("httpUser", manager);
        
        return "/user/productAddPage";

    }

    @RequestMapping("/admin/productProcess")
    public String productAddProcess(HttpSession session, ProductDto productDto,@RequestParam("uploadFile") MultipartFile uMultipartFile, @RequestParam("uploadFiles") MultipartFile[] uploadFiles){

        String rootPath = "C:/uploadFiles/";
            
            //날짜별 폴더(디렉토리) 생성
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
            String todayPath = sdf.format(new Date());
            
            File todayFolderForCreate = new File(rootPath + todayPath);
            
            //이 파일이 존재하지않으면 폴더를 다 만들어라 라는 코드
            if(!todayFolderForCreate.exists()){
                boolean dirsCreated = todayFolderForCreate.mkdirs();
                
                if(dirsCreated){
                    System.out.println("폴더가 생성되었습니다.");
                }else{
                    System.out.println("폴더 생성 실패");
                }
            }
            String originalFileName = uMultipartFile.getOriginalFilename();
            
            String uuid = UUID.randomUUID().toString(); // 객체를 문자열로 표현하는 메서드
            long currentTime = System.currentTimeMillis();
            
            String fileName = uuid + "_" + currentTime;
            
            //확장자명 추출
            String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
            fileName += ext;
            
            try{
                //업로드된 파일을 지정된 경로(rootPath + todayPath + fileName)에 저장
                uMultipartFile.transferTo(new File(rootPath + todayPath + fileName));
            }catch(Exception e){
                e.printStackTrace();
            }
            
            // DB작업용 Dto 생성
            
            // productDto2.setOriginal_filename(originalFileName);
            productDto.setImage_link(todayPath + fileName);


        List<ProductImageDto> productImageDtoList = new ArrayList<>();

        //파일 처리 구간
        for(MultipartFile uploadFile : uploadFiles){
            if(uploadFile.isEmpty()){
                continue;
            }
            
            //파일명 충돌 회피 - 랜덤 + 시간 조합
            originalFileName = uploadFile.getOriginalFilename();
            
            uuid = UUID.randomUUID().toString(); // 객체를 문자열로 표현하는 메서드
            currentTime = System.currentTimeMillis();
            
            fileName = uuid + "_" + currentTime;
            
            //확장자명 추출
            ext = originalFileName.substring(originalFileName.lastIndexOf("."));
            fileName += ext;
            
            try{
                //업로드된 파일을 지정된 경로(rootPath + todayPath + fileName)에 저장
                uploadFile.transferTo(new File(rootPath + todayPath + fileName));
            }catch(Exception e){
                e.printStackTrace();
            }
            
            // DB작업용 Dto 생성
            ProductImageDto productImageDto = new ProductImageDto();
            productImageDto.setOriginal_filename(originalFileName);
            productImageDto.setLocation(todayPath + fileName);
            productImageDtoList.add(productImageDto);
        }
            
        ManagerDto managerDto = (ManagerDto)session.getAttribute("manager");
        int managerPk = managerDto.getId();
        
        productDto.setManager_id(managerPk);

        userService.registerProduct(productDto,productImageDtoList);

        return "redirect:/fitnessfootball/mainPage";
    }

    @RequestMapping("/admin/managerLoginPage")
    public String managerLoginPage(){

        return "/user/managerLoginPage";
    }

    //한번에 관리자면 관리자로, 유저면 유저로 가게끔 처리.
    @RequestMapping("/admin/loginProcess")
    public String managerLoginProcess(HttpSession session , @RequestParam("id") String id, @RequestParam("password") String password){

        ManagerDto managerDto =  new ManagerDto();
        UserDto userDto = new UserDto();

        managerDto.setManager_id(id);
        managerDto.setPassword(password);

        userDto.setUser_id(id);
        userDto.setPassword(password);

        ManagerDto manager = userService.managerIdAndPassword(managerDto);

        if (manager != null) {
            session.setAttribute("manager", manager);
            return "redirect:/fitnessfootball/admin/productAddPage"; // 관리자 페이지
        }

        UserDto user = userService.userIdAndPassword(userDto);
        System.out.println(" >>>>> " + user);


        if (user != null) {
        session.setAttribute("user", user);
        return "redirect:/fitnessfootball/mainPage"; // 쇼핑몰 페이지
        }


          

        // 로그인 실패 페이지
        return "user/loginFailPage";

    }


    //쇼핑몰 사용자 페이지

    @RequestMapping("mainPage")

    public String mainPage(HttpSession session , Model model ){

        UserDto userDto = (UserDto) session.getAttribute("user");
        
        model.addAttribute("user", userDto);

       
        if(userDto == null){

            return "/user/loginPage";
            
        }

        model.addAttribute("product", userService.selectProduct(userDto));  

        model.addAttribute("boardDto", userService.selectBoard());

        return "user/mainPage";
    }

    @RequestMapping("registerPage")
    public String registerPage(Model model){

        model.addAttribute("interestList", userService.getInterestList());
        
        return "/user/registerPage";
    }

    @RequestMapping("registerProcess")
    public String registerProcess(UserDto userDto, @RequestParam(value =  "InterestTag" , required = false) List<Integer> InterestTagIdList){
        System.out.println(userDto);
        userService.registerUser(userDto, InterestTagIdList);

        return "redirect:/fitnessfootball/mainPage";

    }

    @RequestMapping("loginPage")
    public String loginPage(){

        return "/user/loginPage";
    }

    @RequestMapping("logOutProcess")
    public String logOutProcess(HttpSession session){

        session.invalidate();

        return "redirect:/fitnessfootball/loginPage";

    }

    @RequestMapping("shopDetailPage")
    public String shopDetailPage(Model model , @RequestParam("id") int id ,HttpSession session){

        UserDto userDto = (UserDto) session.getAttribute("user");

        String productdescription = userService.selectProductdescription(id);

        productdescription = productdescription.replace("\n", "<br>");

        model.addAttribute("userDto", userDto);
        model.addAttribute("productDto", userService.selectProductById(id));
        model.addAttribute("productImage", userService.selectProductImageById(id));
        model.addAttribute("productdescription", productdescription);
        model.addAttribute("cartCount", userService.myCartCount(userDto.getId()));

        return "/user/shopDetailPage";
    }

    @RequestMapping("shopPage")
    public String shopPage(Model model){

        UserDto userDto = new UserDto();
        
        model.addAttribute("product", userService.selectProduct(userDto));

        return "/user/shopPage";
        
    }


    @RequestMapping("orderPage")
    public String orderPage(@RequestParam(value = "id[]") List<Integer> productIds, @RequestParam(name = "count[]") List<Integer> count, @RequestParam(name = "size", required = false) String size, @RequestParam(name = "color", required = false) String color, Model model, HttpSession session){
        System.out.println("받은 수량 리스트: " + count); // 디버깅용 로그

        UserDto userDto = (UserDto) session.getAttribute("user");
        List<Map<String,Object>> list = new ArrayList<>();
        for(int i=0; i< productIds.size(); i++){
            Map<String,Object> map = new HashMap<>();
            System.out.println(productIds.get(i)+"수량"+ count.get(i));
            ProductDto productDto = userService.selectProductById(productIds.get(i));
            ProductImageDto productImage = userService.selectProductImageById(productIds.get(i));
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+count);
            map.put("productDto", productDto);
            map.put("productImage", productImage);
            int totalPrice = productDto.getPrice()*count.get(i);
            map.put("totalPrice",totalPrice);
            map.put("size", size);
            map.put("color", color);
            map.put("count", count);
            list.add(map);
        }
        model.addAttribute("list", list);
        model.addAttribute("userDto", userDto);


        return "/user/orderPage";
        
    }
    // @RequestMapping("addressProcess")
    // public String addressProcess(AddressDto addressDto){

    //     userService.insertAddress(addressDto);

    //     return "redirect:/fitnessfootball/orderPage";
        
    // }
    






    //게시판 페이지

    @RequestMapping("boardPage")
    public String boardPage(HttpSession session ,Model model){

        UserDto userDto = (UserDto) session.getAttribute("user");

        model.addAttribute("boardDto", userService.selectBoard());

        return "/user/boardPage";
    }

    @RequestMapping("boardWritePage")
    public String boardWritePage(){

        return "/user/boardWritePage";
    }

    @RequestMapping("boardProcess")
    public String boardProcess(HttpSession session , BoardDto boardDto){

        UserDto userDto = (UserDto) session.getAttribute("user");
        System.out.println(">>>>>>" + userDto);

        // if (userDto == null) {
        //     return "redirect:/user/loginPage";  // 로그인 페이지로 리다이렉트
        // }

        boardDto.setUser_id(userDto.getId());

        userService.createBoard(boardDto);

        return "redirect:/fitnessfootball/boardPage";
    }

    @RequestMapping("boardDetailPage")
    public String boardDetailPage(@RequestParam("id") int id ,Model model, HttpSession session){

        UserDto userDto = (UserDto) session.getAttribute("user");

        userService.readCountUp(id);

        model.addAttribute("board", userService.selectArticleId(id));
        model.addAttribute("userDto", userDto);


        return "/user/boardDetailPage";
    }

    @RequestMapping("deleteProcess")
    public String deleteProcess(@RequestParam("id") int id){

        userService.boardDelete(id);

        return "redirect:/fitnessfootball/boardPage";
    }

    @RequestMapping("cartPage")
    public String cartPage(Model model, HttpSession session){

        UserDto userDto = (UserDto) session.getAttribute("user");

        model.addAttribute("cartList", userService.cartList(userDto.getId()));

        return "/user/cartPage";
    }


}
