package com.flying.view.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.flying.init.StaticVariable;

public class MySessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        //向sessions存入session
        StaticVariable.SESSIONS.put(session.getId(), session);
	}

	public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
       
        //销毁的session均从HashSet集中移除
        StaticVariable.SESSIONS.remove(session.getId());
	}
}
