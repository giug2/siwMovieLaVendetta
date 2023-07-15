package it.uniroma3.siw.controller;

import jakarta.servlet.RequestDispatcher;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            HttpStatus statusCode = HttpStatus.valueOf(Integer.parseInt(status.toString()));

            if(statusCode.is4xxClientError()) {
                return "/errors/error404";
            }
            else if(statusCode.is5xxServerError()) {
                return "/errors/error500";
            }
        }
        return "/errors/error404";
    }
}
