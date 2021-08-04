package com.gestankbratwurst.core.mmcore.inventories.guis;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractGUIInventory implements IInventoryHandler {

  private final Int2ObjectMap<GUIItem> buttonMap = new Int2ObjectOpenHashMap<>();

  protected void update(final Player player) {
    final Inventory inventory = player.getOpenInventory().getTopInventory();
    GUIManager.getInstance().getOptionalHandlerOf(inventory).ifPresent(handler -> this.prepareInventory(player, inventory));
  }

  protected void reopen(final Player player) {
    player.closeInventory();
    this.openFor(player);
  }

  public void openFor(final Player player) {
    final GUIManager guiManager = GUIManager.getInstance();
    final Inventory inventory = this.createInventory(player);
    guiManager.addHandler(inventory, this);
    player.openInventory(inventory);
  }

  private void prepareInventory(final Player player, final Inventory inventory) {
    this.buttonMap.clear();
    this.init(player);
    inventory.clear();
    for (final Int2ObjectMap.Entry<GUIItem> guiEntry : this.buttonMap.int2ObjectEntrySet()) {
      final Function<Player, ItemStack> itemCreator = guiEntry.getValue().getIconCreator();
      inventory.setItem(guiEntry.getIntKey(), itemCreator == null ? null : itemCreator.apply(player));
    }
  }

  protected void addGUIItem(final GUIItem guiItem) {
    for (int i = 0; i < 9 * 7; i++) {
      if (!this.buttonMap.containsKey(i)) {
        this.buttonMap.put(i, guiItem);
        return;
      }
    }
  }

  protected void setGUIItem(final int slot, final GUIItem guiItem) {
    this.buttonMap.put(slot, guiItem);
  }

  protected abstract Inventory createInventory(Player player);

  protected abstract void init(Player player);

  protected void handlePrimaryInventoryClick(final InventoryClickEvent event) {
    final int slot = event.getSlot();
    final GUIItem guiItem = this.buttonMap.get(slot);
    if (guiItem != null) {
      final Consumer<InventoryClickEvent> eventConsumer = guiItem.getEventConsumer();
      if (eventConsumer != null) {
        eventConsumer.accept(event);
        event.setCancelled(true);
      }
    }
  }

  protected void handleSecondaryInventoryClick(final InventoryClickEvent event) {
    event.setCancelled(true);
  }

  protected void preClose(final InventoryCloseEvent event) {

  }

  protected void preOpen(final InventoryOpenEvent event) {

  }

  @Override
  public void handleClick(final InventoryClickEvent event) {
    final Inventory clickedInventory = event.getClickedInventory();
    if (clickedInventory == null) {
      return;
    }
    if (clickedInventory.equals(event.getInventory())) {
      this.handlePrimaryInventoryClick(event);
    } else {
      this.handleSecondaryInventoryClick(event);
    }
  }

  @Override
  public void handleOpen(final InventoryOpenEvent event) {
    this.preOpen(event);
    this.prepareInventory((Player) event.getPlayer(), event.getInventory());
  }

  @Override
  public void handleClose(final InventoryCloseEvent event) {
    this.preClose(event);
    GUIManager.getInstance().terminate(event.getInventory());
  }

}