package com.bookie.legacy.watcha.dto;

import com.bookie.legacy.watcha.type.WatchaUserPhoto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class WatchaUserDtoTest {

    @Test
    void testContains() {
        String defaultPhoto = "https://an2-glx.amz.wtchn.net/assets/default/user/photo_file_name_large-ab0a7f6a92a282859192ba17dd4822023e22273e168c2daf05795e5171e66446.jpg";
        WatchaUserPhoto watchaUserPhoto = defaultPhoto.contains("default") ? new WatchaUserPhoto() : null;

        Assertions.assertNotNull(watchaUserPhoto);
        Assertions.assertEquals("", watchaUserPhoto.getLarge());
        Assertions.assertEquals("", watchaUserPhoto.getOriginal());
        Assertions.assertEquals("", watchaUserPhoto.getSmall());

    }
}