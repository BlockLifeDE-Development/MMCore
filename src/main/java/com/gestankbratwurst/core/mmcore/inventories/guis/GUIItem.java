package com.gestankbratwurst.core.mmcore.inventories.guis;

import java.util.function.Consumer;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Builder
@AllArgsConstructor
public class GUIItem {

  @Getter
  private final Function<Player, ItemStack> iconCreator;
  @Getter
  private final Consumer<InventoryClickEvent> eventConsumer;

}