package com.fitnessfootball.fitnessfootball.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.stereotype.Service;

import com.fitnessfootball.fitnessfootball.dto.AddressDto;
import com.fitnessfootball.fitnessfootball.dto.BoardDto;
import com.fitnessfootball.fitnessfootball.dto.CartDto;
import com.fitnessfootball.fitnessfootball.dto.GoodDto;
import com.fitnessfootball.fitnessfootball.dto.InterestTagDto;
import com.fitnessfootball.fitnessfootball.dto.ManagerDto;
import com.fitnessfootball.fitnessfootball.dto.ProductDto;
import com.fitnessfootball.fitnessfootball.dto.ProductImageDto;
import com.fitnessfootball.fitnessfootball.dto.UserDto;
import com.fitnessfootball.fitnessfootball.dto.UserInterestDto;
import com.fitnessfootball.fitnessfootball.user.mapper.UserSqlMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private UserSqlMapper userSqlMapper;

    public void registerUser(UserDto userDto, List<Integer>InterestTagIdList){
        userSqlMapper.createUser(userDto);
        int lastUserPk =  userDto.getId();

        for(int interestId : InterestTagIdList){
            UserInterestDto userInterestDto = new UserInterestDto();
            userInterestDto.setUser_id(lastUserPk);
            userInterestDto.setTag_id(interestId);
            userSqlMapper.createUserInterest(userInterestDto);
        }
    }

    public ManagerDto managerIdAndPassword(ManagerDto managerDto){

        return userSqlMapper.managerIdAndPassword(managerDto);

    }

    public List<InterestTagDto> getInterestList(){

        return userSqlMapper.findInterestTagAll();
        
    }

    public void registerManager(ManagerDto managerDto){

        userSqlMapper.createMagager(managerDto);
        
    }

    public void registerProduct(ProductDto productDto,  List<ProductImageDto> productImageDtoList){

        userSqlMapper.createProduct(productDto);

        //1대 N 관계 (이미지가 한 장 이라면 for 문 필요없음)
        for(ProductImageDto productImageDto : productImageDtoList){
            productImageDto.setProduct_id(productDto.getId());
            userSqlMapper.createProductImage(productImageDto);
        }


    }

    public void createProductImage(ProductImageDto productImageDto){

        userSqlMapper.createProductImage(productImageDto);

    }

    public UserDto userIdAndPassword(UserDto userDto){
 
        return userSqlMapper.userIdAndPassword(userDto);
    }

    public List<Map<String,Object>> selectProduct(UserDto userDto){

        List<Map<String,Object>> result= new ArrayList<>();


        List<ProductDto> productList = userSqlMapper.selectProduct();
        // System.out.println(productList);                        
        for(ProductDto productDto : productList){


            CartDto cartDto = new CartDto();
            cartDto.setProduct_id(productDto.getId());
            cartDto.setUser_id(userDto.getId());

            Map<String, Object> map = new HashMap<>();
            map.put("productDto", productDto);
            map.put("boolean", userSqlMapper.getMyCart(cartDto));
            // System.out.println(userSqlMapper.getMyCart(cartDto));
            result.add(map);

        }

        return result;

    }

    public ProductDto selectProductById(int id){

        return userSqlMapper.selectProductById(id);

    }

    public ProductImageDto selectProductImageById(int product_id){

        return userSqlMapper.selectProductImageById(product_id);

    }

    public String selectProductdescription(int id){

        return userSqlMapper.selectProductdescription(id);

    }

    public boolean isExistUserById(String user_id){

        return userSqlMapper.countUserByUserId(user_id) > 0;
    }

    //장바구니

    public void cart(CartDto cartDto){
        userSqlMapper.createCart(cartDto);
    }

    public void unCart(CartDto cartDto){
        userSqlMapper.deleteCart(cartDto);
    }

    public int getTotalCart(int product_id){
        return userSqlMapper.getTotalCart(product_id);
    }

    public boolean getUserCart(CartDto cartDto){
        return userSqlMapper.getMyCart(cartDto) > 0;
 
    }

    public int myCartCount(int user_id){
        return userSqlMapper.myCartCount(user_id);
    }

      public void createBoard(BoardDto boardDto){
        userSqlMapper.createBoard(boardDto);
    }

    public List<Map<String,Object>> selectBoard(){

        List<Map<String,Object>> result= new ArrayList<>();

        List<BoardDto> boardList = userSqlMapper.selectBoard();
        // System.out.println(productList);                        
        for(BoardDto boardDto : boardList){

            UserDto userDto  = userSqlMapper.selectUserId(boardDto.getUser_id());

            Map<String, Object> map = new HashMap<>();
            map.put("boardDto", boardDto);
            map.put("userDto", userDto);

            result.add(map);

        }

        return result;

    }

    public void readCountUp(int id){

        userSqlMapper.readCountUp(id);

    }

    public void insertAddress(AddressDto addressDto){

        userSqlMapper.insertAddress(addressDto);

    }

    public Map<String, Object> selectArticleId(int id){

        Map<String, Object> result = new HashMap<>();

        BoardDto boardDto = userSqlMapper.selectArticleId(id);

        UserDto userDto  = userSqlMapper.selectUserId(boardDto.getUser_id());

        result.put("boardDto", boardDto);
        result.put("userDto", userDto);

        // System.out.println(" >>> boardDto " + boardDto);

        return result;
    }

    // public List<Map<String,Object>> selectAddress(AddressDto addressDto){

    //     Map<String, Object> result = new HashMap<>();
        
    // }

    //추천

    
    public void good(GoodDto goodDto){
        userSqlMapper.creategood(goodDto);
    }

    public void ungood(GoodDto goodDto){
        userSqlMapper.deletegood(goodDto);
    }

    public int getTotalGood(int article_id){
        return userSqlMapper.getTotalgood(article_id);
    }

    public boolean getMygood(GoodDto goodDto){
        return userSqlMapper.getMygood(goodDto) > 0;
    }

    //게시글 삭제

    public void boardDelete(int id){
        userSqlMapper.boardDelete(id);
    }

    public List<Map<String,Object>> cartList(int user_id){

        List<Map<String, Object>> result = new ArrayList<>();

        List<CartDto> cartList = userSqlMapper.cartList();
        
        for(CartDto cartDto : cartList){
            
            Map<String, Object> map = new HashMap<>();

            ProductDto productDto = userSqlMapper.selectProductById(cartDto.getProduct_id());
            ProductImageDto productImageDto = userSqlMapper.selectProductImageById(cartDto.getProduct_id());
            cartDto.setUser_id(user_id);
            System.out.println("cartDto >> " + cartDto);
            System.out.println("cartDto.getQuantity() >> " + cartDto.getQuantity());

            

            map.put("cartDto", cartDto);
            map.put("productDto", productDto);
            map.put("productImageDto", productImageDto);
            System.out.println(" >>> cartDto " + cartDto);

            result.add(map);

            
        }

        return result;
    }

    public double productPrice(int product_id){
        return userSqlMapper.productPrice(product_id);
    }

    // public void deleteCart(int product_id, int user_id){
    //     userSqlMapper.deleteCart(product_id, user_id);
    // }
 

}
