/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.streampipes.rest.impl;


import org.apache.streampipes.manager.health.CoreServiceStatusManager;
import org.apache.streampipes.rest.core.base.impl.AbstractRestResource;
import org.apache.streampipes.storage.api.ISpCoreConfigurationStorage;
import org.apache.streampipes.storage.management.StorageDispatcher;

import com.google.gson.JsonObject;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v2/setup")
public class Setup extends AbstractRestResource {

  private final ISpCoreConfigurationStorage storage = StorageDispatcher
      .INSTANCE.getNoSqlStore().getSpCoreConfigurationStorage();

  @GET
  @Path("/configured")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Endpoint is used by UI to determine whether the core is running upon startup",
      tags = {"Configurated"})
  public Response isConfigured() {
    JsonObject obj = new JsonObject();
    var statusManager = new CoreServiceStatusManager(storage);
    if (statusManager.isCoreReady()) {
      obj.addProperty("configured", true);
    } else {
      obj.addProperty("configured", false);
      obj.addProperty("setupRunning", false);
    }
    return ok(obj.toString());
  }
}
