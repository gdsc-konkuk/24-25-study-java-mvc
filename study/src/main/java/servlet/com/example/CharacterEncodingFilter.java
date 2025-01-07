package servlet.com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    private static final String ENCODING = "UTF-8"; // 사용할 문자 인코딩

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 요청(Request)와 응답(Response)의 문자 인코딩 설정
        request.setCharacterEncoding(ENCODING);
        response.setCharacterEncoding(ENCODING);

        // 로그 기록
        request.getServletContext().log("doFilter() 호출");

        // 다음 필터 또는 서블릿으로 요청 전달
        chain.doFilter(request, response);
    }
}
