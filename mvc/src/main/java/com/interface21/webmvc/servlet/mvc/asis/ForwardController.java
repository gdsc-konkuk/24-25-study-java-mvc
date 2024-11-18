package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;
@com.interface21.context.stereotype.Controller
public class ForwardController implements Controller {

    private final String path;

    public ForwardController(final String path) {
        this.path = Objects.requireNonNull(path);
    }

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public void AnnotationGetMapping(){

    }

    @RequestMapping(value = "/post-test",method = RequestMethod.POST)
    public void AnnotationPostMapping(){

    }

    @Override
    public String execute(final HttpServletRequest request, final HttpServletResponse response) {

        return path;
    }
}
