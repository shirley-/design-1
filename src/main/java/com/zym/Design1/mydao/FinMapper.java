package com.zym.Design1.mydao;

import com.zym.Design1.bean.vo.FinVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created on 2018/4/4.
 */
//@Mapper
public interface FinMapper {

//    @Cacheable(value = "FinMapper")
    List<FinVO> getFinDetailList(@Param("userId") Integer userId, @Param("searchUserUid") String searchUserUid);
}
