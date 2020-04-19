package com.chengshare.usercenter.auth;

import com.chengshare.usercenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author fate7
 * @Date 2020/4/19 2:02 下午
 **/

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthAspect {

    private final JwtOperator jwtOperator;

    @Around("@annotation(com.chengshare.usercenter.auth.CheckLogin)")
    public Object checklogin(ProceedingJoinPoint point) throws Throwable {
        this.checkToken();
        return point.proceed();
    }

    private void checkToken() {
        try{
            //1. 从header中获取token
            HttpServletRequest request = getHttpServletRequest();

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
        } catch (Throwable e) {
            throw new SecurityException("Token不合法");
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return attributes.getRequest();
    }

    @Around("@annotation(com.chengshare.usercenter.auth.CheckAuthorization)")
    public Object checkAuthorization(ProceedingJoinPoint point) throws Throwable {
        //1. 验证token是否合法
        this.checkToken();
        try {
            //2. 验证角色是否匹配
            HttpServletRequest httpServletRequest = this.getHttpServletRequest();
            String role = (String) httpServletRequest.getAttribute("role");

            //拿到方法的注解中的值
            MethodSignature signature = (MethodSignature)point.getSignature();
            Method method = signature.getMethod();
            CheckAuthorization checkAuthorization = method.getAnnotation(CheckAuthorization.class);
            String value = checkAuthorization.value();

            if (!Objects.equals(role, value)) {
                throw new SecurityException("用户无权访问");
            }

        } catch (Throwable throwable) {
            throw new SecurityException("用户无权访问", throwable);
        }

        return point.proceed();
    }
}
