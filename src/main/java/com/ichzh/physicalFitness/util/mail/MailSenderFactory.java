package com.ichzh.physicalFitness.util.mail;
 
 
/**
 * 发件箱工厂
 * 
 * @author MZULE
 * 
 */
public class MailSenderFactory {
 
    /**
     * 服务邮箱
     */
    private static SimpleMailSender serviceSms = null;
 
    /**
     * 获取邮箱
     * 
     * @param type 邮箱类型
     * @return 符合类型的邮箱
     */
//    public static SimpleMailSender getSender(MailSenderType type) {
//	    if (type == MailSenderType.SERVICE) {
//	        if (serviceSms == null) {
//	        serviceSms = new SimpleMailSender("invisible@126.com",
//	            "hidden");
//	        }
//	        return serviceSms;
//	    }
//	    return null;
//    }
    
    /**
     * 获取邮箱
     * @return 邮箱
     */
    public static SimpleMailSender getSender() {
    	if (serviceSms == null) {
//    		serviceSms = new SimpleMailSender("211.153.66.115", "123","111");
    		serviceSms = new SimpleMailSender("211.153.66.115", "tydladmin@bjesr.cn", "111");
    		return serviceSms;
    	}
	    return null;
    }
 
}