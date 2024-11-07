package com.nuneddine.server.service;

import com.nuneddine.server.config.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private JwtUtil jwtUtil;

    public Long getKakaoIdFromToken(String token) {
        return jwtUtil.extractKakaoId(token);
    }
}
