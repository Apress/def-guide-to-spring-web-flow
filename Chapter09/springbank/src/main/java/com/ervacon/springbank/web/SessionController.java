package com.ervacon.springbank.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import com.ervacon.springbank.user.User;

public class SessionController extends MultiActionController {
	
	public SessionController() {
		setCacheSeconds(0);
	}

	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("login");
	}
	
	public ModelAndView prepare(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getDetails();
		request.getSession(true).setAttribute("user", user);
		return new ModelAndView(new RedirectView("/balances/show.html", true));
	}

	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().invalidate();
		return new ModelAndView("login");
	}
}
