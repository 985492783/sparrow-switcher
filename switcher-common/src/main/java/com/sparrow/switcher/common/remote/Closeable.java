package com.sparrow.switcher.common.remote;

import com.sparrow.switcher.common.remote.exception.SparrowException;

public interface Closeable {
    void shutdown() throws SparrowException;
}
