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
        int i = 0;
        do {
            int num = ((buf[i] << 24) >>> 0) +
                    (((i + 1 > buf.length - 1 ? 0 : buf[i + 1]) << 16) >>> 0) +
                    (((i + 2 > buf.length - 1 ? 0 : buf[i + 2]) << 8) >>> 0) +
                    (((i + 3 > buf.length - 1 ? 0 : buf[i + 3]) << 0) >>> 0);
            StringBuilder block = new StringBuilder(1);
            int j = 0;
            do {
                block.insert(0, Character.toChars(33 + (num % 85))[0]);
                num = (int) Math.round(Math.floor(num / 85));
                ++j;
            } while (j < 5);
            result += block.toString();
            i += 4;
        } while (i < buf.length);
        return result.substring(0, result.length() - padding);
    }

    public static String makePassword(String input) throws NoSuchAlgorithmException {
        if (input.length() != 0) {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            return a85Encode(Base64.encodeToString(mDigest.digest(input.getBytes()), Base64.DEFAULT));
        } else {
            return "";
        }
    }

    public static String randomPassword() throws NoSuchAlgorithmException {
        SecureRandom rnd = new SecureRandom();
        StringBuilder str = new StringBuilder(1);
        int i = 0;
        do {
            str.insert(0, Character.toChars(32 + (rnd.nextInt(94)))[0]);
            i++;
        } while (i < 16);
        return makePassword(str.toString());
    }
}
