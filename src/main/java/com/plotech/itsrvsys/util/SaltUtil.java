package com.plotech.itsrvsys.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SaltUtil {
    public String generateSalt() {
        Random random = new Random();
        // 生成一个16位的随机数
        StringBuilder stringBuilder = new StringBuilder(16);
        stringBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int len = stringBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                stringBuilder.append("0");
            }
        }
        // 生成盐
        return stringBuilder.toString();
    }
}
