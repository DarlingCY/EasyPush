package EasyPush.bizService;

import EasyPush.enums.MessageChannelEnum;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: CheckService
 * @Author: ChenYue
 * @Date: 2022/10/25
 */
@Component
public class CheckService {

    //校验方法
    private final Map<MessageChannelEnum, Function<JSONObject, Boolean>> checkFunMap = new HashMap<>();

    //校验字段
    private final Map<MessageChannelEnum, List<String>> checkFieldMap = new HashMap<>();

    @PostConstruct
    public void init() {
        //校验方法
        checkFunMap.put(MessageChannelEnum.DING_TALK_GROUP_BOT, this::dingTalkGroupBot);
        //校验字段
        List<String> dingTalkGroupBot = new ArrayList<>();
        dingTalkGroupBot.add("secretKey");
        dingTalkGroupBot.add("webhook");
        checkFieldMap.put(MessageChannelEnum.DING_TALK_GROUP_BOT, dingTalkGroupBot);
    }

    public Boolean check(MessageChannelEnum messageChannelEnum, JSONObject config) {
        Function<JSONObject, Boolean> function = checkFunMap.get(messageChannelEnum);
        if (null == function) {
            return false;
        }
        return function.apply(config);
    }

    private Boolean dingTalkGroupBot(JSONObject config) {
        if (null == config || config.isEmpty()) {
            return false;
        }
        List<String> list = checkFieldMap.get(MessageChannelEnum.DING_TALK_GROUP_BOT);
        for (String s : list) {
            if (StringUtils.isBlank(config.getString(s))) {
                return false;
            }
        }
        return true;
    }
}
