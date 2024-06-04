package com.philodice.core.controller;

import com.philodice.common.response.ResponseMessage;
import com.philodice.core.Entity.ServerEntity;
import com.philodice.core.link.LinkManager;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class LinkController {

    private final LinkManager linkManager;

    private final ServerEntity serverEntity;

    public LinkController(LinkManager linkManager, ServerEntity serverEntity) {
        this.linkManager = linkManager;
        this.serverEntity = serverEntity;
    }

    /**
     * 根据长连接创建短链接
     * @param source 长链接
     * @return 响应消息
     */
    @PostMapping("/shorten")
    public ResponseMessage<String> shortenSource(@RequestParam("source") @NotBlank @URL String source) {
        // 检测原始连接是否已存在
        String link = linkManager.checkSourceExists(source);
        if (link != null) {
            return ResponseMessage.success("短链接地址已存在：" + serverEntity.getLink() + link);
        }

        // 创建新的短链接
        String linkCode = linkManager.createLinkCode(source);
        return ResponseMessage.success("创建成功：" + serverEntity.getLink() + linkCode);
    }

    /**
     * 由短链接重定向访问原长连接
     * @param link 短链接
     * @return 重定向的长连接
     */
    @GetMapping("/{link}")
    public RedirectView redirectToSource(@PathVariable String link) {
        // 从 redis 中查询对应长连接
        String source = linkManager.checkLinkExists(link);

//        if (source == null) {
//            // TODO 从数据库中查询对应长连接
              // 如何数据库中也没有，返回错误信息
//        }

        // 更新点击次数
        linkManager.updateLickIncrement(link);
        // 更新最后访问时间
        linkManager.updateLastAccess(link);

        // 302 重定向访问 source
        RedirectView redirectView = new RedirectView(source, true);
        redirectView.setStatusCode(HttpStatus.FOUND);
        return redirectView;
    }
}
