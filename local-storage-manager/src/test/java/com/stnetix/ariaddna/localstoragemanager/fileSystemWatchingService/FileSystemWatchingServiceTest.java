package com.stnetix.ariaddna.localstoragemanager.fileSystemWatchingService;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


class FileSystemWatchingServiceTest {
    static FileSystemWatchingService service;
    static Path root;
    static int count;

    @BeforeAll
    static void setUp() throws IOException {
        //Change to yours test folder


    }

    @BeforeEach
    void beforeEach() throws IOException {
        root = Files.createTempDirectory("ariaddnaTemp");
        service = new FileSystemWatchingService(root);


        Thread thread = new Thread(() -> {
            service.processEvents();
        });
        thread.start();

        try {
            thread.join(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count = 0;
        service.removeEventListners();
    }

    @Test
    void createFilesTest() throws InterruptedException {

        service.addEventListener(e -> {
            if (e.getType().equals(FileSystemWatchEvent.Type.CREATE)) {
                count++;
            }
        });

        int filesCount = 10;

        createTempFiles(filesCount, root);

        Thread.sleep(1000);

        Assertions.assertEquals(filesCount, count);
    }

    @Test
    void deleteFilesTest() throws IOException, InterruptedException {
        count = 0;

        service.addEventListener(e -> {
            if (e.getType().equals(FileSystemWatchEvent.Type.DELETE)) {
                count++;
            }
        });

        int filesCount = 10;

        createTempFiles(filesCount, root);

        Thread.sleep(1000);

        deleteFiles(root);

        Thread.sleep(1000);

        Assertions.assertEquals(filesCount, count);


    }

    @Test
    void renameFileTest() throws InterruptedException, IOException {
        //count = 0;

        Path path = Paths.get("C:/temp/tmp");

        service.registerDirectory(path);

        service.addEventListener(event -> {
            if (event.getType().equals(FileSystemWatchEvent.Type.RENAME)) {
                count++;
            }
        });

        int fileCount = 1;

        //createTempFiles(fileCount, root);

        //Thread.sleep(500);

        renameFiles(path);

        Thread.sleep(10000);

        Assertions.assertEquals(fileCount, count);


    }



    static void createTempFiles(int filesCount, Path rootDir) {
        for (int i = 0; i < filesCount; i++) {
            try {
                Files.createTempFile(rootDir, "ariaddnaFile" + i, ".tmp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void deleteFiles(Path dir) throws IOException {

        Files.walkFileTree(dir, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    static void renameFiles(Path dir) throws IOException {
        final int[] i = {0};

        Files.walkFileTree(dir, new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path parent = file.getParent();
                Files.move(file, Paths.get(parent.toString(), "newFile" + i[0] + ".tmp"));
                System.out.println("rename ");
                file.toFile().renameTo((parent.resolve("newFile" + i[0] + ".tmp")).toFile());

                i[0]++;

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @AfterEach
    void tearDown() throws IOException {
        deleteFiles(root);

        Files.delete(root);
    }

    @AfterAll
    static void tearDownAll() throws IOException {


    }




}