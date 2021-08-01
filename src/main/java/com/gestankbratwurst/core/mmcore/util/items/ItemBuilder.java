package com.gestankbratwurst.core.mmcore.util.items;

import com.gestankbratwurst.core.mmcore.util.common.NamespaceFactory;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemBuilder {

  public ItemBuilder(final Material material) {
    this(new ItemStack(material));
  }

  public ItemBuilder(final ItemStack itemStack) {
    this.itemMeta = itemStack.getItemMeta();
    this.material = itemStack.getType();
    this.amount = itemStack.getAmount();
    if (this.itemMeta.hasLore()) {
      this.loreLines = Lists.newArrayList(Objects.requireNonNull(this.itemMeta.getLore()));
    } else {
      this.loreLines = Lists.newArrayList();
    }
  }

  private Material material;
  private ItemMeta itemMeta;
  private int amount;
  private final List<String> loreLines;

  public ItemBuilder clearLore() {
    this.editLore(List::clear);
    return this;
  }

  public ItemBuilder editLore(final Consumer<List<String>> loreConsumer) {
    loreConsumer.accept(this.loreLines);
    return this;
  }

  public ItemBuilder amount(final int amount) {
    this.amount = amount;
    return this;
  }

  public ItemBuilder meta(final ItemMeta meta) {
    this.itemMeta = meta;
    return this;
  }

  public ItemBuilder material(final Material material) {
    this.material = material;
    return this;
  }

  public ItemBuilder name(final String name) {
    this.itemMeta.setDisplayName(name);
    return this;
  }

  public ItemBuilder lore(final String line) {
    this.loreLines.add(line);
    return this;
  }

  public ItemBuilder lore(final String... lines) {
    this.loreLines.addAll(Arrays.asList(lines));
    return this;
  }

  public ItemBuilder setEnchantable(final boolean value) {
    final PersistentDataContainer persistentDataContainer = this.itemMeta.getPersistentDataContainer();
    if (value) {
      if (persistentDataContainer.has(NamespaceFactory.provide("ENCHANTBLOCK"), PersistentDataType.INTEGER)) {
        persistentDataContainer.remove(NamespaceFactory.provide("ENCHANTBLOCK"));
      }
    } else {
      persistentDataContainer.set(NamespaceFactory.provide("ENCHANTBLOCK"), PersistentDataType.INTEGER, 0);
    }
    return this;
  }

  public ItemBuilder setAnvilReceiver(final boolean value) {
    final PersistentDataContainer persistentDataContainer = this.itemMeta.getPersistentDataContainer();
    if (value) {
      if (persistentDataContainer.has(NamespaceFactory.provide("BLOCKANVILRECEIVER"), PersistentDataType.INTEGER)) {
        persistentDataContainer.remove(NamespaceFactory.provide("BLOCKANVILRECEIVER"));
      }
    } else {
      persistentDataContainer.set(NamespaceFactory.provide("BLOCKANVILRECEIVER"), PersistentDataType.INTEGER, 0);
    }
    return this;
  }

  public ItemBuilder setAnvilProvider(final boolean value) {
    final PersistentDataContainer persistentDataContainer = this.itemMeta.getPersistentDataContainer();
    if (value) {
      if (persistentDataContainer.has(NamespaceFactory.provide("BLOCKANVILPROVIDER"), PersistentDataType.INTEGER)) {
        persistentDataContainer.remove(NamespaceFactory.provide("BLOCKANVILPROVIDER"));
      }
    } else {
      persistentDataContainer.set(NamespaceFactory.provide("BLOCKANVILPROVIDER"), PersistentDataType.INTEGER, 0);
    }
    return this;
  }

  public ItemBuilder lore(final Collection<String> lines) {
    this.loreLines.addAll(lines);
    return this;
  }

  public ItemBuilder attribute(final Attribute attribute, final AttributeModifier modifier) {
    this.itemMeta.addAttributeModifier(attribute, modifier);
    return this;
  }

  public ItemBuilder enchant(final Enchantment enchantmen, final int level) {
    this.itemMeta.addEnchant(enchantmen, level, true);
    return this;
  }

  public ItemBuilder flag(final ItemFlag... flags) {
    this.itemMeta.addItemFlags(flags);
    return this;
  }

  public ItemBuilder setUnbreakable(final boolean value) {
    this.itemMeta.setUnbreakable(value);
    return this;
  }

  public <T, Z> ItemBuilder addPersistentData(final String key, final PersistentDataType<T, Z> type, final Z value) {
    final PersistentDataContainer pdc = this.itemMeta.getPersistentDataContainer();
    pdc.set(NamespaceFactory.provide(key), type, value);
    return this;
  }

  public ItemBuilder modelData(final int customModelData) {
    this.itemMeta.setCustomModelData(customModelData);
    return this;
  }

  public ItemStack build() {
    final ItemStack item = new ItemStack(this.material);
    if (!this.loreLines.isEmpty()) {
      this.itemMeta.setLore(this.loreLines);
    }
    item.setItemMeta(this.itemMeta);
    if (this.amount != 1) {
      item.setAmount(this.amount);
    }
    return item;
  }

}
