package com.zym.Design1.service;

import com.github.pagehelper.PageHelper;
import com.zym.Design1.bean.vo.FinVO;
import com.zym.Design1.mydao.FinMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created on 2018/4/4.
 */
@Service
@Slf4j
@Transactional
//@CacheConfig(cacheNames = "FinService")
public class FinService {

    @Autowired
    private FinMapper finMapper;

//    @Cacheable( keyGenerator = "keyGenerator")
    public List<FinVO> getFinDetail(Integer id, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        return finMapper.getFinDetailList(id, null);
    }

    public List<FinVO> getFinDetailAll( Integer pageIndex, Integer pageSize, String searchUserUid) {
        if(searchUserUid!=null && !searchUserUid.equals("")) {
            searchUserUid = searchUserUid + "%";
        }
        PageHelper.startPage(pageIndex, pageSize);
        return finMapper.getFinDetailList(null, searchUserUid);
    }
}
