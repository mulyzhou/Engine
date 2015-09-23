package com.flying.view.xfire;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.codehaus.xfire.XFire;
import org.codehaus.xfire.transport.http.XFireServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.flying.service.Engine;

/**
 * An servlet which exposes XFire services via Spring.
 * 
 * @author Jason Carreira <jcarreira@eplus.com>
 */
public class XfireServlet extends XFireServlet
{
    private String xfireBeanName = "xfire";

    private XFire xfire;

    public void init(ServletConfig servletConfig)
        throws ServletException
    {
        String configBeanName = servletConfig.getInitParameter("XFireBeanName");

        xfireBeanName = ((configBeanName != null) && (!"".equals(configBeanName.trim()))) ? configBeanName
                : xfireBeanName;
        
        xfire = (XFire) Engine.ac.getBean(xfireBeanName, XFire.class);
        super.init(servletConfig);
    }

    public XFire createXFire()
    {
        return xfire;
    }
}