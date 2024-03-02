package com.kmhoon.mall.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class CustomS3UtilTest {

    @Autowired
    CustomS3Util s3Util;

    @Test
    void testUpload() {
        log.info("update test...........");

        Path filePath = new File("C:\\upload\\m1.jpg").toPath();

        s3Util.uploadFiles(List.of(filePath), false);
    }
}