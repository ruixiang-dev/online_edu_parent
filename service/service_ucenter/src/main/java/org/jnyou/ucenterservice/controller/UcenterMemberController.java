package org.jnyou.ucenterservice.controller;


import io.swagger.annotations.ApiOperation;
import org.jnyou.commonutils.JwtUtils;
import org.jnyou.commonutils.R;
import org.jnyou.servicebase.exception.IsMeException;
import org.jnyou.ucenterservice.entity.UcenterMember;
import org.jnyou.ucenterservice.entity.vo.RegisterVo;
import org.jnyou.ucenterservice.service.UcenterMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author jnyou
 * @since 2020-06-26
 */
@RestController
@RequestMapping("ucenterservice/ucenter")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    /**
     * 登陆
     * @param member
     * @return
     */
    @PostMapping("login")
    @ApiOperation(value = "会员登录")
    public R loginUser(@RequestBody UcenterMember member){
        String token = memberService.login(member);
        return R.ok().put("token",token);
    }

    /**
     * 注册
     * @param registerVo
     * @return
     */
    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    /**
     * 根据token获取到id,根据id查询用户信息
     * @param request
     * @return
     */
    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("auth/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request){
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            UcenterMember member = memberService.getById(memberId);
            return R.ok().put("item", member);
        }catch (Exception e){
            e.printStackTrace();
            throw new IsMeException(-1,"获取用户信息失败");
        }
    }

}

