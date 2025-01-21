package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.ModelAndView;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller("/logout")
public class LogoutController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView resolve(final HttpServletRequest req, final HttpServletResponse res) {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }
}
