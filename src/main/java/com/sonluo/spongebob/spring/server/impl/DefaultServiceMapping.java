package com.sonluo.spongebob.spring.server.impl;

import com.sonluo.spongebob.spring.server.ServiceCall;
import com.sonluo.spongebob.spring.server.ServiceMapping;

import javax.annotation.Nullable;
import java.util.Map;

public class DefaultServiceMapping implements ServiceMapping {

    private Node[] nodes;

    @Override
    @Nullable
    public ServiceCall getServiceCall(String url) {
        int hash = (url.hashCode() & 0x7fffffff) % nodes.length;
        if (nodes[hash] == null) {
            return null;
        }
        Node node = nodes[hash];
        while (node != null) {
            if (url.equals(node.url)) {
                return node.serviceCall;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public void init(Map<String, ServiceCall> serviceCalls) {
        nodes = new Node[serviceCalls.size() * 2];
        serviceCalls.forEach((url, call) -> {
            int hash = (url.hashCode() & 0x7fffffff) % nodes.length;
            if (nodes[hash] == null) {
                nodes[hash] = new Node(url, call);
            } else {
                Node original = nodes[hash];
                Node newNode = new Node(url, call);
                newNode.next = original;
                nodes[hash] = newNode;
            }
        });
    }

    private class Node {
        private final String url;
        private final ServiceCall serviceCall;
        private Node next;

        Node(String url, ServiceCall serviceCall) {
            this.url = url;
            this.serviceCall = serviceCall;
        }
    }
}
