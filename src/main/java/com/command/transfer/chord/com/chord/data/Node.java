package com.command.transfer.chord.com.chord.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.alibaba.fastjson.JSONObject;
import com.command.transfer.chord.com.net.Client;
import com.command.transfer.chord.com.net.PacketType;
import com.command.transfer.chord.com.chord.tool.Const;

public class Node {
    static private Node instance;

    static public Node Instance() {
        return instance;
    }

    public Address addr;
    public Address predecessor;
    public Address successor;
    public FingerTable fingerTable;
    private Method onFindFileSuccess;
    private Object findFileInstance;

    private Node(String ip, int port) {
        this.addr = new Address(ip, port);
        this.fingerTable = new FingerTable(addr);
    }

    public static void CreateNewNode(String ip, int port, String seed) throws UnknownHostException, SocketException {
        Const.PORT = port;
        Node node = new Node(ip, port);
        if (seed.split(":").length < 2) {
            System.out.println("���������� ���� ��ʼ�ڵ㣺  ip��" + ip + "  port" + port);
            node.predecessor = new Address(node.addr.GetString());
            node.successor = new Address(node.addr.GetString());
            instance = node;
            return;
        }
        Address seedAddr = new Address(seed);
        JSONObject json = new JSONObject();
        json.put("PacketType", PacketType.FindLocWithCreateNode.ordinal());
        json.put("Address", node.addr.GetString());
        Client.Instance().send(seedAddr.GetIP(), seedAddr.GetPort(), json.toJSONString());
        instance = node;
    }

