/**
 * Copyright 2023 GiftOrg Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.giftorg.common.bigmodel.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.giftorg.common.bigmodel.BigModel;
import org.giftorg.common.config.Config;
import org.giftorg.common.tokenpool.APITaskResult;
import org.giftorg.common.tokenpool.TokenPool;
import org.giftorg.common.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ChatGPT implements Serializable, BigModel {
    private static final String baseUrl = StringUtil.trimEnd(Config.chatGPTConfig.getHost(), "/");
    private static final List<String> apiKeys = Config.chatGPTConfig.getApiKeys();
    private static final String model = Config.chatGPTConfig.getModel();
    private static final String apiUrl = baseUrl + "/v1/chat/completions";
    private static final TokenPool tokenPool = TokenPool.getTokenPool(apiKeys, 64, 3);

    /**
     * 聊天接口，接收一个消息列表，返回大模型回复的消息
     */
    public String chat(List<Message> messages) {
        Request req = new Request(model, messages);
        AtomicReference<HttpResponse> atResp = new AtomicReference<>();
        log.info("chat request: {}", messages);

        APITaskResult result = tokenPool.runTask(token -> {
            HttpResponse resp = HttpRequest.post(apiUrl)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(JSON.toJSONBytes(req))
                    .execute();
            atResp.set(resp);
        });

        if (!result.getSuccess()) {
            throw new RuntimeException(result.getException());
        }

        HttpResponse resp = atResp.get();
        Response res = JSONUtil.toBean(resp.body(), Response.class);
        if (res.choices == null || res.choices.isEmpty()) {
            log.error("chat response error, response.body: {}", resp.body());
            throw new RuntimeException("chat response error, response.body: " + resp.body());
        }

        log.info("chat answer: {}", res.choices.get(0).message.content);
        return res.choices.get(0).message.content;
    }

    @Override
    public List<Double> textEmbedding(String prompt) throws Exception {
        throw new Exception("not implemented");
    }

    /**
     * ChatGPT 请求体
     */
    public static class Request {
        public String model;
        public List<Message> messages;

        public Request(String model, List<Message> messages) {
            this.model = model;
            this.messages = messages;
        }
    }

    /**
     * ChatGPT 响应体
     */
    @ToString
    public static class Response {
        public List<Choice> choices;
    }

    @ToString
    public static class Choice {
        public Message message;
    }

    public static void main(String[] args) throws Exception {
        ChatGPT gpt = new ChatGPT();
        ArrayList<BigModel.Message> messages = new ArrayList<>();
        messages.add(new BigModel.Message("user", "hello"));;
        String answer = gpt.chat(messages);
        System.out.println(answer);
        TokenPool.closeDefaultTokenPool();
    }
}
