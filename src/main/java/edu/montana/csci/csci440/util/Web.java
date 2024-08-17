package edu.montana.csci.csci440.util;

import edu.montana.csci.csci440.controller.BaseController;
import spark.Request;
import spark.Response;
import spark.Session;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static spark.Spark.*;

public class Web {

    public static final int PAGE_SIZE = 10;
    public static final Web INSTANCE = new Web();
    static ThreadLocal<RequestInfo> INFO = new ThreadLocal<>();

    public static void set(Request request, Response response, long startTime) {
        INFO.set(new RequestInfo(request, response, startTime, DB.getConnectionCount()));
    }

    public static Request getRequest(){
        return INFO.get().getRequest();
    }
    public static Response getResponse(){ return INFO.get().getResponse(); }


    public static void showMessage(String s) {
        getRequest().session().attribute(":message", s);
    }

    public static String getMessage() {
        Session session = getRequest().session();
        String message = session.attribute(":message");
        session.removeAttribute(":message");
        return message;
    }

    public static void showErrorMessage(String s) {
        getRequest().session().attribute(":error", s);
    }

    public static String getError() {
        Session session = getRequest().session();
        String message = session.attribute(":error");
        session.removeAttribute(":error");
        return message;
    }

    public static Object redirect(String location) {
        getResponse().redirect(location, 303);
        return "";
    }

    public String pagingWidget(List collection) {
        String div = "<div style='padding-bottom:12px'>";
        String prev = prevPage();
        String next = nextPage(collection);
        if (prev.equals("")) {
            div += next;
        } else {
            div += prev + " &bull; Page " + getCurrentPage() + " &bull; " + next;
        }
        return div + "</div>";
    }

    public String nextPage(List collection){
        String otherParams = getParamsForPaging();
        if (collection.size() == PAGE_SIZE) {
            Integer page = getCurrentPage();
            return "<a href='" + getRequest().pathInfo() + "?page=" + (page + 1) + "&" + otherParams + "'>Next &rarr;</a>";
        } else {
            return "";
        }
    }

    private String getOrderBy() {
        String o = getRequest().queryParams("o");
        if (o != null && !"".equals(o)) {
            return "&o=" + o;
        } else {
            return "";
        }
    }

    public static Integer getCurrentPage(){
        String page = getRequest().queryParams("page");
        if (page != null) {
            return Integer.parseInt(page);
        } else {
            return 1;
        }
    }

    public String prevPage() {
        Integer page = getCurrentPage();
        String otherParams = getParamsForPaging();
        if (page > 2) {
            return "<a href='" + getRequest().pathInfo() + "?page=" + (page - 1) + "&" + otherParams + "'>&larr; Previous</a>";
        } else if (page == 2) {
            return "<a href='" + getRequest().pathInfo() + "?" + otherParams + "'>&larr;  Previous</a>";
        } else {
            return "";
        }
    }

    private String getParamsForPaging() {
        StringBuilder queryParamsForPaging = new StringBuilder("");
        Set<String> queryParams = getRequest().queryParams();
        for (String queryParam : queryParams) {
            if (!"page".equals(queryParam)) {
                if (queryParamsForPaging.length() != 0) {
                    queryParamsForPaging.append("&");
                }
                try {
                    queryParamsForPaging.append(queryParam)
                            .append("=")
                            .append(URLEncoder.encode(getRequest().queryParams(queryParam), StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return queryParamsForPaging.toString();
    }

    public String select(String model, String displayProperty, Object selected) throws Exception {
        return select(model, displayProperty, selected, false);
    }

    public String select(String model, String displayProperty, Object selectedId, boolean includeEmpty) throws Exception {
        String select = "<select style='max-width:200px' name='" + model + "Id'>\n";
        Class<?> clazz = Class.forName("edu.montana.csci.csci440.model." + model);
        Method all = clazz.getMethod("all");
        List invoke = (List) all.invoke(null);
        Method idGetter = clazz.getMethod("get" + model + "Id");
        Method displayGetter = clazz.getMethod("get" + displayProperty);
        if (includeEmpty) {
            select += "<option></option>";
        }
        for (Object o : invoke) {
            Object idValue = idGetter.invoke(o);
            String selectedString;
            if (idValue != null &&
                    selectedId != null &&
                    idValue.toString().equals(selectedId.toString())) {
                selectedString = " selected";
            } else {
                selectedString = "";
            }
            select += "  <option value='" + idValue + "' " + selectedString + ">" +
                    displayGetter.invoke(o) +
                    "</option>\n";
        }
        select += "</select>\n";
        return select;
    }

    public static void init() {
        before((request, response) -> {
            System.out.println(">> REQUEST " + request.requestMethod() + " " + request.pathInfo() + getParameterInfo(request));
            Web.set(request, response, System.currentTimeMillis());
        });
        after((request, response) -> {
            long startTimestamp = INFO.get().timestamp;
            long startConnections = INFO.get().getConnections();
            long endConnections = DB.getConnectionCount();
            long totalConnections = endConnections - startConnections;
            System.out.println("  << REQUEST " + request.requestMethod() + " " + request.pathInfo() + " completed in " +
                    ((System.currentTimeMillis() - startTimestamp) / 1000.0) + " seconds " +
                    "(" + totalConnections + " Database Connection" + (totalConnections == 1 ? "" : "s") + ")");
        });

        exception(Exception.class, (e, request, response) -> {
            System.out.println("################################################################");
            System.out.println("#  ERROR ");
            System.out.println("################################################################");
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            response.status(500);
            response.body(BaseController.renderTemplate("templates/error.vm",
                    "error", e,
                    "stacktrace", sw.getBuffer().toString()));
        });
    }

    private static String getParameterInfo(Request request) {
        Set<String> params = request.queryParams();
        if (params.size() > 0) {
            StringBuilder str = new StringBuilder("\n   Parameters: {");
            Object[] paramsArr = params.toArray();
            Arrays.sort(paramsArr);
            for (int i = 0; i < paramsArr.length; i++) {
                if (i != 0) {
                    str.append(", ");
                }
                Object o = paramsArr[i];
                str.append(o.toString()).append(":").append(request.queryParams(o.toString()));
            }
            str.append("}");
            return str.toString();
        } else {
            return "";
        }
    }

    private static class RequestInfo {
        public RequestInfo(Request request, Response response, long timestamp, long connections) {
            this.request = request;
            this.response = response;
            this.timestamp = timestamp;
            this.connections = connections;
        }
        private Request request;
        private Response response;
        private long timestamp;
        private long connections;

        public Request getRequest() {
            return request;
        }

        public Response getResponse() {
            return response;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public long getConnections() {
            return connections;
        }
    }

}
