package com.zym.Design1.Test2;

/**
 * Created by YM on 2018/6/26.
 */
abstract public class TestParent {
    private String name;
    public abstract String callName1() ;
    public abstract String callName2() ;

    public String getName() {return name;}


}

class Son extends TestParent{

    @Override
    public String callName1() {

        return null;
    }

    @Override
    public String callName2() {
        return null;
    }

    @Override
    public String getName() {
        return super.getName();
    }
}
