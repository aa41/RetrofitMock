package com.xiaoma.retrofitmock;

/**
 * Created by jcf-mbp on 17-4-21.
 */

public class TestMain {
    public static void main(String[] args){
        Test test = new Test();
        RetrofitMock.setAttrs("code=1","value=2b青年屁事多");
        Test init = RetrofitMock.init(test);
        System.out.println(init.getCode());
        System.out.println(init.getValue());
        System.out.println(init.getWebApi());
        System.out.println(init.isApp());
        System.out.println(init.getI());
        System.out.println(init.getAm());
        System.out.println(init.getNothing());
        System.out.println(init.isUseFull());
        System.out.println(init.getFxxk());
        System.out.println(init.getSuperTest());
        System.out.println(init.isHohoho());


    }
}
