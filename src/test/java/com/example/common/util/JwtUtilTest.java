package com.example.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * JwtUtil.getTokenByRequest() 方法的单元测试类
 * <p>
 * 场景覆盖：
 * - 参数中存在 token
 * - Header 中存在 token
 * - 都不存在 token
 */
class JwtUtilTest {

    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
    }

    @Test
    void testGetToken_ParameterTokenExists() {
        when(request.getParameter("token")).thenReturn("abc123");
        when(request.getHeader(JwtUtil.X_ACCESS_TOKEN)).thenReturn(null);

        String result = JwtUtil.getTokenByRequest(request);

        assertEquals("abc123", result);
        verify(request, times(1)).getParameter("token");
        verify(request, never()).getHeader(JwtUtil.X_ACCESS_TOKEN);
    }

    @Test
    void testGetToken_HeaderTokenExists() {
        when(request.getParameter("token")).thenReturn(null);
        when(request.getHeader(JwtUtil.X_ACCESS_TOKEN)).thenReturn("def456");

        String result = JwtUtil.getTokenByRequest(request);

        assertEquals("def456", result);
        verify(request, times(1)).getParameter("token");
        verify(request, times(1)).getHeader(JwtUtil.X_ACCESS_TOKEN);
    }

    @Test
    void testGetToken_NoTokenAvailable() {
        when(request.getParameter("token")).thenReturn(null);
        when(request.getHeader(JwtUtil.X_ACCESS_TOKEN)).thenReturn(null);

        String result = JwtUtil.getTokenByRequest(request);

        assertNull(result);
        verify(request, times(1)).getParameter("token");
        verify(request, times(1)).getHeader(JwtUtil.X_ACCESS_TOKEN);
    }
}
