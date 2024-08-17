package edu.montana.csci.csci440.controller;

import edu.montana.csci.csci440.util.Web;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;

public class BaseController {


    protected static Integer asInt(String paramVal) {
        return asInt(paramVal, null);
    }

    protected static Integer asInt(String paramVal, Integer defaultVal) {
        if (paramVal == null) {
            return defaultVal;
        } else {
            return Integer.parseInt(paramVal);
        }
    }

    public static String renderTemplate(String index, Object... args) {
        HashMap<Object, Object> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (++i < args.length) {
                map.put(arg, args[i]);
            }
        }
        map.put("message", Web.getMessage());
        map.put("error", Web.getError());
        map.put("web", Web.INSTANCE);
        return new VelocityTemplateEngine().render(new ModelAndView(map, index));
    }


}
