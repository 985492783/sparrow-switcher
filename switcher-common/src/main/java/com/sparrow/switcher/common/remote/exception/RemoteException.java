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

package com.sparrow.switcher.common.remote.exception;

import com.sparrow.switcher.common.remote.exception.runtime.SparrowRuntimeException;

/**
 * super exception in remote module.
 *
 * @author pixel-revolve
 */
public class RemoteException extends SparrowRuntimeException {
    
    public RemoteException(int errorCode) {
        super(errorCode);
    }
    
    public RemoteException(int errorCode, String msg) {
        super(errorCode, msg);
    }
    
    public RemoteException(int errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
