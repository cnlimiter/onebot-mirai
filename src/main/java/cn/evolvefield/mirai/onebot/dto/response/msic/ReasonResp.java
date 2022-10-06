package cn.evolvefield.mirai.onebot.dto.response.msic;

import cn.evolvefield.mirai.onebot.dto.response.Response;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 2:29
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ReasonResp extends Response {
    @Expose
    String message;

}
