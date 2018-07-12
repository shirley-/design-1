package com.zym.Design1.mydao;

import com.zym.Design1.bean.vo.EncashVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by YM on 2018/5/3.
 */
public interface AdminEncashMapper {
    List<EncashVO> getApprovedListForOp(@Param("searchDate") String searchDate) ;
}
