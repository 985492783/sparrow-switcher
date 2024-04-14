/*
 * Copyright 1999-2022 Alibaba Group Holding Ltd.
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

package com.sparrow.switcher.common.remote.constants;

/**.
 * @author pixel-revolve
 * @description It is used to know a certain ability whether supporting.
 * @date 2022/8/31 12:27
 **/
public enum AbilityStatus {
    
    /**.
     * Support a certain ability
     */
    SUPPORTED,
    
    /**.
     * Not support a certain ability
     */
    NOT_SUPPORTED,
    
    /**.
     * Cannot find ability table, unknown
     */
    UNKNOWN
}
