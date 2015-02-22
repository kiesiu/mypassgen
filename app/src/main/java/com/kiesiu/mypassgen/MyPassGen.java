/*
 * Copyright 2015 Łukasz Kieś <kiesiu@kiesiu.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kiesiu.mypassgen;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class MyPassGen {
    private static String a85Encode(String input) {
        /* Based on base85 implementation on https://github.com/noseglid/base85/
         * Copyright by Alexander Olsson <noseglid(at)gmail.com>
         */
        byte[] buf = input.getBytes();
        int padding = (buf.length % 4 == 0) ? 0 : 4 - buf.length % 4;
        String result = "";
        for (int i = 0; i < buf.length; i += 4) {
            int num = ((buf[i] << 24) >>> 0) +
                    (((i + 1 > buf.length - 1 ? 0 : buf[i + 1]) << 16) >>> 0) +
                    (((i + 2 > buf.length - 1 ? 0 : buf[i + 2]) << 8) >>> 0) +
                    (((i + 3 > buf.length - 1 ? 0 : buf[i + 3]) << 0) >>> 0);
            StringBuilder block = new StringBuilder(1);
            for (int j = 0; j < 5; ++j) {
                block.insert(0, Character.toChars(33 + (num % 85))[0]);
                num = (int)Math.round(Math.floor(num / 85));
            }
            result += block.toString();
        }
        return result.substring(0, result.length() - padding);
    }

    public static String makePassword(String input) throws NoSuchAlgorithmException {
        if (input.length() == 0) {
            return "";
        }
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        return a85Encode(Base64.encodeToString(mDigest.digest(input.getBytes()), Base64.DEFAULT));
    }

    public static String randomPassword() {
        SecureRandom rnd = new SecureRandom();
        StringBuilder str = new StringBuilder(1);
        for (int i = 0; i < 16; i++) {
            str.insert(0, Character.toChars(32 + (rnd.nextInt(94)))[0]);
        }
        try {
            return makePassword(str.toString());
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
