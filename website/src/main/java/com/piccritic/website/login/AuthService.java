package com.piccritic.website.login;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.Cookie;

import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;

/**
 * Source code heavily inspired from:
 * @see https://github.com/alejandro-du/remember-me-feature/blob/master/src/main/java/com/example/AuthService.java
 * 
 * This Service will be used by the login panel and the PicCritic UI class to validate cookies.
 * 
 * @author Damien Robichaud <br>
 * 		Francis Bosse
 *
 */
public class AuthService {
	
	private static final String COOKIE = "piccritic-cookie";
	private static final String SESSION_NAME = "handle";
	private static final int COOKIE_TIMEOUT = 60 * 60 * 24 * 30;
	private static final int DELETE_TIMEOUT = 0;
	
	public static boolean isAuthenticated() {
		return VaadinSession.getCurrent().getAttribute(SESSION_NAME) != null || isCookieValid();
	}
	
	public static boolean isCookieValid() {
		Optional<Cookie> picCriticCookie = getUserSession();
		
		if (picCriticCookie.isPresent()) {
			String id = picCriticCookie.get().getValue();
			String handle = /* UserSessionService.getUserSessionCookie(id) */ id;
			
			if (handle != null) { 
				VaadinSession.getCurrent().setAttribute(SESSION_NAME, handle);
				return true;
			}
		}
		return false;
	}
	
	private static Optional<Cookie> getUserSession() {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		return Arrays.stream(cookies).filter(c -> c.getName().equals(COOKIE)).findFirst();
	}
	
	public static String getHandleFromCookie() {
		if (isAuthenticated()) {
			Optional<Cookie> cookie = getUserSession();
			try {
				String id = cookie.get().getValue();
				return /* UserService.getHandleFromCookieId(id) */ id;
			} catch (NoSuchElementException e) { 
				return null;
			}
		}
		
		return null;
	}

	public static void createUserSession(String handle) {
		String id = /* UserService.rememberUserSession(handle)*/ handle;
		setCookieAttr(new Cookie(COOKIE, id), COOKIE_TIMEOUT);
	}

	public static void deleteUserSession(String handle) {
		String id = /* UserService.rememberUserSession(handle)*/ handle;
		setCookieAttr(new Cookie(COOKIE, id), DELETE_TIMEOUT);
	}
	
	private static void setCookieAttr(Cookie cookie, int age) {
		cookie.setPath("/");
		cookie.setMaxAge(age);
		VaadinService.getCurrentResponse().addCookie(cookie);
	}

}
