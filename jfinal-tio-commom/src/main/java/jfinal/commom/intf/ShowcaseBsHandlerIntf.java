package jfinal.commom.intf;

import org.tio.core.ChannelContext;

import jfinal.commom.ShowcasePacket;

/**
 * 业务处理器接口
 * @author why
 * @时间 2018年4月2日
 * @联系方式 15024202548
 */
public interface ShowcaseBsHandlerIntf {
	/**
	 * 处理消息
	 * @param packet 需要处理的数据
	 * @param channelContext 通道 链接信息
	 * @return
	 * @throws Exception
	 */
	public Object handler(ShowcasePacket packet, ChannelContext channelContext) throws Exception;
}
