package com.philodice.admin.controller;

import com.philodice.admin.exception.SequencePoolExpandException;
import com.philodice.admin.exception.SequencePoolInitializeException;
import com.philodice.admin.sequence.SequenceManager;
import com.philodice.common.response.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 序列号池相关 controller
 */
@RestController
public class SequencePoolController {

    private final SequenceManager sequenceManager;

    private final Logger logger;

    public SequencePoolController(SequenceManager sequenceManager) {
        this.sequenceManager = sequenceManager;
        this.logger = LoggerFactory.getLogger(SequencePoolController.class);
    }

    /**
     * 初始化序列号池
     * @return 响应消息
     */
    @GetMapping("/api/pool/init")
    public ResponseMessage<String> initSequencePool() {
        Long number = 0L;
        try {
            number = sequenceManager.initPool();
        } catch (Exception e) {
            logger.error("Initialize sequence pool exception: " + e);
            throw new SequencePoolInitializeException("Initialize sequence pool exception: " + e);
        }

        if (number == 0) {
            return ResponseMessage.success("序列号池已存在，如需扩容请执行扩容操作。");
        }

        return ResponseMessage.success("初始化序列号池成功，序列号共：" + number);
    }

    /**
     * 序列号池扩容
     * @return 扩容信息
     */
    @PostMapping("/api/pool/expand")
    public ResponseMessage<String> expandSequencePool(@RequestParam("number") Long number) {
        try {
            sequenceManager.addSequences(number);
        } catch (Exception e) {
            logger.error("Expand sequence pool exception: " + e);
            throw new SequencePoolExpandException("Expand sequence pool exception: " + e);
        }

        return ResponseMessage.success("序列号池扩容成功，共增加序列号：" + number);
    }

    /**
     * 获取序列号池相关信息
     * @return 序列号池信息
     */
    @GetMapping("/api/pool/status")
    public ResponseMessage<String> getSequencePoolStatus() {
        return ResponseMessage.success(sequenceManager.getStatus());
    }
}
