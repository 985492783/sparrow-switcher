package com.sparrow.switcher.common.constants;

/**
 * @author 985492783@qq.com
 * @date 2024/4/11 17:22
 */
public class Constants {
    
    public static final String DATASOURCE_TYPE = "sparrow.switcher.storage.type";
    
    public static final String DATASOURCE_URL = "sparrow.switcher.storage.url";

    public static final String INTERNAL_MODULE = "internal";

    public static class Exception {

        public static final int SERIALIZE_ERROR_CODE = 100;

        public static final int DESERIALIZE_ERROR_CODE = 101;

        public static final int FIND_DATASOURCE_ERROR_CODE = 102;

        public static final int FIND_TABLE_ERROR_CODE = 103;
    }

}
