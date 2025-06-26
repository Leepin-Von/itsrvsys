package com.plotech.itsrvsys.jmreport;

import com.plotech.itsrvsys.exception.CommonBaseErrorCode;
import com.plotech.itsrvsys.exception.CommonBaseException;
import com.plotech.itsrvsys.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@AllArgsConstructor
public class JimuReportTokenService implements JmReportTokenServiceI {

    private final JwtUtil jwtUtil;

//    @Override
//    public String getToken(HttpServletRequest request) {
//        // 获取请求头中的token
//        String token = request.getHeader("token");
//        if (!StringUtils.hasLength(token)) {
//            token = "";
//            throw new CommonBaseException(CommonBaseErrorCode.USER_NOT_PERMISSION);
//        }
//        log.info("token: {}", token);
//        return token;
//    }

    @Override
    public String getUsername(String s) {
        if (!StringUtils.hasLength(s)) {
            throw new CommonBaseException(CommonBaseErrorCode.USER_NOT_PERMISSION);
        }
        Claims claims = jwtUtil.parseJwt(s);
        return claims.get("username").toString();
    }

    @Override
    public String[] getRoles(String s) {
        return new String[]{"admin", "lowdeveloper", "dbadeveloper"};
    }

    @Override
    public String[] getPermissions(String s) {
        if (getUsername(s).equals("ADMIN")) {
            log.info("用户名为ADMIN, 获得积木报表的所有权限");
            return new String[]{"drag:datasource:testConnection", "onl:drag:clear:recovery", "drag:analysis:sql", "drag:design:getTotalData"};
        } else {
            log.info("用户名不为ADMIN, 获得积木报表的普通权限");
            return null;
        }
    }

    @Override
    public Boolean verifyToken(String s) {
        return !jwtUtil.parseJwt(s).isEmpty();
    }
}
