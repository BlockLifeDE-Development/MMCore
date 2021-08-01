package com.gestankbratwurst.core.mmcore.util.nbtapi;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 28.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class NBTStringList implements List<String> {

  public NBTStringList() {
    this.nmsList = new NBTTagList();
  }

  public NBTStringList(final NBTTagList nmsList) {
    this.nmsList = nmsList;
  }

  protected final NBTTagList nmsList;

  @Override
  public int size() {
    return this.nmsList.size();
  }

  @Override
  public boolean isEmpty() {
    return this.nmsList.isEmpty();
  }

  @Override
  public boolean contains(final Object o) {
    return this.nmsList.contains(o);
  }

  @Override
  public Iterator<String> iterator() {
    final List<String> list = this.nmsList.stream().map(NBTBase::asString).collect(Collectors.toList());
    return list.iterator();
  }

  @Override
  public void forEach(final Consumer<? super String> action) {
    this.nmsList.forEach(b -> {
      action.accept(b.asString());
    });
  }

  @Override
  public String[] toArray() {
    final int size = this.nmsList.size();
    final String[] array = new String[size];
    for (int index = 0; index < size; index++) {
      array[index] = this.nmsList.getString(index);
    }
    return array;
  }

  @Override
  public <T> T[] toArray(final T[] a) {
    return null;
  }

  @Override
  public boolean add(final String element) {
    return this.nmsList.add(NBTTagString.a(element));
  }

  @Override
  public boolean remove(final Object o) {
    return this.nmsList.remove(o);
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    final List<String> list = this.nmsList.stream().map(NBTBase::asString).collect(Collectors.toList());
    return list.containsAll(c);
  }

  @Override
  public boolean addAll(final Collection<? extends String> c) {
    for (final String element : c) {
      if (!this.add(element)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean addAll(final int index, final Collection<? extends String> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    c.forEach(this::remove);
    return true;
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    this.nmsList.clear();
  }

  @Override
  public String get(final int index) {
    return this.nmsList.getString(index);
  }

  @Override
  public String set(final int index, final String element) {
    return this.nmsList.set(index, NBTTagString.a(element)).asString();
  }

  @Override
  public void add(final int index, final String element) {
    this.nmsList.add(index, NBTTagString.a(element));
  }

  @Override
  public String remove(final int index) {
    return this.nmsList.remove(index).asString();
  }

  @Override
  public int indexOf(final Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int lastIndexOf(final Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListIterator<String> listIterator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListIterator<String> listIterator(final int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> subList(final int fromIndex, final int toIndex) {
    throw new UnsupportedOperationException();
  }

}
