package com.gestankbratwurst.core.mmcore.protocol.holograms;

import org.bukkit.Location;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 28.07.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface IHologram {

  void moveTo(Location location);

  Location getLocation();

}
