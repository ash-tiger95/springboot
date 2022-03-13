package com.example.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j // lombok에서 제공, (== Logger logger = LoggerFactory.getLogger(ZuulLoggingFilter.class); )
@Component
public class ZuulLoggingFilter extends ZuulFilter { // Log를 출력해주는 필터

    @Override
    public Object run() throws ZuulException {
        log.info("************printing logs: ");

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest(); // 어떤 요청인지 알 수 있다.

        log.info("************ " + request.getRequestURI());
        return null;
    }

    @Override
    public String filterType() { // 이 필터가 사전인지 사후인지 결정
        return "pre";
    }

    @Override
    public int filterOrder() { // 필터 순서
        return 1;
    }

    @Override
    public boolean shouldFilter() { // 필터 사용 유무
        return true;
    }
}
