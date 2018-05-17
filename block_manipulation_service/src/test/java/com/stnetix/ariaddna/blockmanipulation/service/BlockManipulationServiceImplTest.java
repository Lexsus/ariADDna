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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.stnetix.ariaddna.commonutils.datetime.DateTime;
import com.stnetix.ariaddna.commonutils.dto.vufs.MetatableType;
import com.stnetix.ariaddna.vufs.bo.Block;
import com.stnetix.ariaddna.vufs.bo.Metatable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stnetix.ariaddna.blockmanipulation.exception.BlockDoesNotExistException;
import com.stnetix.ariaddna.blockmanipulation.exception.MetafileIsFolderException;
import com.stnetix.ariaddna.commonutils.dto.vufs.FileType;
import com.stnetix.ariaddna.commonutils.mavenutil.MavenUtil;
import com.stnetix.ariaddna.vufs.bo.Metafile;

public class BlockManipulationServiceImplTest {

    private Metafile metafile;
    private IBlockManipulationService blockManipulationService;

    @Before
    public void setUp() throws Exception {
        metafile = new Metafile(MavenUtil.getCurrentVersion(), UUID.randomUUID().toString(), null);
        blockManipulationService = new BlockManipulationServiceImpl();
    }

    @After
    public void tearDown() throws Exception {
        metafile = null;
        blockManipulationService = null;
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void getNextBlockUuid() throws BlockDoesNotExistException, MetafileIsFolderException {
        Map<String, String> properties = new HashMap<>();
        properties.put("typeOnFs", FileType.FILE_BIN.toString());
        metafile.setProperties(properties);
        String blockUuid1 = UUID.randomUUID().toString();
        String blockUuid2 = UUID.randomUUID().toString();
        String blockUuid3 = UUID.randomUUID().toString();
        String blockUuid4 = UUID.randomUUID().toString();
        String blockUuid5 = UUID.randomUUID().toString();
        metafile.addBlockUuid(blockUuid1);
        metafile.addBlockUuid(blockUuid2);
        metafile.addBlockUuid(blockUuid3);
        metafile.addBlockUuid(blockUuid4);
        metafile.addBlockUuid(blockUuid5);

        String blockUuid = blockManipulationService.getNextBlockUuid(metafile);
        assertEquals(blockUuid1, blockUuid);

        blockUuid = blockManipulationService.getNextBlockUuid(metafile);
        assertEquals(blockUuid2, blockUuid);

        blockUuid = blockManipulationService.getNextBlockUuid(metafile);
        assertEquals(blockUuid3, blockUuid);

        blockUuid = blockManipulationService.getNextBlockUuid(metafile);
        assertEquals(blockUuid4, blockUuid);

        blockUuid = blockManipulationService.getNextBlockUuid(metafile);
        assertEquals(blockUuid5, blockUuid);

        //here it throws exception
        blockManipulationService.getNextBlockUuid(metafile);
    }

    @Test
    public void CheckBlock() {
        int blockSize = 1024;
        String version = "1";
        byte[] data = new byte[blockSize];
        DateTime date = new DateTime();

        Map<String, String> properties = new HashMap<>();
        properties.put("typeOnFs", FileType.FILE_BIN.toString());
        metafile.setProperties(properties);

        Random random = new Random();
        random.nextBytes(data);
        Block block1 = new Block(version, 0L, metafile.getFileUuid(), data,
                date.getTimeInMillisec(),
                (long) blockSize);
        random.nextBytes(data);
        Block block2 = new Block(version, 1L, metafile.getFileUuid(), data,
                date.getTimeInMillisec(),
                (long) blockSize);
        random.nextBytes(data);
        Block block3 = new Block(version, 1L, metafile.getFileUuid(), data,
                date.getTimeInMillisec(),
                (long) blockSize);
        random.nextBytes(data);
        Block block4 = new Block(version, 1L, metafile.getFileUuid(), data,
                date.getTimeInMillisec(),
                (long) blockSize);
        metafile.addBlockUuid(block1.getBlockUuid());
        metafile.addBlockUuid(block2.getBlockUuid());
        metafile.addBlockUuid(block3.getBlockUuid());

        Metatable metatable = new Metatable(MetatableType.MASTER, UUID.randomUUID().toString(),
                UUID.randomUUID().toString());
        metatable.addMetafile(metafile);
        assertTrue(blockManipulationService.checkBlock(block3, metatable));
        assertFalse(blockManipulationService.checkBlock(block4, metatable));
        assertTrue(blockManipulationService.checkBlock(block3, metatable));

        //remove block
        metafile.removeBlockUuid(block3.getBlockUuid());
        blockManipulationService.removeBlockFromCache(block3, metatable);
        assertFalse(blockManipulationService.checkBlock(block3, metatable));
    }

    @Test(expected = MetafileIsFolderException.class)
    public void getNextBlockUuidByFolder()
            throws BlockDoesNotExistException, MetafileIsFolderException {
        Map<String, String> properties = new HashMap<>();
        properties.put("typeOnFs", FileType.DIR.toString());
        metafile.setProperties(properties);
        //here it throws exception
        blockManipulationService.getNextBlockUuid(metafile);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void getBlockUuidByNumber()
            throws BlockDoesNotExistException, MetafileIsFolderException {
        Map<String, String> properties = new HashMap<>();
        properties.put("typeOnFs", FileType.FILE_BIN.toString());
        metafile.setProperties(properties);
        String blockUuid1 = UUID.randomUUID().toString();
        String blockUuid2 = UUID.randomUUID().toString();
        String blockUuid3 = UUID.randomUUID().toString();
        String blockUuid4 = UUID.randomUUID().toString();
        String blockUuid5 = UUID.randomUUID().toString();
        metafile.addBlockUuid(blockUuid1);
        metafile.addBlockUuid(blockUuid2);
        metafile.addBlockUuid(blockUuid3);
        metafile.addBlockUuid(blockUuid4);
        metafile.addBlockUuid(blockUuid5);

        String blockUuid = blockManipulationService.getBlockUuidByNumber(metafile, 0);
        assertEquals(blockUuid1, blockUuid);

        blockUuid = blockManipulationService.getBlockUuidByNumber(metafile, 1);
        assertEquals(blockUuid2, blockUuid);

        //here it throws exception
        blockManipulationService.getBlockUuidByNumber(metafile, 9);
    }

    @Test(expected = MetafileIsFolderException.class)
    public void getBlockUuidByNumberByFolder()
            throws BlockDoesNotExistException, MetafileIsFolderException {
        Map<String, String> properties = new HashMap<>();
        properties.put("typeOnFs", FileType.DIR.toString());
        metafile.setProperties(properties);
        //here it throws exception
        blockManipulationService.getBlockUuidByNumber(metafile, 9);
    }
}