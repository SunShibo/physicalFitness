package com.ichzh.physicalFitness.service;

import com.ichzh.physicalFitness.domain.WeChatToken;
import com.ichzh.physicalFitness.dto.MyProfileDto;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.model.UnifiedAccount;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
	
	/**
	 * 根据用户名查询你用户信息
	 * @param userName
	 * @return
	 */
	Member queryAuthUserInfoByUserName(String userName);

	/**
	 * 获取登录的用户
	 * @return
	 */
	Member getLoginUser();
	
	/**
	 * 获取当前登录的用户
	 * @param request
	 * @return
	 */
	Member getCurrentLoginUser(HttpServletRequest request);

	/**
	 * 获取最新的当前登录的用户信息
	 * @param request
	 * @return
	 */
	Member queryNewestMember(HttpServletRequest request);
	
	/**
	 * 获取当前登录的用户ID
	 * @param request
	 * @return
	 */
	String getCurrentLoginUserId(HttpServletRequest request);

	/**
	 * 判断微信的基本信息是否已修改，如果已修改则需要更新用户信息
	 * @param member 登录的用户对象信息
	 * @param nickName 微信昵称，可空
	 * @param gender 性别（微信中性别 0：未知、1：男、2：女），可空
	 * @param avatarUrl 微信头像地址，可空
	 * @param phoneNumber 用户绑定的手机号（国外手机号会有区号），可空
	 * @return
	 */
    boolean isUpdateUser(Member member, String nickName, Integer gender, String avatarUrl, String phoneNumber);

	/**
	 * 更新或插入用户
	 * @param member 登录的用户对象
	 * @param token 登录标识
	 */
	Member modifyUser(Member member, String token);

	/**
	 * 更新用户信息
	 * @param member
	 * @return
	 */
	Member modifyUser(Member member);

	/**
	 * 根据微信的openId查询会员信息
	 * @param openid
	 * @return
	 */
	Member queryByMemberWeChat(String openid);

	/**
	 * 用户首次登录，创建的用户信息
	 * @param openid 微信的唯一标识
	 * @return
	 */
	Member buildMember(String openid);

	/**
	 * 根据会员id查询会员信息
	 * @param memberId
	 * @return
	 */
    Member queryMemberByMemberId(String memberId);
    
    /**
     * 将会员信息封装成 我的 栏目需要的数据.
     * @param member
     */
    MyProfileDto wrapMyProfileInfo(Member member);

	/**
	 * 保存统一账户信息
	 *
	 * @param weChatToken
	 * @return
	 */
	UnifiedAccount saveUnifiedAccount(WeChatToken weChatToken);

	/**
	 * 更新/保存统一账户信息
	 *
	 * @param openId
	 * @return
	 */
	UnifiedAccount saveUnifiedAccount(String openId);
}
