package EasyPush.info;

import EasyPush.enums.MessageChannelEnum;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: MessageInfo
 * @Author: ChenYue
 * @Date: 2022/10/17
 */
@Data
@Accessors(chain = true)
public class MessageInfo {
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 消息渠道
     */
    private MessageChannelEnum channel;
    /**
     * 配置信息
     */
    private JSONObject config;
}
