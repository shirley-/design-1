package com.zym.Design1.tttest;

import com.zym.Design1.dao.NoticeMapper;
import com.zym.Design1.dao.OrgMapper;
import com.zym.Design1.entity.Notice;
import com.zym.Design1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by YM on 2018/4/16.
 */
//@Service
//@Transactional
public class TestServiceB {
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private TestServiceA testServiceA;

    public void addNotice(Notice notice) {
        notice.setTitle("serviceB add");
        noticeMapper.insertSelective(notice);
        testServiceA.addUser(new User());
        addNotice2();
    }

    public void addNotice2() {
        Notice notice = new Notice();
        notice.setTitle("serviceB add 2");
        noticeMapper.insertSelective(notice);
    }

    public void addNotice3(Notice notice) {
        notice.setTitle("serviceB add3");
        noticeMapper.insertSelective(notice);
        testServiceA.addUser(new User());
    }
}
