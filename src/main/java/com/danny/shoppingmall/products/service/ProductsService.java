package com.danny.shoppingmall.products.service;

import com.danny.shoppingmall.orders.entity.Orders;
import com.danny.shoppingmall.orders.repository.OrdersRepository;
import com.danny.shoppingmall.products.dto.ProductsDTO;
import com.danny.shoppingmall.products.entity.Products;
import com.danny.shoppingmall.products.repository.ProductsRepository;
import com.danny.shoppingmall.user.dto.ResponseDTO;
import com.danny.shoppingmall.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepository productsRepository;

    public ResponseEntity<?> getProductList() {

        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        Map<String, Object> returnMap = new HashMap<>();
        try {
            List<Products> productList = productsRepository.findAll();
            System.out.println("물건들" + productList);

            List<ProductsDTO> productDTOList = new ArrayList<>();
            for(Products p : productList) {
                productDTOList.add(p.entityToDTO());
            }
            returnMap.put("productList", productDTOList);
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    public ResponseEntity<?> makeProducts(ProductsDTO productsDTO, UserDTO userDTO) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        Map<String, String> returnMap = new HashMap<>();
        try {
            String link = productsDTO.getLink();
            Boolean save = extract(link);
            System.out.println("세이브됐니?" + save);
            if(save == false) {
                returnMap.put("save", "fail");
            } else {
                returnMap.put("save", "ok");
            }
            System.out.println("리턴맵이 왜 이러지?" + returnMap);
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    public Boolean extract(String link) {

        String url = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

        Pattern p = Pattern.compile(url);

        Matcher matcher = p.matcher(link); //위의 url 패턴과 맞는지 검사
        if (matcher.find()) { //패턴과 맞을 시 실행
            int startIndex = matcher.start(); //주소의 첫 번째 인덱스
            int endIndex = matcher.end(); //주소의 마지막 인덱스
            String exportUrl = link.substring(startIndex, endIndex); // 게시글 내용이 들어있는 변수에서 url만 짜르기

            try {
                Document doc = Jsoup.connect(exportUrl).header("User-Agent", "Mozilla/5.0").get();
                //Jsoup은 데이터 크롤링 라이브러리 (라이브러리 필수 설치)
                //url주소를 가져와서 페이지 데이터를 수집
                System.out.println(doc); //데이터 출력해보기
                System.out.println("---");

                //"오픈그래프 태그에 작성된 타이틀, 내용, 이미지 가져와서 문자열에 담기
                String title = doc.select("meta[property=og:title]").attr("content");  // 제목
                String content = doc.select("meta[property=og:description]").attr("content"); // 내용
                String image = doc.select("meta[property=og:image]").attr("content"); // 이미지
                System.out.println("어느게 안됐을까?");
//                int price = Integer.parseInt(doc.select("meta[property=product:price:amount]").attr("content"));
//                System.out.println("가격뜨나?" + price);

                Products products = new Products();
                products.setProductName(title);
//                products.setProductPrice(price);
                products.setLink(link);
                products.setContent(content);
                products.setImage(image);
                productsRepository.save(products);

                System.out.println("링크 저장됐나요?" + products);
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return false;
    }

    public ResponseEntity<?> getProduct(long id) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO();
        Map<String, Object> returnMap = new HashMap<>();
        try {
            Products product = productsRepository.findById(id).get();
            System.out.println("이건 상품 하나" + product);
            ProductsDTO productsDTO = product.entityToDTO();
            returnMap.put("product", productsDTO);
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
