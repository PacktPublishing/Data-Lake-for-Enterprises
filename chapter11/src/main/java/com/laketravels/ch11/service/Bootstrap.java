package com.laketravels.ch11.service;



import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.model.ApiInfo;

import javax.servlet.http.HttpServlet;


public class Bootstrap extends HttpServlet {
    static {
        ConfigFactory.config().setBasePath("/services");
        ApiInfo info = new ApiInfo(
                "Single Customer View API",     /* title */
                "API for Customer",
                "",                             /* TOS URL */
                "api@scv.com",                  /* Contact */
                "",                             /* license */
                ""                              /* license URL */
        );
        ConfigFactory.config().setApiInfo(info);
    }
}