package com.chengshare.contentcenter.auth;

import com.chengshare.contentcenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author fate7
 * @Date 2020/4/19 2:02 下午
 **/

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckLoginAspect {

    private final JwtOperator jwtOperator;

    @Around("@annotation(com.chengshare.contentcenter.auth.CheckLogin)")
    public Object checklogin(ProceedingJoinPoint point) {
        try{
            //1. 从header中获取token
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = attributes.getRequest();

            String token = request.getHeader("X-Token");

            //2. 校验token是否合法&是否过期，如果不合法，直接抛出异常；如果合法放行
            Boolean isValid = jwtOperator.validateToken(token);
            if (!isValid) {
                throw new SecurityException("Token不合法");
            }

            //3. 校验成功将用户信息设置到request的attributes中
            Claims claimsFromToken = jwtOperator.getClaimsFromToken(token);
            request.setAttribute("id", claimsFromToken.get("id"));
            request.setAttribute("wxNickname", claimsFromToken.get("wxNickname"));
            request.setAttribute("role", claimsFromToken.get("role"));

            return point.proceed();
        } catch (Throwable e) {
            throw new SecurityException("Token不合法");
        }
    }
}
