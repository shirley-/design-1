package com.zym.Design1.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zym.Design1.dao.NoticeMapper;
import com.zym.Design1.entity.Notice;
import com.zym.Design1.entity.NoticeExample;
import com.zym.Design1.entity.User;
import com.zym.Design1.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created on 2018/3/30.
 */
@Service
@Slf4j
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    public Notice getNoticeById(Integer id) {
        return noticeMapper.selectByPrimaryKey(id);
    }

    public List<Notice> getNoticeList(Integer pageIndex, Integer pageSize) {
        NoticeExample example = new NoticeExample();
        example.setOrderByClause("created_date desc");
        PageHelper.startPage(pageIndex, pageSize);
        return noticeMapper.selectByExample(example);
    }

    @Transactional
    public Notice addNotice(String title, String content, User user) {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setCreatedDate(new Date());
        notice.setState(ConstantUtil.STATE_VALID);
        notice.setUserId(user.getId());
        notice.setUserUid(user.getUid());
        notice.setVersion(1);
        noticeMapper.insertSelective(notice);
        log.info("notice:{}", JSON.toJSONString(notice));
        return notice;
    }

    @Transactional
    public void update(Integer id, String title, String content) {
        Notice notice = getNoticeById(id);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setModifiedDate(new Date());
        log.info("notice:{}", JSON.toJSONString(notice));
        int num = noticeMapper.updateByPrimaryKeySelective(notice);
        if(num!=1) {
            log.error("updateNotice错误，num:{}, notice:{}", num , JSON.toJSONString(notice));
            throw new RuntimeException("修改通知错误");
        }
    }

    @Transactional
    public void invalidNotice(Integer id, String state) {
        Notice notice = getNoticeById(id);
        notice.setState(state);
        int num = noticeMapper.updateByPrimaryKeySelective(notice);
        if(num!=1) {
            log.error("updateNotice错误，num:{}, notice:{}", num , JSON.toJSONString(notice));
            throw new RuntimeException("失效错误");
        }
    }


}
