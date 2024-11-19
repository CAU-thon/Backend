package com.nuneddine.server.service;

import com.nuneddine.server.config.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;

    public Long getMemberIdFromToken(String token) {
        return jwtUtil.extractMemberId(token);
    }
}
