package cc.doctor.framework.web.sample;

import cc.doctor.framework.web.handler.in.ListSplit;
import cc.doctor.framework.web.handler.parser.*;
import cc.doctor.framework.web.handler.resolver.json.JsonBody;
import cc.doctor.framework.web.handler.resolver.modelview.ModelView;
import cc.doctor.framework.web.route.RequestMapping;
import cc.doctor.framework.web.servlet.meta.HttpMetadata;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by doctor on 2017/7/23.
 * todo static files, view path
 * todo memory loads
 * todo caches
 */
@RequestMapping
public class SampleController {
    public Object sampleData() {
        Map<Object, Object> map = new HashMap<>();
        map.put("a", 10);
        map.put("b", "100");
        return map;
    }
    @JsonBody
    @RequestMapping("/json")
    public Object sampleJsonBody(@Form FormParam form, @Param("id")Long id) {
        return form;
    }

    @ModelView(resolver = "vm", view = "sample")
    @RequestMapping("/vm")
    public Object sampleModelView(@Form FormParam formParam) {
        return formParam;
    }

    @ModelView(resolver = "freemarker", view = "sample")
    @RequestMapping("/ftl")
    public Object sampleJsp(UnpackParam unpackParam) {
        return sampleData();
    }

    @ModelView(resolver = "excel", view = "sample")
    @RequestMapping("/ex")
    public Object sampleExcel() {
        List list = new LinkedList();
        for (int i = 0; i < 10; i++) {
            list.add(sampleData());
        }
        return list;
    }

    @RequestMapping("/json2")
    @ModelView(resolver = "freemarker", view = "sample")
    public Object jsonParam(@JsonParam JsonP jsonP) {
        return jsonP;
    }

    public static class UnpackParam extends Unpack {
        private String aa;
        private String b;
        private String c;

        public String getAa() {
            return aa;
        }

        public void setAa(String aa) {
            this.aa = aa;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        @Override
        public void beforeUnpack(HttpMetadata httpMetadata) {
            aa = httpMetadata.getParam("a");
        }

        @Override
        public void afterUnpack(HttpMetadata httpMetadata) {
            System.out.println(aa);
            System.out.println(b);
            System.out.println(c);
        }
    }

    public static class JsonP {
        private String a;
        private Integer b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public Integer getB() {
            return b;
        }

        public void setB(Integer b) {
            this.b = b;
        }
    }

    public static class FormParam {
        private Integer a;
        @Param(value = "bb")
        private Long b;
        @Header
        private Double c;
        @Cookie
        private Float d;
        private String e;
        private Boolean f;
        @ListSplit
        private List<Integer> es;

        public Integer getA() {
            return a;
        }

        public void setA(Integer a) {
            this.a = a;
        }

        public Long getB() {
            return b;
        }

        public void setB(Long b) {
            this.b = b;
        }

        public Double getC() {
            return c;
        }

        public void setC(Double c) {
            this.c = c;
        }

        public Float getD() {
            return d;
        }

        public void setD(Float d) {
            this.d = d;
        }

        public String getE() {
            return e;
        }

        public void setE(String e) {
            this.e = e;
        }

        public Boolean getF() {
            return f;
        }

        public void setF(Boolean f) {
            this.f = f;
        }

        public List<Integer> getEs() {
            return es;
        }

        public void setEs(List<Integer> es) {
            this.es = es;
        }

        @Override
        public String toString() {
            return "FormParam{" +
                    "a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    ", d=" + d +
                    ", e='" + e + '\'' +
                    ", f=" + f +
                    '}';
        }
    }
}
