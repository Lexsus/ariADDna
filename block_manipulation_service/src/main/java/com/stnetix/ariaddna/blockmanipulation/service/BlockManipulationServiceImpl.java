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

package com.stnetix.ariaddna.blockmanipulation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.stnetix.ariaddna.blockmanipulation.exception.BlockDoesNotExistException;
import com.stnetix.ariaddna.blockmanipulation.exception.MetafileIsFolderException;
import com.stnetix.ariaddna.commonutils.dto.vufs.FileType;
import com.stnetix.ariaddna.vufs.bo.Block;
import com.stnetix.ariaddna.vufs.bo.Metafile;
import com.stnetix.ariaddna.vufs.bo.Metatable;



@Service
@Scope(value = "session")
public class BlockManipulationServiceImpl implements IBlockManipulationService {

    private List<String> blockUuidList;
    private HashMap<String, HashSet<String>> blockMap = new HashMap<>();

    private Metafile GetMetafileByFileUuid(Metatable metatable, String fileUuid) {
        Set<Metafile> metafileSet = metatable.getMetafileSet();
        for (Metafile metafile : metafileSet) {
            if (metafile.getFileUuid().equals(fileUuid)) {
                return metafile;
            }
        }
        return null;
    }

    private boolean containsBlock(Metafile metafile, Block block) {
        for (String blockUuid : metafile.getBlockUuidList()) {
            if (blockUuid.equals(block.getBlockUuid())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isExistOnVufs(Block block, Metatable metatable) {

        HashSet<String> blockOfFile = null;
        if (blockMap.containsKey(block.getFileUuid())) {
            blockOfFile = blockMap.get(block.getFileUuid());
            if (blockOfFile.contains(block.getBlockUuid())) {
                return true;
            }
            Metafile metafile = GetMetafileByFileUuid(metatable, block.getFileUuid());
            if (containsBlock(metafile, block)) {
                blockOfFile.add(block.getBlockUuid());
                return true;
            } else {
                return false;
            }
        } else {
            Metafile metafile = GetMetafileByFileUuid(metatable, block.getFileUuid());
            if (metafile != null) {
                blockOfFile = new HashSet<>();
                blockMap.put(block.getFileUuid(), blockOfFile);
                if (containsBlock(metafile, block)) {
                    blockOfFile.add(block.getBlockUuid());
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        }
    }

    @Override
    public String getNextBlockUuid(Metafile metafile)
            throws MetafileIsFolderException, BlockDoesNotExistException {
        if (metafile.getProperties().containsValue(FileType.DIR.toString())) {
            StringBuilder sb = new StringBuilder();
            sb.append(getMetafileInfoForLog(metafile, "getNextBlockUuid"));
            sb.append(", is folder.");
            throw new MetafileIsFolderException(sb.toString());
        } else {
            if (blockUuidList == null) {
                blockUuidList = new ArrayList<>(metafile.getBlockUuidList());
            }

            if (blockUuidList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append(getMetafileInfoForLog(metafile, "getNextBlockUuid"));
                sb.append(" does not have a block.");
                throw new BlockDoesNotExistException(sb.toString());
            }
        }

        return blockUuidList.remove(0);
    }

    @Override
    public String getBlockUuidByNumber(Metafile metafile, int blockNumber)
            throws MetafileIsFolderException, BlockDoesNotExistException {
        List<String> blockUuidList = metafile.getBlockUuidList();
        if (metafile.getProperties().containsValue(FileType.DIR.toString())) {
            StringBuilder sb = new StringBuilder();
            sb.append(getMetafileInfoForLog(metafile, "getBlockUuidByNumber"));
            sb.append(", is folder.");
            throw new MetafileIsFolderException(sb.toString());
        } else {
            if (blockUuidList.size() < blockNumber) {
                StringBuilder sb = new StringBuilder();
                sb.append(getMetafileInfoForLog(metafile, "getBlockUuidByNumber"));
                sb.append(", has block count: ");
                sb.append(blockUuidList.size());
                sb.append(", requested number is: ");
                sb.append(blockNumber);
                throw new BlockDoesNotExistException(sb.toString());
            }
        }

        return blockUuidList.get(blockNumber);
    }

    @Override public void removeBlockFromCache(Block block, Metatable metatable) {
        HashSet<String> blocksOfFile = null;
        if (blockMap.containsKey(block.getFileUuid())) {
            blocksOfFile = blockMap.get(block.getFileUuid());
            blocksOfFile.remove(block.getBlockUuid());
            if (blocksOfFile.size() == 0) {
                blockMap.remove(block.getFileUuid());
            }
        }
    }

    private StringBuilder getMetafileInfoForLog(Metafile metafile, String methodName) {
        return new StringBuilder()
                .append("Method ")
                .append(methodName)
                .append(", Metafile with uuid: ")
                .append(metafile.getFileUuid())
                .append(", version: ")
                .append(metafile.getVersion());
    }
}
