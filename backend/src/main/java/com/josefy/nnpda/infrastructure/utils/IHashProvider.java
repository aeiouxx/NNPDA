package com.josefy.nnpda.infrastructure.utils;

import java.io.IOException;
import java.io.InputStream;

public interface IHashProvider {
    public String hash(String input);
    public boolean isHmacValid(String input, String key, String hash);
    public boolean isHmacValid(InputStream input, String key, String hash) throws IOException;
    public String hmac(String input, String key);
}
