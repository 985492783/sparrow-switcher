/*
 * Copyright 1999-2020 Alibaba Group Holding Ltd.
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

package com.sparrow.switcher.common.remote.request;

import com.sparrow.switcher.common.remote.response.Response;

/**
 * future for request.
 *
 * @author pixel-revolve
 */
public interface RequestFuture {
    
    /**
     * check that it is done or not..
     *
     * @return is done .
     */
    boolean isDone();
    
    /**
     * get response without timeouts.
     *
     * @return return response if done.
     * @throws Exception exception throws .
     */
    Response get() throws Exception;
    
    /**
     * get response with a given timeouts.
     *
     * @param timeout timeout milliseconds.
     * @return return response if done.
     * @throws Exception exception throws .
     */
    Response get(long timeout) throws Exception;
    
}
