package com.gestankbratwurst.core.mmcore.util.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class TickedRunnable implements Runnable {

  /**
   * Executes a Runnable a fixed amount of times. Bukkit sync
   *
   * @param runnable    the runnable that is going to be executed.
   * @param delayTicks  delay before the first execution.
   * @param repeatDelay delay between each repetetive execution.
   * @param repeats     amount of repeats.
   * @param plugin      the core plugin.
   * @param sync        if the task is started sync
   */
  protected static void start(final Runnable runnable, final long delayTicks, final long repeatDelay, final int repeats,
      final JavaPlugin plugin, final boolean sync) {
    final TickedRunnable encapsulatedRunnable = new TickedRunnable(runnable, repeats, Bukkit.getScheduler(), delayTicks, repeatDelay,
        plugin, sync);
  }

  /**
   * @param runnable
   * @param repeats
   * @param bukkitScheduler
   * @param delay
   * @param repeatDelay
   * @param plugin
   * @param sync
   */
  private TickedRunnable(final Runnable runnable, final int repeats, final BukkitScheduler bukkitScheduler, final long delay,
      final long repeatDelay, final JavaPlugin plugin, final boolean sync) {
    this.runnable = runnable;
    this.maxRepeats = repeats;
    this.taskID = sync ? bukkitScheduler.runTaskTimer(plugin, this, delay, repeatDelay).getTaskId()
        : bukkitScheduler.runTaskTimerAsynchronously(plugin, this, delay, repeatDelay).getTaskId();
    this.bukkitScheduler = bukkitScheduler;
  }

  private final int taskID;
  private final Runnable runnable;
  private final int maxRepeats;
  private final BukkitScheduler bukkitScheduler;
  private int timesTicked;

  /**
   *
   */
  @Override
  public void run() {
    this.runnable.run();
    this.timesTicked++;
    if (this.timesTicked == this.maxRepeats) {
      this.bukkitScheduler.cancelTask(this.taskID);
    }
  }
}
