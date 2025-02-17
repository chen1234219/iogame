/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.bolt.broker.client.external.bootstrap.initializer;

import com.iohao.game.bolt.broker.client.external.ExternalServerBuilder;
import com.iohao.game.bolt.broker.client.external.bootstrap.heart.IdleProcessSetting;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2022-01-13
 */
@Setter
@Accessors(chain = true)
public class ExternalChannelInitializerCallbackOption {

    /**
     * netty channel pipeline
     * <pre>
     *     将在下个大版本中移除
     *
     *     请使用 {@link ChannelPipelineHook} 接口来给 netty pipeline 添加处理器
     * </pre>
     */
    @Deprecated
    Map<String, ChannelHandler> channelHandlerProcessors;
    /** 默认数据包最大 1MB */
    int packageMaxSize = 1024 * 1024;
    /** http 升级 websocket 协议地址 */
    String websocketPath = "/websocket";
    /** 心跳相关的构建器 */
    IdleProcessSetting idleProcessSetting;
    /** netty 编排业务钩子接口 */
    ChannelPipelineHook channelPipelineHook;

    /**
     * 添加其他 handler 到 pipeline 中
     *
     * @param pipeline pipeline
     */
    void channelHandler(ChannelPipeline pipeline) {
        // 心跳
        this.idleHandler(pipeline);

        /*
         * 这两句代码都是对 netty 的业务编排，channelHandlerProcessors 已经标记为过期，这里只是做个兼容。
         * 这里做了限制，两者只能选其一，建议使用 channelPipelineHook 来做编排，因为 hook 在使用上灵活性相对高一些。
         */
        this.channelHandlerProcessors(pipeline);
        this.channelPipelineHook(pipeline);
    }

    private void channelPipelineHook(ChannelPipeline pipeline) {
        if (Objects.isNull(this.channelPipelineHook)) {
            return;
        }

        this.channelPipelineHook.initChannelPipeline(pipeline);
    }

    private void idleHandler(ChannelPipeline pipeline) {
        // 心跳处理
        if (Objects.isNull(this.idleProcessSetting)) {
            return;
        }

        this.idleProcessSetting.idlePipeline(pipeline);
    }

    /**
     * <pre>
     *     将在下个大版本中移除
     *     请使用 {@link ExternalServerBuilder#channelPipelineHook(ChannelPipelineHook)} 代替
     * </pre>
     *
     * @param pipeline pipeline
     */
    @Deprecated
    private void channelHandlerProcessors(ChannelPipeline pipeline) {
        if (Objects.nonNull(this.channelHandlerProcessors)) {
            // 将用户编排的 channelHandler 添加到 pipeline 中
            this.channelHandlerProcessors.forEach(pipeline::addLast);
        }
    }
}
