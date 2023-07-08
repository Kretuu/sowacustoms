package pl.sowacustoms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pl.sowacustoms.panel.orders.Order;

import static pl.sowacustoms.config.WebSocketConfiguration.MESSAGE_PREFIX;

@Component
@RepositoryEventHandler(Order.class)
public class EventHandler {

    private final SimpMessagingTemplate websocket;

    private final EntityLinks entityLinks;

    @Autowired
    public EventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @HandleAfterCreate
    public void newOrder(Order order) {
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/newOrder", getPath(order));
    }

    @HandleAfterDelete
    public void deleteOrder(Order order) {
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/deleteOrder", getPath(order));
    }

    @HandleAfterSave
    public void updateOrder(Order order) {
        this.websocket.convertAndSend(MESSAGE_PREFIX + "updateOrder", getPath(order));
    }

    private String getPath(Order order) {
        return this.entityLinks.linkForItemResource(order.getClass(), order.getId()).toUri().getPath();
    }
}