    private boolean isMyLoc(Long aimHash) {
        if (this.predecessor.HashCode() >= this.addr.HashCode()) {
            if (aimHash < this.addr.HashCode() || aimHash > this.predecessor.HashCode()) {
                return true;
            } else {
                return false;
            }
        } else {
            if (aimHash < this.addr.HashCode() && aimHash > this.predecessor.HashCode()) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void UpdateOwnFinger() throws UnknownHostException, SocketException {
        for (int i = 0; i < Const.RING_LOC_DIGIT; i++) {
            JSONObject sendJson = new JSONObject();
            sendJson.put("PacketType", PacketType.FindLoc.ordinal());
            sendJson.put("local", this.fingerTable.GetTableLoc(i));
            sendJson.put("Address", this.addr.GetString());
            sendJson.put("TTL", 16);
            Client.Instance().send(this.successor.GetIP(), this.successor.GetPort(), sendJson.toJSONString());
        }
    }

    private void UpdateOtherFinger() throws UnknownHostException, SocketException {
        JSONObject sendJson = new JSONObject();
        sendJson.put("PacketType", PacketType.UpdateNode.ordinal());
        sendJson.put("Address", this.addr.GetString());
        Client.Instance().send(this.predecessor.GetIP(), this.predecessor.GetPort(), sendJson.toJSONString());
    }

    public void FinddLocWithCreateNode(JSONObject json) throws UnknownHostException, SocketException {
        Address aimAddr = new Address(json.getString("Address"));
        if (isMyLoc(aimAddr.HashCode())) {
            JSONObject sendJson = new JSONObject();
            sendJson.put("PacketType", PacketType.SuccessFindCreateNodeLoc.ordinal());
            sendJson.put("predecessor", this.predecessor.GetString());
            sendJson.put("successor", this.addr.GetString());
            Client.Instance().send(aimAddr.GetIP(), aimAddr.GetPort(), sendJson.toJSONString());

            JSONObject sendJson1 = new JSONObject();
            sendJson1.put("PacketType", PacketType.ChangeSuccessor.ordinal());
            sendJson1.put("successor", json.getString("Address"));
            Client.Instance().send(this.predecessor.GetIP(), this.predecessor.GetPort(), sendJson1.toJSONString());

            this.predecessor = aimAddr;
        } else {
            Finger finger = this.fingerTable.SearchWithHash(aimAddr.HashCode());
            Client.Instance().send(finger.addr.GetIP(), finger.addr.GetPort(), json.toJSONString());
        }
    }

    public void SuccessFindCreateNodeLoc(JSONObject json) throws UnknownHostException, SocketException {
        this.predecessor = new Address(json.getString("predecessor"));
        this.successor = new Address(json.getString("successor"));
        System.out.println("����ڵ� ����   ip��" + this.addr.GetIP() + "  port" + this.addr.GetPort());

        //�ڵ������������finger��������finger
        this.UpdateOwnFinger();
    }

    public void ChangeSuccessor(JSONObject json) {
        this.successor = new Address(json.getString("successor"));
    }

    public void FindLoc(JSONObject json) throws UnknownHostException, SocketException {
        if (json.getInteger("TTL") <= 0) {
            Address aimAddr = new Address(json.getString("Address"));
            JSONObject sendJson = new JSONObject();
            sendJson.put("PacketType", PacketType.SuccessFindLoc.ordinal());
            sendJson.put("local", json.getLong("local"));
            sendJson.put("Address", "");
            Client.Instance().send(aimAddr.GetIP(), aimAddr.GetPort(), sendJson.toJSONString());
            return;
        }
        if (isMyLoc(json.getLong("local"))) {
            Address aimAddr = new Address(json.getString("Address"));
            JSONObject sendJson = new JSONObject();
            sendJson.put("PacketType", PacketType.SuccessFindLoc.ordinal());
            sendJson.put("local", json.getLong("local"));
            sendJson.put("Address", this.addr.GetString());
            Client.Instance().send(aimAddr.GetIP(), aimAddr.GetPort(), sendJson.toJSONString());
        } else {
            Finger finger = this.fingerTable.SearchWithHash(json.getLong("local"));
            int TTL = json.getInteger("TTL");
            json.put("TTL", TTL - 1);
            Client.Instance().send(finger.addr.GetIP(), finger.addr.GetPort(), json.toJSONString());
        }
    }

    int count = 0;

    public void SuccessFindLoc(JSONObject json) throws UnknownHostException, SocketException {
        count++;

        int i = this.fingerTable.GetTableNum(json.getLong("local"));
        if (json.getString("Address").equals("")) {
            this.fingerTable.SetTable(i, null, true);
        } else {
            this.fingerTable.SetTable(i, new Address(json.getString("Address")), true);
        }
        if (this.fingerTable.IsTableFull()) {
            this.UpdateOtherFinger();
        }
    }

    public void UpdateNode(JSONObject json) throws UnknownHostException, SocketException {
        Address aimAddr = new Address(json.getString("Address"));
        if (aimAddr.HashCode() == this.addr.HashCode()) {
            System.out.println("Finger������� ����   ip��" + this.addr.GetIP() + "  port" + this.addr.GetPort());
        } else {
            this.fingerTable.Update(aimAddr);
            Client.Instance().send(this.predecessor.GetIP(), this.predecessor.GetPort(), json.toJSONString());
        }
    }

    public void FindFile(Long fileLoc, String fileName, Method onFindFile, Object instance) throws UnknownHostException, SocketException {
        this.onFindFileSuccess = onFindFile;
        this.findFileInstance = instance;
        JSONObject sendJson = new JSONObject();
        sendJson.put("PacketType", PacketType.FindFileLoc.ordinal());
        sendJson.put("local", fileLoc);
        sendJson.put("Address", this.addr.GetString());
        sendJson.put("TTL", 16);
        sendJson.put("fileName", fileName);
        Client.Instance().send(this.successor.GetIP(), this.successor.GetPort(), sendJson.toJSONString());
    }

    public void FindFileLoc(JSONObject json) throws UnknownHostException, SocketException {
        if (json.getInteger("TTL") <= 0) {
            Address aimAddr = new Address(json.getString("Address"));
            JSONObject sendJson = new JSONObject();
            sendJson.put("PacketType", PacketType.SuccessFindFileLoc.ordinal());
            sendJson.put("local", json.getLong("local"));
            sendJson.put("Address", "");
            sendJson.put("fileName", json.getString("fileName"));
            Client.Instance().send(aimAddr.GetIP(), aimAddr.GetPort(), sendJson.toJSONString());
            return;
        }
        if (isMyLoc(json.getLong("local"))) {
            Address aimAddr = new Address(json.getString("Address"));
            JSONObject sendJson = new JSONObject();
            sendJson.put("PacketType", PacketType.SuccessFindFileLoc.ordinal());
            sendJson.put("local", json.getLong("local"));
            sendJson.put("Address", this.addr.GetString());
            sendJson.put("fileName", json.getString("fileName"));
            Client.Instance().send(aimAddr.GetIP(), aimAddr.GetPort(), sendJson.toJSONString());
        } else {
            Finger finger = this.fingerTable.SearchWithHash(json.getLong("local"));
            int TTL = json.getInteger("TTL");
            json.put("TTL", TTL - 1);
            Client.Instance().send(finger.addr.GetIP(), finger.addr.GetPort(), json.toJSONString());
        }
    }

    public void SuccessFindFileLoc(JSONObject json) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (json.getString("Address").equals("")) {
            this.onFindFileSuccess.invoke(this.findFileInstance, null);
        } else {
            this.onFindFileSuccess.invoke(this.findFileInstance, json.getString("Address") + ";" + json.getString("local") + ";" + json.getString("fileName"));
        }
    }
}
