package com.gestankbratwurst.core.mmcore.util.tasks;

import com.gestankbratwurst.core.mmcore.MMCore;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class TaskManager {

  private static final TaskManager instance = TaskManager.getInstance();

  public static TaskManager getInstance() {
    return TaskManager.instance == null ? new TaskManager(JavaPlugin.getPlugin(MMCore.class)) : TaskManager.instance;
  }

  @Getter
  private final ScheduledExecutorService scheduler;
  private final BukkitScheduler bukkitScheduler;
  private final JavaPlugin plugin;

  public TaskManager(final MMCore plugin) {
    final int threadPoolSize = 2;
    this.bukkitScheduler = Bukkit.getScheduler();
    this.scheduler = Executors.newScheduledThreadPool(threadPoolSize);
    this.plugin = plugin;
  }

  public void runTaskTimer(final Runnable runnable, final int initialDelay, final int repeatedDelay) {
    Bukkit.getScheduler().runTaskTimer(this.plugin, runnable, initialDelay, repeatedDelay);
  }

  public <U> Consumer<U> consumeBukkitSync(final Consumer<U> asyncAction) {
    return (u) -> this.bukkitScheduler.runTask(this.plugin, () -> asyncAction.accept(u));
  }

  public <U, V> BiConsumer<U, V> biConsumeBukkitSync(final BiConsumer<U, V> asyncAction) {
    return (u, v) -> this.bukkitScheduler.runTask(this.plugin, () -> asyncAction.accept(u, v));
  }

  /**
   * Repeatedly executes a Runnable at a fixed rate. - Thread Async
   *
   * @param runnable the Runnable that is going to be executed.
   * @param delay    the delay before the first execution.
   * @param repeat   the delay between each repeated execution.
   * @param timeUnit the time units.
   * @return a ScheduledFuture
   */
  public ScheduledFuture<?> executeScheduledTask(final Runnable runnable, final long delay,
      final long repeat, final TimeUnit timeUnit) {
    return this.scheduler.scheduleAtFixedRate(runnable, delay, repeat, timeUnit);
  }

  // Replaces everything but 0-9
  private String extractNumber(final String input) {
    return input.replaceAll("[^0-9]", "");
  }

  // Strips color codes
  private String stripColor(final String input) {
    return ChatColor.stripColor(input);
  }

  /**
   * Executes a Runnable Task Async once. - Thread Async
   *
   * @param runnable the runnable to execute.
   */
  public void executeTask(final Runnable runnable) {
    this.scheduler.execute(runnable);
  }

  /**
   * Executes a Runnable. - Bukkit Sync
   *
   * @param runnable the runnable to execute.
   * @return
   */
  public BukkitTask runBukkitSync(final Runnable runnable) {
    return this.bukkitScheduler.runTask(this.plugin, runnable);
  }

  public int runBukkitSyncDelayed(final Runnable runnable, final long delyTicks) {
    return this.bukkitScheduler.scheduleSyncDelayedTask(this.plugin, runnable, delyTicks);
  }

  /**
   * Executes a Runnable. - Bukkit Async
   *
   * @param runnable the runnable to execute.
   * @return
   */
  public BukkitTask runBukkitAsync(final Runnable runnable) {
    return this.bukkitScheduler.runTaskAsynchronously(this.plugin, runnable);
  }

  /**
   * Repeatedly executes a Runnable. - Bukkit Sync - Delays in Ticks
   *
   * @param runnable    the runnable to execute.
   * @param delayTicks  the delay before the first execution.
   * @param repeatTicks the dealy between each repeated execution.
   * @return a BukkitTask
   */
  public BukkitTask runRepeatedBukkit(final Runnable runnable, final long delayTicks,
      final long repeatTicks) {
    return this.bukkitScheduler.runTaskTimer(this.plugin, runnable, delayTicks, repeatTicks);
  }

  /**
   * Repeatedly executes a Runnable. - Bukkit Async - Delays in Ticks
   *
   * @param runnable    the runnable to execute.
   * @param delayTicks  the delay before the first execution.
   * @param repeatTicks the dealy between each repeated execution.
   * @return a BukkitTask
   */
  public BukkitTask runRepeatedBukkitAsync(final Runnable runnable, final long delayTicks,
      final long repeatTicks) {
    return this.bukkitScheduler
        .runTaskTimerAsynchronously(this.plugin, runnable, delayTicks, repeatTicks);
  }

  /**
   * Repeats a Runnable a fixed amount of times. - BukkitSync - Delays in Ticks
   *
   * @param runnable     the runnable to execute.
   * @param delayTicks   the delay before the first execution.
   * @param repeatDelay  the delay between each further execution.
   * @param repeatAmount the amount of repeats.
   */
  public void runFixedTimesBukkitSync(final Runnable runnable, final long delayTicks,
      final long repeatDelay, final int repeatAmount) {
    TickedRunnable.start(runnable, delayTicks, repeatDelay, repeatAmount, this.plugin, true);
  }

  /**
   * Repeats a Runnable a fixed amount of times. - BukkitSync - Delays in Ticks
   *
   * @param runnable     the runnable to execute.
   * @param delayTicks   the delay before the first execution.
   * @param repeatDelay  the delay between each further execution.
   * @param repeatAmount the amount of repeats.
   */
  public void runFixedTimesBukkitAsync(final Runnable runnable, final long delayTicks,
      final long repeatDelay, final int repeatAmount) {
    TickedRunnable.start(runnable, delayTicks, repeatDelay, repeatAmount, this.plugin, false);
  }

}
