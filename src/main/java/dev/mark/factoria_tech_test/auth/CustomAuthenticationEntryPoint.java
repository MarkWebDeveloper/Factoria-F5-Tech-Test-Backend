package dev.mark.factoria_tech_test.auth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
  
      response.addHeader("WWW-Authenticate", "Basic realm");
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      Map<String, String> error = new HashMap<>();
      error.put("error", "Autenticación fallida");

      OutputStream responseStream = response.getOutputStream();
      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(responseStream, error);
      responseStream.flush();
  }
  
}
