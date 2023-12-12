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

package org.giftorg.common.utils;

import java.net.URI;
import java.nio.file.*;

public class PathUtil {
    /**
     * 拼接路径，多平台兼容
     */
    public static String join(String first, String... more) {
        return Paths.get(first, more).toString();
    }

    /**
     * 获取路径最后的文件名
     */
    public static String base(String path) {
        return Paths.get(URI.create(path).getPath()).getFileName().toString();
    }
}
