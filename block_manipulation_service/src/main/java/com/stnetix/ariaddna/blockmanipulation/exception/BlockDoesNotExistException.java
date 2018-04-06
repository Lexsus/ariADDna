/*
 * Copyright (c) 2018 stnetix.com. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.stnetix.ariaddna.blockmanipulation.exception;

/**
 *This exception was throws if Block does not exist in requested object..
 * */

public class BlockDoesNotExistException extends BlockManipulationException {
    public BlockDoesNotExistException(Throwable cause) {
        super(cause);
    }

    public BlockDoesNotExistException(String traceMessage) {
        super(traceMessage);
    }

    public BlockDoesNotExistException(String traceMessage, String errorMessage) {
        super(traceMessage, errorMessage);
    }

    public BlockDoesNotExistException(String traceMessage, Throwable cause) {
        super(traceMessage, cause);
    }

    public BlockDoesNotExistException(String traceMessage, String errorMessage,
            Throwable cause) {
        super(traceMessage, errorMessage, cause);
    }
}