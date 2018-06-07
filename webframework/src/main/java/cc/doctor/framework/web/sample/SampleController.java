package cc.doctor.framework.web.sample;

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
        System.out.println(id);
        return form;
    }

    @ModelView(resolver = "vm", view = "sample.vm")
    @RequestMapping("/vm")
    public Object sampleModelView(UnpackParam unpackParam) {
        System.out.println(unpackParam);
        return sampleData();
    }

    @ModelView(resolver = "jsp", view = "sample.jsp")
    @RequestMapping("/jsp")
    public Object sampleJsp() {
        return sampleData();
    }

    @ModelView(resolver = "excel", view = "sample.xls")
    @RequestMapping("/ex")
    public Object sampleExcel() {
        List list = new LinkedList();
        for (int i = 0; i < 10; i++) {
            list.add(sampleData());
        }
        return list;
    }

    public static class UnpackParam extends Unpack {
        private String aa;
        @Header("h")
        private String header;
        @Cookie("c")
        private String cookie;
        @Override
        public void beforeUnpack(HttpMetadata httpMetadata) {
            aa = httpMetadata.getParam("a");
        }

        @Override
        public void afterUnpack(HttpMetadata httpMetadata) {

        }

        @Override
        public String toString() {
            return "UnpackParam{" +
                    "aa='" + aa + '\'' +
                    "} " + super.toString();
        }
    }

    public static class FormParam {
        private Integer a;
        private Long b;
        private Double c;
        private Float d;
        private String e;
        private Boolean f;

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
