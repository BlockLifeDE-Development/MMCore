package com.gestankbratwurst.core.mmcore.inventories.anvil;

import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;

/**
 * Matches the server's NMS version to its {@link VersionWrapper}
 *
 * @author Wesley Smith
 * @since 1.2.1
 */
public class VersionMatcher {

  /**
   * The server's version
   */
  private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
  /**
   * All available {@link VersionWrapper}s
   */
  private final List<Class<? extends VersionWrapper>> versions = Collections.singletonList(
      Wrapper1_17_R1.class
  );

  /**
   * Matches the server version to it's {@link VersionWrapper}
   *
   * @return The {@link VersionWrapper} for this server
   * @throws RuntimeException If AnvilGUI doesn't support this server version
   */
  public VersionWrapper match() {
    try {
      return this.versions.stream()
          .filter(version -> version.getSimpleName().substring(7).equals(this.serverVersion))
          .findFirst().orElseThrow(() -> new RuntimeException("Your server version isn't supported in AnvilGUI!"))
          .newInstance();
    } catch (final IllegalAccessException | InstantiationException ex) {
      throw new RuntimeException(ex);
    }
  }

}
