package com.example.demo.security;

import com.example.demo.security.member.Member;
import com.example.demo.security.member.MemberDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Member getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof MemberDetails) {
            return ((MemberDetails) authentication.getPrincipal()).getMember();
        }
        return null;
    }
}
