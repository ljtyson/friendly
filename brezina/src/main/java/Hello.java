

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Hello
 */
@WebServlet("/hello")
public class Hello extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Hello() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<title>Witch Hazel</title>");
		writer.println("</head>");
		writer.println("<body bgcolor=lightblue>");
		writer.println("<table border=\"0\">");
		writer.println("<tr>");
		writer.println("<td>");
		writer.println("<hr>");
		writer.println("</td>");
		writer.println("<td>");
		writer.println("<h1>Sample Servlet</h1>");
		writer.println("This is the output of a servlet that is part of");
		writer.println("the Hello, Tyson application.  It displays the");
		writer.println("request headers from the request we are currently");
		writer.println("processing.");
		writer.println("</td>");
		writer.println("</tr>");
		writer.println("</table>");
		writer.println("<table border=\"0\" width=\"100%\">");
		writer.println("</table>");
		writer.println("</body>");
		writer.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
