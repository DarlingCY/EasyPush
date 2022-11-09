package EasyPush.service;

import EasyPush.bizService.CheckService;
import EasyPush.enums.MessageChannelEnum;
import EasyPush.info.MessageInfo;
import EasyPush.util.R;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: MessageServiceImpl
 * @Author: ChenYue
 * @Date: 2022/10/25
 */
@Service
public class MessageService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CheckService checkService;

    private final Map<MessageChannelEnum, Function<MessageInfo, R<Boolean>>> functionMap = new HashMap<>();

    @PostConstruct
    private void init() {
        functionMap.put(MessageChannelEnum.DING_TALK_GROUP_BOT, this::dingTalkGroupBot);
    }

    public R<Boolean> sendMessage(MessageInfo messageInfo) {
        //校验配置信息
        Boolean check = checkService.check(messageInfo.getChannel(), messageInfo.getConfig());
        if (check) {
            Function<MessageInfo, R<Boolean>> function = functionMap.get(messageInfo.getChannel());
            if (null == function) {
                return R.failed(false, "暂不支持该消息渠道");
            }
            return function.apply(messageInfo);
        } else {
            return R.failed(false, "配置信息异常");
        }
    }

    private R<Boolean> dingTalkGroupBot(MessageInfo messageInfo) {
        JSONObject param = new JSONObject();
        param.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("title", messageInfo.getTitle());
        markdown.put("text", messageInfo.getContent());
        param.put("markdown", markdown);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param, httpHeaders);
        try {
            Long timestamp = System.currentTimeMillis();
            String secret = messageInfo.getConfig().getString("secretKey");
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            //消息发送
            JSONObject result = restTemplate.postForObject(messageInfo.getConfig().getString("webhook") + "&timestamp=" + timestamp + "&sign=" + sign, httpEntity, JSONObject.class);
            if (null != result) {
                Integer code = result.getInteger("errcode");
                if (null != code && 0 == code) {
                    return R.ok(true, "发送成功");
                }
                return R.failed(false, result.getString("errmsg"));
            }
            return R.failed(false, "发送异常");
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            return R.failed(false, e.getMessage());
        }
    }
}
