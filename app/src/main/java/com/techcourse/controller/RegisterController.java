package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.ModelAndView;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller("/register")
public class RegisterController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView resolve(final HttpServletRequest req, final HttpServletResponse res) {
        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
