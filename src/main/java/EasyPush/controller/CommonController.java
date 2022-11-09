package EasyPush.controller;

import EasyPush.info.MessageInfo;
import EasyPush.service.MessageService;
import EasyPush.util.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName: CommonController
 * @Author: ChenYue
 * @Date: 2022/10/25
 */
@RestController
@RequestMapping("/sendMessage")
public class CommonController {

    @Resource
    private MessageService messageService;

    @PostMapping
    public R<Boolean> sendMessage(@RequestBody MessageInfo messageInfo) {
        return messageService.sendMessage(messageInfo);
    }
}
