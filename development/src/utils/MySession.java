package utils;

import javax.servlet.http.HttpSession;

public class MySession {
    HttpSession session;

    public HttpSession getSession() { return this.session; }
    public void setSession(HttpSession session) { this.session = session; }

    public MySession() {}
    public MySession(HttpSession session) {
        this.setSession(session);
    }

    public Object get(String name) {
        return this.getSession().getAttribute(name);
    }

    public void add(String name, Object value) {
        HttpSession session = this.getSession();
        session.setAttribute(name, value);
        this.setSession(session);
    }

    public void delete(String name) {
        HttpSession session = this.getSession();
        session.removeAttribute(name);
        this.setSession(session);
    }

}