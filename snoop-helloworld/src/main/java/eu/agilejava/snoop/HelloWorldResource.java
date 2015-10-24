/*
 * The MIT License
 *
 * Copyright 2015 Ivar Grimstad (ivar.grimstad@gmail.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package eu.agilejava.snoop;

import eu.agilejava.snoop.annotation.Snoop;
import eu.agilejava.snoop.client.SnoopServiceClient;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.mvc.annotation.Controller;
import javax.mvc.annotation.View;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Ivar Grimstad (ivar.grimstad@gmail.com)
 */
@RequestScoped
@Controller
@Path("helloworld")
public class HelloWorldResource {

   private static final Logger LOGGER = Logger.getLogger("eu.agilejava.snoop");

   @Inject
   @Snoop(serviceName = "hello")
   private SnoopServiceClient helloService;

   @Inject
   @Snoop(serviceName = "world")
   private SnoopServiceClient worldService;

   @Inject
   private Models model;
   
   @GET
   public String greet() {

      LOGGER.info(() -> "greeting " + helloService);

      String helloResponse = helloService.simpleGet("hello")
              .filter(r -> r.getStatus() == 200)
              .map(r -> r.readEntity(String.class))
              .orElse("goodbye");

      LOGGER.info(() -> "response " + helloResponse);

      String worldResponse = worldService.simpleGet("world")
              .filter(r -> r.getStatus() == 200)
              .map(r -> r.readEntity(String.class))
              .orElse("");

      model.put("greeting", helloResponse + " " + worldResponse);
      
      return "helloworld.jsp";
   }
}
