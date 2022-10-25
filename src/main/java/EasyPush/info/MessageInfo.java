package EasyPush.info;

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
    private String title;
    private String content;
//    private
}
