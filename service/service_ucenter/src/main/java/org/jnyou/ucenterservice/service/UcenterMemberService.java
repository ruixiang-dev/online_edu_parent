package org.jnyou.ucenterservice.service;

import org.jnyou.ucenterservice.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jnyou.ucenterservice.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author jnyou
 * @since 2020-06-26
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    /**
     * 登陆返回token
     * @param member
     * @return
     */
    String login(UcenterMember member);

    /**
     * 注册
     * @param registerVo
     */
    void register(RegisterVo registerVo);

    /**
     * 通过openid查询用户
     * @param openid
     * @return
     */
    UcenterMember getByOpenid(String openid);
}
