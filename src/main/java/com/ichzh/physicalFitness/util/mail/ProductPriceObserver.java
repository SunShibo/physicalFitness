package com.ichzh.physicalFitness.util.mail;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
 
public class ProductPriceObserver implements Observer {

	@Override
    public void update(Observable obj, Object arg) {
		SimpleMailSender sms = MailSenderFactory.getSender();
		List<String> recipients = new ArrayList<String>();
		recipients.add("gx@chzh.cn");
//		recipients.add("xu.qiaoxue@chzh.cn");
//		recipients.add("invisible@chzh.cn");
		try {
			// 发送邮件
			for(String recipient : recipients){
				sms.send(recipient, "服务器异常", "定时任务服务器，单表推数据异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
 
    
 
}