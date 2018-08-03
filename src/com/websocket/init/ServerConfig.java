package com.websocket.init;

import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

/**
 * ��Ŀ����ʱ���Զ�������������ContextListener. ��webSocket�ĺ��������ࡣ
 * 
 */
public class ServerConfig implements ServerApplicationConfig {

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scan) {
		// ɨ��src��������@ServerEndPointע����ࡣ
		System.out.println("ɨ�赽" + scan.size() + "������˳���");
		return scan;
	}

	// ��ȡ�����Խӿڷ�ʽ���õ�webSocket�ࡣ
	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> point) {
		System.out.println("ʵ��EndPoint�ӿڵ���������" + point.size());
		return null;
	}

}
