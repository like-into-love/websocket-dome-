package com.websocket.init;

import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

/**
 * 项目启动时会自动启动，类似与ContextListener. 是webSocket的核心配置类。
 * 
 */
public class ServerConfig implements ServerApplicationConfig {

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scan) {
		// 扫描src下所有类@ServerEndPoint注解的类。
		System.out.println("扫描到" + scan.size() + "个服务端程序");
		return scan;
	}

	// 获取所有以接口方式配置的webSocket类。
	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> point) {
		System.out.println("实现EndPoint接口的类数量：" + point.size());
		return null;
	}

}
