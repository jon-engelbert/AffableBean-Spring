package affableBean.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/*
 * taken from:
 * https://gist.github.com/jonikarppinen/662c38fb57a23de61c8b
 * Just a basic implementation of Spring Boot's built-in BasicErrorController,
 * customized with our own error messages and code and directed to the default
 * /error page that Spring is looking for
 */

@Controller
public class CustomErrorController implements ErrorController {

	private static final String PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    String error(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
        // Appropriate HTTP response code (e.g. 404 or 500) is automatically set by Spring. 
        // Here we just define response body.
    	Map<String, Object> errors = getErrorAttributes(request);
    	mm.put("status", response.getStatus());
    	mm.put("errorCode", errors.get("error"));
    	mm.put("errorMessage", errors.get("message"));
        return PATH;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, false);
    }


}
