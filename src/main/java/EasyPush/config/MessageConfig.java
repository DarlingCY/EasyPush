package EasyPush.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MessageConfig
 * @Author: ChenYue
 * @Date: 2022/10/25
 */
@Component
public class MessageConfig {
    @Value("${message.configSource}")
    public static String configSource;
}
