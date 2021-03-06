/*
 * Copyright (c) 2017 stnetix.com. All Rights Reserved.
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

package com.stnetix.ariaddna.persistence.transformers;

import org.mapstruct.Mapper;

import com.stnetix.ariaddna.commonutils.dto.KeyStorePasswordDTO;
import com.stnetix.ariaddna.persistence.entities.KeyStorePassword;

/**
 * Created by alexkotov on 03.05.17.
 */
@Mapper
public interface KeyStorePasswordTransformer {
    KeyStorePasswordDTO keyStorePasswordEntityToDTO(KeyStorePassword keyStorePassword);

    KeyStorePassword keyStorePasswordDTOToEntity(KeyStorePasswordDTO keyStorePasswordDTO);
}
