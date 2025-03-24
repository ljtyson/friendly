package springapp.web;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;

public class HelloController implements Controller {
	protected final Log logger = LogFactory.getLog(getClass());

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				String now = (new java.util.Date()).toString();
				logger.info("Returning hello view with " + now);
		logger.info("Returning hello view with" + now);
		return new ModelAndView("hello" , "now", now);
//		return new ModelAndView("WEB-INF/jsp/hello.jsp" , "now", now);
//		return new ModelAndView("hello.jsp");
	}
}
