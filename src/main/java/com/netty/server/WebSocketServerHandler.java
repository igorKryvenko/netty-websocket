package com.netty.server;

import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.HOST;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.PRAGMA;

/**
 * Created by ikryvenko on 20.06.17.
 */
public class WebSocketServerHandler extends SimpleChannelUpstreamHandler {
    private static final String WEBSOCKET_PATH = "/ws";

    private WebSocketServerHandshaker handshaker;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object message = e.getMessage();
        if(message instanceof HttpRequest) {
            handleHandshake(ctx,(HttpRequest)message);
        }
        if(message instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx,(WebSocketFrame)message);
        }
        super.messageReceived(ctx, e);
    }

    private void handleHandshake(ChannelHandlerContext ctx, HttpRequest request) {
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(request),null,false);

        handshaker = wsFactory.newHandshaker(request);
        if(handshaker == null) {
            wsFactory.sendUnsupportedWebSocketVersionResponse(ctx.getChannel());
        } else {
            handshaker.handshake(ctx.getChannel(),request).addListener(WebSocketServerHandshaker.HANDSHAKE_LISTENER);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        String request = ((TextWebSocketFrame)frame).getText();
        System.err.println(String.format("Channel %s received %s", ctx.getChannel().getId(), request));
        ctx.getChannel().write(new TextWebSocketFrame(request.toUpperCase()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
    private static String getWebSocketLocation(HttpRequest req) {
        String location =  req.headers().get(HOST) + WEBSOCKET_PATH;
        System.out.println(location);
        return "ws://" + location;
    }


}
