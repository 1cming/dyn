package com.conning.compents.servlet.servletHandler;

public class ServletHandlerFactory
{
  public static ServletHandler buildHandler(String reqUrl)
  {
    String sourceName = reqUrl.substring(reqUrl.lastIndexOf("/"));
    if (sourceName.indexOf(".") > 0) {
      return new WebResourcesHandler();
    }
    return new PageMarkerHandler();
  }
}