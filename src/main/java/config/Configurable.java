package config;

import exception.ServerException;

/**
 * @author bsun
 */
public interface Configurable {
    void configure() throws ServerException;
}
