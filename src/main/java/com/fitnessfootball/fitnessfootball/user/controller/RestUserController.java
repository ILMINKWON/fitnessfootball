package com.fitnessfootball.fitnessfootball.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fitnessfootball.fitnessfootball.dto.CartDto;
import com.fitnessfootball.fitnessfootball.dto.GoodDto;
import com.fitnessfootball.fitnessfootball.dto.RestResponseDto;
import com.fitnessfootball.fitnessfootball.dto.UserDto;
import com.fitnessfootball.fitnessfootball.user.service.UserService;

import jakarta.servlet.http.HttpSession;

// @Controller
// @ResponseBody //데이터 별칭 , 데이터를 응답하겠다 라는뜻.
@RestController //@Controller + @ResponseBody 
@RequestMapping("api/user")
public class RestUserController {

    @Autowired
    private UserService userService;


    //테스트 코드 / 디폴트 
//     @RequestMapping("test")
//     public RestResponseDto test(){

//         RestResponseDto restResponseDto = new RestResponseDto();

//         restResponseDto.setResult("success");

//         restResponseDto.add("user", new UserDto());// 키 값으로 url에서 출력

//         return restResponseDto;
//     }

    @RequestMapping("existsId")
    public RestResponseDto existsId(@RequestParam("userId") String userId){

        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");

        restResponseDto.add("isExist", userService.isExistUserById(userId));// 키 값으로 url에서 출력

        return restResponseDto;
    }

    //장바구니
    @RequestMapping("cart")
    public RestResponseDto cart(HttpSession session , CartDto cartDto){
        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");

        UserDto userDto = (UserDto) session.getAttribute("user");

        cartDto.setUser_id(userDto.getId());

        userService.cart(cartDto);

        return restResponseDto;


    }
    
    
    @RequestMapping("unCart")
    public RestResponseDto unCart(HttpSession session , CartDto cartDto){
        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");

        
        UserDto userDto = (UserDto) session.getAttribute("user");

        cartDto.setUser_id(userDto.getId());

        userService.unCart(cartDto);


        return restResponseDto;


    }

    @RequestMapping("getTotalCartCount")
    public RestResponseDto getTotalCartCount(@RequestParam("productId") int product_id){
        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");
        
        restResponseDto.add("count", userService.getTotalCart(product_id));

        return restResponseDto;


    }

    @RequestMapping("isUserCart")
    public RestResponseDto isUserCart(CartDto cartDto, HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");

        UserDto userDto = (UserDto) session.getAttribute("user");

        cartDto.setUser_id(userDto.getId());

        restResponseDto.add("isCart", userService.getUserCart(cartDto));



        return restResponseDto;


    }

    @RequestMapping("getSessionId")
    public RestResponseDto getSessionId(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");

        UserDto userDto = (UserDto) session.getAttribute("user");

        if(userDto == null){
            restResponseDto.setResult("fail");
            restResponseDto.setReason("인증되지 않았습니다.");
        }else{
            restResponseDto.add("id",userDto.getId());
        }

        return restResponseDto;

    }

    @RequestMapping("updateCartSidebar")
    public RestResponseDto updateCartSidebar(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");

        UserDto userDto = (UserDto) session.getAttribute("user");

        restResponseDto.add("cartCount", userService.myCartCount(userDto.getId()));



        return restResponseDto;


    }



    //추천 
    @RequestMapping("goodd")
    public RestResponseDto good(HttpSession session ,GoodDto goodDto){
        RestResponseDto responseDto = new RestResponseDto();
        responseDto.setResult("success");

        UserDto userDto = (UserDto)session.getAttribute("user");
        System.out.println(">>>>>>>>>>>>>>>>"+userDto);
        goodDto.setUser_id(userDto.getId());
        

        userService.good(goodDto);

        return responseDto;
    }

    @RequestMapping("getTotalLikeCount")
    public RestResponseDto getTotalLikeCount(@RequestParam("articleId")int articleId){
        RestResponseDto responseDto = new RestResponseDto();
        responseDto.setResult("success");
        System.out.println(userService.getTotalGood(articleId));
        responseDto.add("count", userService.getTotalGood(articleId));

        return responseDto;
    }

    @RequestMapping("ungood")
    public RestResponseDto ungood(HttpSession session ,GoodDto goodDto){
        RestResponseDto responseDto = new RestResponseDto();
        responseDto.setResult("success");

        UserDto userDto = (UserDto)session.getAttribute("user");
        System.out.println(">>>>>>>>>>>>>>>>"+userDto);
        goodDto.setUser_id(userDto.getId());
        

        userService.ungood(goodDto);

        return responseDto;
    }

    @RequestMapping("isGood")
    public RestResponseDto isGood(HttpSession session ,GoodDto goodDto){
        RestResponseDto responseDto = new RestResponseDto();
        responseDto.setResult("success");

        UserDto userDto = (UserDto)session.getAttribute("user");
        System.out.println(">>>>>>>>>>>>>>>>"+userDto);
        goodDto.setUser_id(userDto.getId());
        

        responseDto.add("isGood", userService.getMygood(goodDto));

        return responseDto;
    }

    //별표 즐겨찾기
    
     
    
    
}
