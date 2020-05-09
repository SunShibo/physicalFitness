package com.ichzh.physicalFitness.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.LoginConfig;
import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.WeChatToken;
import com.ichzh.physicalFitness.dto.MyProfileDto;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.model.UnifiedAccount;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.repository.UnifiedAccountRepository;
import com.ichzh.physicalFitness.security.SecUserDetails;
import com.ichzh.physicalFitness.service.UserService;
import com.ichzh.physicalFitness.service.WeChatService;
import com.ichzh.physicalFitness.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource(name = "memberRepository")
    private MemberRepository memberRepository;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "loginConfig")
    private LoginConfig loginConfig;

    @Resource(name = "unifiedAccountRepository")
    private UnifiedAccountRepository unifiedAccountRepository;

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;

    @Resource(name = "weChatService")
    private WeChatService weChatService;

    @Override
    public Member queryAuthUserInfoByUserName(String userName) {
        return memberRepository.findByMemberUsername(userName);
    }

    @Override
    public Member getLoginUser() {
        Member user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if ((principal instanceof SecUserDetails)) {
                user = ((SecUserDetails) principal).getUser();
            }
        }
        return user;
    }

    @Override
    public Member getCurrentLoginUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        return (Member) redisTemplate.opsForValue().get(token);
    }

    @Override
    public Member queryNewestMember(HttpServletRequest request) {
        Member member = this.getCurrentLoginUser(request);
        Member newestMember = memberRepository.findByMemberId(member.getMemberId());
        newestMember.setWeChatToken(member.getWeChatToken());
        redisTemplate.opsForValue().set(member.getWeChatToken().getLoginKey(), member, loginConfig.getExpirationMinutes(), TimeUnit.MINUTES);
        return newestMember;
    }

    @Override
    public String getCurrentLoginUserId(HttpServletRequest request) {
        Member dmUser = (Member) redisTemplate.opsForValue().get(request.getHeader("token"));
        return dmUser == null ? null : dmUser.getMemberId();
    }

    @Override
    public boolean isUpdateUser(Member member, String nickName, Integer gender, String avatarUrl, String phoneNumber) {
        boolean b = false;
        // 如果用户的微信基本信息有变动，则需更新数据库。首次登录，用户不授权时，信息才会为空。
        if (StringUtils.isNotEmpty(nickName) && !nickName.equals(member.getMemberNickname())) {
            member.setMemberNickname(nickName);
            b = true;
        }
        if (gender != null && !gender.equals(member.getMemberSex())) {
            member.setMemberSex(gender);
            b = true;
        }
        if (StringUtils.isNotEmpty(avatarUrl) && !avatarUrl.equals(member.getMemberPhoto())) {
            member.setMemberPhoto(avatarUrl);
            b = true;
        }
        if (StringUtils.isNotEmpty(phoneNumber) && !phoneNumber.equals(member.getMemberMobile())) {
            member.setMemberMobile(phoneNumber);
            b = true;
        }
        return b;
    }

    @Override
    public Member modifyUser(Member member, String token) {
        // 保存会返回一个Member对象，但是该对象中的其它信息可能会没有，所以调用的地方尽量不使用返回的对象
        memberRepository.save(member);
        redisTemplate.opsForValue().set(token, member, loginConfig.getExpirationMinutes(), TimeUnit.MINUTES);
        return member;
    }

    @Override
    public Member modifyUser(Member member) {
        memberRepository.save(member);
        if (member.getWeChatToken() != null) {
            redisTemplate.opsForValue().set(member.getWeChatToken().getLoginKey(), member, loginConfig.getExpirationMinutes(), TimeUnit.MINUTES);
        }
        return member;
    }

    /**
     * 根据微信的openId查询会员信息
     *
     * @param openid
     * @return
     */
    @Override
    public Member queryByMemberWeChat(String openid) {
        return memberRepository.findByMemberWeChat(openid);
    }

    /**
     * 用户首次登录，创建的用户信息
     *
     * @param openid
     * @return
     */
    @Override
    public Member buildMember(String openid) {
        Member member = new Member();
        member.setMemberWeChat(openid);
        member.setMemberGrade(1);
        member.setMemberUsername("");
        member.setMemberPassword("");
        member.setMemberMobile("");
        member.setMemberNickname("");
        member.setMemberPhoto("");
        member.setMemberSex(0);
        member.setMemberBirthday(null);
        member.setMemberIntegral(0);
        member.setMemberBeans(0);
        member.setExpiryDateBegin(null);
        member.setExpiryDateEnd(null);
        member.setCreateTime(new Date());
        return member;
    }

    @Override
    public Member queryMemberByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    /**
         * 将会员信息封装成 我的 栏目需要的数据.
     * @param member
     */
	public MyProfileDto wrapMyProfileInfo(Member member) {
		
		MyProfileDto ret = new MyProfileDto();
		try
		{
			ret.setBeans(member.getMemberBeans());
			ret.setBeginDate(member.getExpiryDateBegin());
			ret.setEndDate(member.getExpiryDateEnd());
			ret.setMindPhone(member.getMemberMobile());
			
			float intervalDays = CommonUtil.intervalBetweenTwoDate(new Date(), member.getExpiryDateEnd());
			//计算结果保留一位小数
			String restDays = CommonUtil.div(Float.toString(intervalDays), "1", 0);
			ret.setRestDays(Float.valueOf(restDays));
			
		}catch(Exception ex) {
			log.warn(ex.getMessage(), ex);
		}
		return ret;
	}

    @Override
    public UnifiedAccount saveUnifiedAccount(WeChatToken weChatToken) {
        UnifiedAccount unifiedAccount = unifiedAccountRepository.findByAppIdAndOpenId(selfConfig.getAppid(), weChatToken.getOpenid());
        boolean isSave = false;
        if (unifiedAccount == null) {
            unifiedAccount = new UnifiedAccount();
            unifiedAccount.setAppId(selfConfig.getAppid());
            unifiedAccount.setOpenId(weChatToken.getOpenid());
            unifiedAccount.setType(1);
            isSave = true;
        }
        // unionId 数据库还没有，而登录账户已拿到，则设置
        if (StringUtils.isNotEmpty(weChatToken.getUnionid()) && StringUtils.isEmpty(unifiedAccount.getUnionId())) {
            unifiedAccount.setUnionId(weChatToken.getUnionid());
            isSave = true;
        }
        if (isSave) {
            unifiedAccount = unifiedAccountRepository.save(unifiedAccount);
        }
        return unifiedAccount;
    }

    @Override
    public UnifiedAccount saveUnifiedAccount(String openId) {
	    // 数据库中对应的公众号的统一账户信息
        UnifiedAccount unifiedAccount = unifiedAccountRepository.findByAppIdAndOpenId(selfConfig.getPublicAppId(), openId);
        boolean isSave = false;
        if (unifiedAccount == null) {
            unifiedAccount = new UnifiedAccount();
            unifiedAccount.setAppId(selfConfig.getPublicAppId());
            unifiedAccount.setOpenId(openId);
            unifiedAccount.setType(2);
            isSave = true;
        }
        if (StringUtils.isEmpty(unifiedAccount.getUnionId())) {
            // 通过api查询获取 unionId
            WeChatAccount weChatAccount = weChatService.getWeChatAccount(openId, "zh_CN");
            if (StringUtils.isNotEmpty(weChatAccount.getUnionid())) {
                unifiedAccount.setUnionId(weChatAccount.getUnionid());
                isSave = true;
            }
        }
        if (isSave) {
            unifiedAccount = unifiedAccountRepository.save(unifiedAccount);
        }
        return unifiedAccount;
    }
}
