package fr.lebonq;

import org.aeonbits.owner.Config;

@Config.Sources(value = "file:config/configFtp.properties")
public interface ConfigFtp extends Config {
    String host();
    String username();
    String password();
    int port();
    String modsJsonRemoteDir();
    String modsJarDir();
}