package hellomvc.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("HelloServlet.service"); // 단축키 soutm
        System.out.println("request = " + request);  // 단축키 soutv
        System.out.println("response = " + response);

        String username = request.getParameter("username"); //파라미터의 요정 정보를 받아옴.
        System.out.println("username = " + username);

        response.setContentType("text/plain"); // 응답의 콘텐츠 다입에 대한 정보
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("hello " + username+username); // 응답 메세지를 반환할 수 있다.

    }
}