/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.client.external.bootstrap.initializer;

import com.iohao.game.bolt.broker.client.external.bootstrap.handler.ExternalBizHandler;
import io.netty.channel.ChannelPipeline;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * @author 渔民小镇
 * @date 2022-12-05
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultChannelPipelineHook implements ChannelPipelineHook {

    @Override
    public void initChannelPipeline(ChannelPipeline pipeline) {
        /*
         * 这是一个默认的 netty 编排业务实现，
         * 通常情况下，这样的编排方式也会更加清晰，
         * 因为代码统一在这里做编排了。
         *
         * 开发者在自定义业务编排时，可以通过这个钩子接口，
         * 比如添加一个 SSL 到 pipeline addFirst 中。
         *
         * 注意事项：
         * 在调用 hook 前，会经过 ExternalChannelInitializerCallback.initChannelPipeline(SocketChannel) 。
         * ExternalChannelInitializerCallback 接口的实现类有
         *     1.ExternalChannelInitializerCallbackWebsocket
         *     2.ExternalChannelInitializerCallbackTcp
         * 这些实现类中，会给 ChannelPipeline 添加上一些默认的处理器，通常是编解码相关的。
         *
         *
         *
         * 当前默认的钩子实现类 DefaultChannelPipelineHook 只是一个样例，提供参考
         */

        // 添加业务处理器
        pipeline.addLast("ExternalBizHandler", new ExternalBizHandler());
    }
}
