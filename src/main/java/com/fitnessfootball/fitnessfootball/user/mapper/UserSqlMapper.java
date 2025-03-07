package com.fitnessfootball.fitnessfootball.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

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

@Mapper
public interface UserSqlMapper {

    public void createUser(UserDto userDto);

    public ManagerDto managerIdAndPassword(ManagerDto managerDto);

    public void createUserInterest(UserInterestDto userInterestdto);

    public List<InterestTagDto> findInterestTagAll();

    public void createMagager(ManagerDto managerDto);

    public void createProduct(ProductDto productDto);

    public void createProductImage(ProductImageDto productImageDto);

    public List<ProductImageDto> findImageByProductId(int product_id);
    
    public UserDto userIdAndPassword(UserDto userDto);

    public List<ProductDto> selectProduct();

    public ProductDto selectProductById(int id);

    public ProductImageDto selectProductImageById(int product_id);

    public String selectProductdescription(int id);

    //회원 존재 유무
    public int countUserByUserId(String user_id);

    //장바구니 
    public void createCart(CartDto cartDto);
    public void deleteCart(CartDto cartDto);
    public int getTotalCart(int product_id);
    public int getMyCart(CartDto cartDto);
    public int myCartCount(int user_id);
    public double productPrice(int id);
    // public void deleteCart(int product_id,);



    public void createBoard(BoardDto boardDto);

    public List<BoardDto> selectBoard();

    public UserDto selectUserId(int id);

    public void readCountUp(int id);

    public BoardDto selectArticleId(int id);

    //추천
    public void creategood(GoodDto goodDto);
    public void deletegood(GoodDto goodDto);
    public int getTotalgood(int article_id);
    public int getMygood(GoodDto goodDto);

    public void boardDelete(int id);

    public List<CartDto> cartList();

    public void insertAddress(AddressDto addressDto);

    //주소 불러오기
    public List<AddressDto> selectAddress ();



}
