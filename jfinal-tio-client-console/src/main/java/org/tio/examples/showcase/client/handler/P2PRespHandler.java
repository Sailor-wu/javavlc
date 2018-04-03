package org.tio.examples.showcase.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

import jfinal.commom.ShowcasePacket;
import jfinal.commom.intf.AbsShowcaseBsHandler;
import jfinal.commom.packets.P2PReqBody;
import jfinal.commom.packets.P2PRespBody;
import jfinal.commom.util.RedisUtil;
import jfinal.commom.util.Type;

/**
 * @author tanyaowu 2017年3月27日 下午9:51:28
 */
public class P2PRespHandler extends AbsShowcaseBsHandler<P2PRespBody> {
	private static Logger log = LoggerFactory.getLogger(P2PRespHandler.class);

	/**
	 * @param args
	 * @author tanyaowu
	 */
	public static void main(String[] args) {

	}

	/**
	 *
	 * @author tanyaowu
	 */
	public P2PRespHandler() {
	}

	/**
	 * @return
	 * @author tanyaowu
	 */
	@Override
	public Class<P2PRespBody> bodyClass() {
		return P2PRespBody.class;
	}

	/**
	 * @param packet
	 * @param bsBody
	 * @param channelContext
	 * @return
	 * @throws Exception
	 * @author tanyaowu
	 */
	@Override
	public Object handler(ShowcasePacket packet, P2PRespBody bsBody, ChannelContext channelContext) throws Exception {
		System.out.println("收到P2P响应消息:" + Json.toJson(bsBody));
		String messID = bsBody.getMessageId();
		System.out.println("messID" + messID);
		// 根据不同的消息类型，做不同的处理
		if (!("".equals(messID)) && null != messID) {
			// 这里是服务器推送，需要回复的
			// String toUserid = //拿到个人信息登录号
			String test[]= {"这里是默认回复的信息第一条","这里是默认回复的信息第二条","这里是默认回复的信息第三条!"};
			int num=(int) (Math.random()*3);
			String text =test[num];// 信息文本
			// 封装消息--点对点消息请求
			P2PReqBody p2pReqBody = new P2PReqBody();
			p2pReqBody.setToUserid(bsBody.getFromUserid());//
			p2pReqBody.setText(text);
			p2pReqBody.setMessageId(messID);
			// 创建数据包，
			ShowcasePacket reqPacket = new ShowcasePacket();
			reqPacket.setType(Type.P2P_REQ);// 设置为点对点消息类型
			reqPacket.setBody(Json.toJson(p2pReqBody).getBytes(ShowcasePacket.CHARSET));
			// 发送 客户端发送完毕 服务器段处理
			Aio.send(channelContext, reqPacket);
		} // 否则就是客户端与客户端之间的通讯
		return null;
	}
}
