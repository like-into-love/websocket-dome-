package com.websocket.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.codehaus.jackson.map.ObjectMapper;

import com.websocket.vo.ContentVo;
import com.websocket.vo.Message;

/**
 * ��ͨ�Źܵ�
 * 
 */
@ServerEndpoint("/webSocket/chat/{user}")
public class ChatSocket {
	// ����һ��ȫ�ֱ�������sockets,�û����ÿ����¼�û���ͨ�Źܵ�
	private static Set<ChatSocket> sockets = new HashSet<ChatSocket>();
	// ����һ��ȫ�ֱ���Session,���ڴ�ŵ�¼�û����û���
	private Session session;
	// ����һ��ȫ�ֱ���map��keyΪ�û��������û���Ӧ��sessionΪvalue
	private static Map<String, Session> map = new ConcurrentHashMap<String, Session>();
	// ����һ�����飬���ڴ�����еĵ�¼�û�,��ʾ������ҳ����û��б�����
	private static List<String> names = new ArrayList<String>();
	private String username;
	private ObjectMapper jackson = new ObjectMapper();

	/*
	 * �����û���¼
	 */
	@OnOpen
	public void open(@PathParam("user") String user, Session session) {
		this.session = session;
		// ����ǰ�����ϵ��û�session��Ϣȫ���浽scokets��
		sockets.add(this);
		// �ѵ�½�û�����ֵ��ȫ�ֱ���
		this.username = user;
		// ÿ��¼һ���û����ͽ����û������뵽names������,����ˢ�º����б�
		names.add(this.username);
		// ����ǰ��¼�û��Լ���Ӧ��session���뵽map��
		map.put(this.username, this.session);
		System.out.println("�û�" + this.username + "����������");
		Message message = new Message();
		message.setAlert("�û�" + this.username + "����������");
		// ����ǰ���е�¼�û����뵽message�У����ڹ㲥���͵�����ҳ��
		message.setNames(names);
		// ��������Ϣ�㲥������ͨ�Źܵ�(sockets)
		// Message��תJSON
		// ��������{"name":"zhangsan","age":20,"birthday":844099200000,"email":"zhangsan@163.com"}
		try {
			broadcast(sockets, jackson.writeValueAsString(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * �˳���¼
	 */
	@OnClose
	public void close(Session session) {
		// �Ƴ��˳���¼�û���ͨ�Źܵ�
		try {
			sockets.remove(this);
			// ���û�����names���޳�������ˢ�º����б�
			names.remove(this.username);
			Message message = new Message();
			System.out.println("�û�" + this.username + "�˳�������");
			message.setAlert(this.username + "�˳���ǰ�����ң�����");
			// ˢ�º����б�
			message.setNames(names);
			broadcast(sockets, jackson.writeValueAsString(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @OnMessage ���տͻ��˷��͹�������Ϣ��Ȼ���ж��ǹ㲥���ǵ���
	 */
	@OnMessage
	public void receive(Session session, String msg) throws IOException {
		// ���ͻ�����Ϣת��json����
		ContentVo vo = jackson.readValue(msg, ContentVo.class);
		// �����Ⱥ�ģ�������Ϣ�㲥��������
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (vo.getType() == 1) {
			Message message = new Message();
			message.setDate(sim.format(new Date()));
			message.setFrom(this.username);
			message.setSendMsg(vo.getMsg());
			broadcast(sockets, jackson.writeValueAsString(message));
		} else {
			Message message = new Message();
			message.setDate(sim.format(new Date()));
			message.setFrom(this.username);
			message.setAlert(vo.getMsg());
			message.setSendMsg("<font color=red>����˽���㣺</font>" + vo.getMsg());
			String to = vo.getTo();
			// ���ݵ��Ķ��������(�����ӳɹ����û���)�õ�Ҫ���Ķ����Session
			Session to_session = map.get(to);
			// ����ǵ��ģ��ͽ���Ϣ���͸��Է�
			to_session.getBasicRemote().sendText(jackson.writeValueAsString(message));
		}
	}

	/*
	 * �㲥��Ϣ
	 */
	public void broadcast(Set<ChatSocket> sockets, String msg) {
		// ������ǰ���е����ӹܵ�����֪ͨ��Ϣ���͸�ÿһ���ܵ�
		for (ChatSocket socket : sockets) {
			try {
				// ͨ��session������Ϣ
				socket.session.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
