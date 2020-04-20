package com.chengshare.usercenter.service.user;

import com.chengshare.usercenter.dao.BonusEventLog.BonusEventLogMapper;
import com.chengshare.usercenter.dao.user.UserMapper;
import com.chengshare.usercenter.domain.dto.bonus.BonusDTO;
import com.chengshare.usercenter.domain.dto.message.UserAddBonusMsgDTO;
import com.chengshare.usercenter.domain.dto.user.UserLoginDTO;
import com.chengshare.usercenter.domain.entity.BonusEventLog.BonusEventLog;
import com.chengshare.usercenter.domain.entity.user.User;
import com.chengshare.usercenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author fate7
 * @Date 2020/4/7 9:13 下午
 **/

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;
    private final BonusEventLogMapper bonusEventLogMapper;

    private final JwtOperator jwtOperator;

    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBonus(UserAddBonusMsgDTO msgDTO) {
        Integer userId = msgDTO.getUserId();
        Integer bonus = msgDTO.getBonus();
        User user = this.userMapper.selectByPrimaryKey(userId);

        user.setBonus(user.getBonus() + bonus);
        this.userMapper.updateByPrimaryKey(user);

        this.bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(msgDTO.getUserId())
                        .value(msgDTO.getBonus())
                        .event(msgDTO.getEvent())
                        .createTime(new Date())
                        .description(msgDTO.getDescription())
                        .build()
        );

    }

    public User me(String token){
        Claims claims = this.jwtOperator.getClaimsFromToken(token);
        Integer userId = (Integer) claims.get("id");
        return this.userMapper.selectByPrimaryKey(userId);
    }

    public User login(UserLoginDTO loginDTO, String openId) {
        User user = this.userMapper.selectOne(
                User.builder().wxId(openId).build()
        );
        if (user == null) {
            User userToSave = User.builder()
                    .wxId(openId)
                    .bonus(300)
                    .wxNickname(loginDTO.getWxNickname())
                    .avatarUrl(loginDTO.getAvatarUrl())
                    .roles("user")
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            this.userMapper.insertSelective(userToSave);
            return userToSave;
        }
        return user;
    }

    public User sign(String token) {
        Claims claims = this.jwtOperator.getClaimsFromToken(token);
        Integer userId = (Integer) claims.get("id");
        UserAddBonusMsgDTO msgDTO = UserAddBonusMsgDTO.builder().bonus(10).userId(userId).description("签到").event("SIGN").build();
        this.addBonus(msgDTO);
        return this.userMapper.selectByPrimaryKey(userId);
    }

    public List<BonusDTO> bonusLogs(String token) {
        Claims claims = this.jwtOperator.getClaimsFromToken(token);
        Integer userId = (Integer) claims.get("id");

        List<BonusEventLog> bonusEventLogs = this.bonusEventLogMapper.select(BonusEventLog.builder().userId(userId).build());
        List<BonusDTO> collect = bonusEventLogs.stream().map(bonusEventLog -> {
            BonusDTO bonusDTO = new BonusDTO();
            BeanUtils.copyProperties(bonusEventLog, bonusDTO);
            return bonusDTO;
        }).collect(Collectors.toList());
        return collect;
    }
}
