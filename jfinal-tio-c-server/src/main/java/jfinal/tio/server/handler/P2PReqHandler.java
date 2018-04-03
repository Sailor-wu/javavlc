package jfinal.tio.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

import jfinal.commom.RedisUtil;
import jfinal.commom.ShowcasePacket;
import jfinal.commom.intf.AbsShowcaseBsHandler;
import jfinal.commom.intf.Tools;
import jfinal.commom.packets.P2PReqBody;
import jfinal.commom.packets.P2PRespBody;
import jfinal.commom.util.ShowcaseSessionContext;
import jfinal.commom.util.Type;

/**
 * @author tanyaowu 2017年3月27日 下午9:51:28
 */
public class P2PReqHandler extends AbsShowcaseBsHandler<P2PReqBody> {
	private static Logger log = LoggerFactory.getLogger(P2PReqHandler.class);

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
	public P2PReqHandler() {
	}

	/**
	 * @return
	 * @author tanyaowu
	 */
	@Override
	public Class<P2PReqBody> bodyClass() {
		return P2PReqBody.class;
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
	public Object handler(ShowcasePacket packet, P2PReqBody bsBody, ChannelContext channelContext) throws Exception {
		// 收到点对点请求信息
		System.out.println("收到消息。。。。。。");
		log.info("收到点对点请求消息:{}", Json.toJson(bsBody));
		String messageId = bsBody.getMessageId();
		// ShowcaseSessionContext showcaseSessionContext =
		// (ShowcaseSessionContext) channelContext.getAttribute();
		// 创建响应信息实体 响应需要两个属性，谁响应和响应信息
		P2PRespBody p2pRespBody = new P2PRespBody();
		// 设置响应者
		p2pRespBody.setFromUserid(channelContext.getUserid());
		// 相应信息
		p2pRespBody.setText(bsBody.getText());
		p2pRespBody.setMessageId(messageId);
		ShowcasePacket respPacket = new ShowcasePacket();
		respPacket.setType(Type.P2P_RESP);
		respPacket.setBody(Json.toJson(p2pRespBody).getBytes(ShowcasePacket.CHARSET));
		// Aio.sendToUser(channelContext.getGroupContext(),
		// bsBody.getToUserid(), respPacket);
		Aio.sendToUser(channelContext.getGroupContext(), bsBody.getToUserid(), respPacket);
		if (null != bsBody.getMessageId() && !("".equals(bsBody.getMessageId()))) {
			// respPacket.setSynSeq(1);
			// Aio.synSend(channelContext, respPacket, 3000);
			// 保存到redis里面
			RedisUtil.jedis.set(bsBody.getMessageId(), bsBody.getText());
		}
		// }else{
		// }
		return null;
	}
}
