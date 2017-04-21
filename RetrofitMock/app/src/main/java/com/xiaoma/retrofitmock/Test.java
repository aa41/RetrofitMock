package com.xiaoma.retrofitmock;

/**
 * Created by jcf-mbp on 17-4-21.
 */
@Mock({"app=true","webApi=1.20"})
public class Test extends TestSuper{



    private int code;
    private String value;
    private boolean app;
    private double webApi;

    private String i;

    private int am;

    private double nothing;

    private boolean useFull;


    private TestInside inside;


    public static class TestInside{
        private String inside;

        public String getInside() {
            return inside;
        }

        public void setInside(String inside) {
            this.inside = inside;
        }
    }


    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public int getAm() {
        return am;
    }

    public void setAm(int am) {
        this.am = am;
    }

    public double getNothing() {
        return nothing;
    }

    public void setNothing(double nothing) {
        this.nothing = nothing;
    }

    public boolean isUseFull() {
        return useFull;
    }

    public void setUseFull(boolean useFull) {
        this.useFull = useFull;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isApp() {
        return app;
    }

    public void setApp(boolean app) {
        this.app = app;
    }

    public double getWebApi() {
        return webApi;
    }

    public void setWebApi(double webApi) {
        this.webApi = webApi;
    }
}
