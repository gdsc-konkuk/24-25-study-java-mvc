package com.interface21.core;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
