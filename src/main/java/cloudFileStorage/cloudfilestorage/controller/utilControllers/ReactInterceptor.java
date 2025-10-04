package cloudFileStorage.cloudfilestorage.controller.utilControllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Slf4j
@Component
public class ReactInterceptor implements HandlerInterceptor {

    public List<String> excludedPaths = List.of("/api","/static","/favicon.ico","/config.js","/v3/api-docs");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        boolean isExcluded = excludedPaths.stream().anyMatch(path::startsWith);
        boolean hasExtension = path.contains(".");

        if (!isExcluded && !hasExtension) {
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;
        }
        return true;
    }
}
